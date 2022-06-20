package me.shadow2hel.minventory;

import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.data.managers.IPlayerInventoryManager;
import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.model.EntityItemTracker;
import me.shadow2hel.minventory.model.PlayerTracker;
import me.shadow2hel.minventory.model.InventoryTracker;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public class Wiper {
    private final IEntityManager mobManager;
    private final IPlayerInventoryManager playerInventoryManager;
    private final IPlayerManager playerManager;
    private final JavaPlugin main;

    public Wiper(IEntityManager mobManager, IPlayerInventoryManager playerInventoryManager, IPlayerManager playerManager, JavaPlugin main) {
        this.mobManager = mobManager;
        this.playerInventoryManager = playerInventoryManager;
        this.playerManager = playerManager;
        this.main = main;
        trackEntities();
    }

    private List<Chunk> getNearbyChunks(World world, int location_x, int location_z) {
        List<Chunk> possibleChunks = new ArrayList<>();
        Chunk currentChunk = world.getChunkAt((int) Math.floor((double) location_x / 16.0), (int) Math.floor((double) location_z / 16.0));
        int relativex = location_x % 16;
        int relativey = location_z % 16;
        relativex = relativex < 0 ? 16 + relativex : relativex;
        relativey = relativey < 0 ? 16 + relativey : relativey;
        if ((relativex >= 7 && relativex <= 9) &&
                (relativey >= 7 && relativey <= 9)) {
            possibleChunks.add(currentChunk);
        } else {
            // Made by Sleepernl
            int xadd = relativex > 8 ? 1 : -1;
            int yadd = relativey > 8 ? 1 : -1;
            possibleChunks.add(currentChunk);
            possibleChunks.add(world.getChunkAt(currentChunk.getX(), currentChunk.getZ() + yadd));
            possibleChunks.add(world.getChunkAt(currentChunk.getX() + xadd, currentChunk.getZ() + yadd));
            possibleChunks.add(world.getChunkAt(currentChunk.getX() + xadd, currentChunk.getZ()));
        }
        return possibleChunks;
    }


    private void trackEntities() {
        BukkitScheduler scheduler = Bukkit.getScheduler();
        NamespacedKey key = new NamespacedKey(main, "minv_uuid");
        scheduler.runTaskTimer(main, () -> {
            List<EntityItemTracker> dataMobs = mobManager.readAllMobWithItem();
            if (dataMobs.size() > 0) {
                for (EntityItemTracker dataMob : dataMobs) {
                    World world = Bukkit.getWorld(dataMob.getWorld());
                    assert world != null;
                    List<Chunk> possibleChunks = getNearbyChunks(world, dataMob.getLocation_x(), dataMob.getLocation_z());
                    Entity foundEntity = possibleChunks
                            .stream()
                            .flatMap(chunk -> Arrays.stream(chunk.getEntities()))
                            .filter(entity -> {
                                if (entity instanceof Item itemEntity) {
                                    PersistentDataContainer nbt = itemEntity.getPersistentDataContainer();
                                    if (nbt.has(key, PersistentDataType.STRING)) {
                                        String uuid = nbt.get(key, PersistentDataType.STRING);
                                        return uuid != null && uuid.equals(dataMob.getUUID());
                                    }
                                }
                                return entity.getUniqueId().toString().equals(dataMob.getUUID());
                            })
                            .findAny()
                            .orElse(null);
                    if (foundEntity == null) {
                        mobManager.deleteMobWithItem(dataMob);
                    } else {
                        EntityItemTracker updatedMob = new EntityItemTracker(
                                dataMob.getUUID(),
                                foundEntity.getCustomName() != null,
                                foundEntity.getType().toString(),
                                (int) foundEntity.getLocation().getX(),
                                (int) foundEntity.getLocation().getY(),
                                (int) foundEntity.getLocation().getZ(),
                                foundEntity.getLocation().getWorld().getName());
                        mobManager.updateMobWithItem(updatedMob);
                    }
                    ;
                }
            }
        }, 1L, 1L);
    }

    public boolean wipe() {
        wipeEntities();
        wipeContainers();
        wipeEnderchests();
        return true;
    }

    private void wipeEntities() {
        List<EntityItemTracker> mobs = mobManager.readAllMobWithItem();
        NamespacedKey key = new NamespacedKey(main, "minv_uuid");
        for (EntityItemTracker mob :
                mobs) {
            Objects.requireNonNull(Bukkit.getWorld(mob.getWorld())).getChunkAt(mob.getLocation_x() / 16, mob.getLocation_z() / 16);
            Entity entity = getEntity(mob);
            if (entity != null) {
                if (entity instanceof Item itemEntity) {
                    itemEntity.remove();
                } else {
                    InventoryHolder realMob = (InventoryHolder) entity;
                    realMob.getInventory().clear();
                }
            }
            mobManager.deleteMobWithItem(mob);
        }
    }

    private void wipeContainers() {
        List<InventoryTracker> containers = playerInventoryManager.readAllTouchedInventory();
        for (InventoryTracker container : containers) {
            Objects.requireNonNull(Bukkit.getWorld(container.getWorld())).getChunkAt(container.getLocationX() / 16, container.getLocationZ() / 16);
            InventoryHolder foundContainer = getContainer(Objects.requireNonNull(Bukkit.getWorld(container.getWorld())), container.getLocationX(), container.getLocationY(), container.getLocationZ());
            if (foundContainer != null) {
                if (foundContainer instanceof Furnace furnaceContainer) {
                    furnaceContainer.getInventory().clear(0);
                } else {
                    foundContainer.getInventory().clear();
                }
                main.getLogger().info(String.format("Wiped %s at %s %s %s",
                        container.getType(),
                        container.getLocationX(),
                        container.getLocationY(),
                        container.getLocationZ()));
            }
            playerInventoryManager.deleteTouchedInventory(container);
        }
    }


    private void wipeEnderchests() {
        List<PlayerTracker> players = playerManager.readAllPlayers();
        for (PlayerTracker player : players) {
            player.setEnderChestWiped(false);
            PlayerTracker updatedPlayer = playerManager.updatePlayer(player);
        }
        main.getLogger().info("Enderchests are marked for wipe");
    }

    public void wipeEnderchest(Player player) {
        player.getEnderChest().clear();
        PlayerTracker dataPlayer = playerManager.readPlayer(player.getUniqueId().toString());
        dataPlayer.setEnderChestWiped(true);
        playerManager.updatePlayer(dataPlayer);
        main.getLogger().info(String.format("%s their Ender Chest has been wiped.", player.getName()));
    }

    private Entity getEntity(EntityItemTracker entity) {
        World currentWorld = Bukkit.getWorld(entity.getWorld());
        NamespacedKey key = new NamespacedKey(main, "minv_uuid");
        if (currentWorld != null && entity.getType().equalsIgnoreCase("dropped_item")) {
            Collection<Entity> items = currentWorld.getNearbyEntities(new Location(currentWorld,
                    entity.getLocation_x(),
                    entity.getLocation_y(),
                    entity.getLocation_z()), 2, 2, 2, (filterEntity) -> filterEntity.getType() == EntityType.DROPPED_ITEM);
            for (Entity item : items) {
                PersistentDataContainer nbt = item.getPersistentDataContainer();
                if (nbt.has(key, PersistentDataType.STRING)) {
                    String uuid = nbt.get(key, PersistentDataType.STRING);
                    if (uuid != null) {
                        if (uuid.equals(entity.getUUID())) {
                            return item;
                        }
                    }
                    return null;
                }
            }
        }
        return Bukkit.getEntity(java.util.UUID.fromString(entity.getUUID()));
    }

    private InventoryHolder getContainer(World world, int location_x, int location_y, int location_z) {
        Block block = world.getBlockAt(location_x, location_y, location_z);
        if (block.getState() instanceof InventoryHolder container) {
            return container;
        }
        return null;
    }
}
