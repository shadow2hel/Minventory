package me.shadow2hel.minventory;

import me.shadow2hel.minventory.data.managers.IMobManager;
import me.shadow2hel.minventory.data.managers.IPlayerInventoryManager;
import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.model.MobWithItem;
import me.shadow2hel.minventory.model.ModelPlayer;
import me.shadow2hel.minventory.model.TouchedInventory;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;
import java.util.stream.Collectors;

public class Wiper {
    private final IMobManager mobManager;
    private final IPlayerInventoryManager playerInventoryManager;
    private final IPlayerManager playerManager;
    private final JavaPlugin main;

    public Wiper(IMobManager mobManager, IPlayerInventoryManager playerInventoryManager, IPlayerManager playerManager, JavaPlugin main) {
        this.mobManager = mobManager;
        this.playerInventoryManager = playerInventoryManager;
        this.playerManager = playerManager;
        this.main = main;
        trackEntities();
    }

    private List<Chunk> getNearbyChunks(World world, int location_x, int location_z) {
        List<Chunk> possibleChunks = new ArrayList<>();
        Chunk currentChunk = world.getChunkAt(location_x / 16, location_z / 16);
        int relativex = Math.abs(location_x % 16);
        int relativey = Math.abs(location_z % 16);
        if ((relativex >= 7 && relativex <= 9) &&
                (relativey >= 7 && relativey <= 9)) {
            possibleChunks.add(world.getChunkAt(location_x / 16, location_z / 16));
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
        scheduler.runTaskTimer(main, () -> {
            List<MobWithItem> dataMobs = mobManager.readAllMobWithItem();
            if (dataMobs.size() > 0) {
                for (MobWithItem dataMob : dataMobs) {
                    World world = Bukkit.getWorld(dataMob.getWorld());
                    assert world != null;
                    List<Chunk> possibleChunks = getNearbyChunks(world, dataMob.getLocation_x(), dataMob.getLocation_z());
                    Entity foundEntity = possibleChunks
                            .stream()
                            .flatMap(chunk -> Arrays.stream(chunk.getEntities()))
                            .filter(entity -> entity.getUniqueId().toString().equals(dataMob.getUUID()))
                            .findAny()
                            .orElse(null);
                    if (foundEntity == null) {
                        mobManager.deleteMobWithItem(dataMob);
                    } else {
                        MobWithItem updatedMob = new MobWithItem(
                                foundEntity.getUniqueId().toString(),
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
        List<MobWithItem> mobs = mobManager.readAllMobWithItem();
        for (MobWithItem mob :
                mobs) {
            Objects.requireNonNull(Bukkit.getWorld(mob.getWorld())).getChunkAt(mob.getLocation_x() / 16, mob.getLocation_z() / 16);
            Entity entity = getEntity(mob.getUUID());
            if (entity != null) {
                InventoryHolder realMob = (InventoryHolder) entity;
                realMob.getInventory().clear();
                main.getLogger().info(String.format("Wiped %s %s", mob.getType(), mob.getUUID()));
            }
            mobManager.deleteMobWithItem(mob);
        }
    }

    private void wipeContainers() {
        List<TouchedInventory> containers = playerInventoryManager.readAllTouchedInventory();
        for (TouchedInventory container : containers) {
            Objects.requireNonNull(Bukkit.getWorld(container.getWorld())).getChunkAt(container.getLocationX() / 16, container.getLocationZ() /16);
            InventoryHolder foundContainer = getContainer(Objects.requireNonNull(Bukkit.getWorld(container.getWorld())), container.getLocationX(), container.getLocationY(), container.getLocationZ());
            if (foundContainer != null) {
                if (foundContainer instanceof Furnace furnaceContainer) {
                    furnaceContainer.getInventory().clear(0);
                } else {
                    foundContainer.getInventory().clear();
                }
                main.getLogger().info(String.format("Wiped %s at %o %o %o",
                        container.getType(),
                        container.getLocationX(),
                        container.getLocationY(),
                        container.getLocationZ()));
            }
            playerInventoryManager.deleteTouchedInventory(container);
        }
    }

    private void wipeEnderchests() {
        List<ModelPlayer> players = playerManager.readAllPlayers();
        players.forEach(player -> {
            player.setEnderChestWiped(false);
            playerManager.updatePlayer(player);
        });
        main.getLogger().info("Enderchests are marked for wipe");
    }

    public void wipeEnderchest(Player player) {
        player.getEnderChest().clear();
        ModelPlayer dataPlayer = playerManager.readPlayer(player.getUniqueId().toString());
        dataPlayer.setEnderChestWiped(true);
        playerManager.updatePlayer(dataPlayer);
        main.getLogger().info(String.format("%s their Ender Chest has been wiped.", player.getName()));
    }

    private Entity getEntity(String UUID) {
        return Bukkit.getEntity(java.util.UUID.fromString(UUID));
    }

    private InventoryHolder getContainer(World world, int location_x, int location_y, int location_z) {
        Block block = world.getBlockAt(location_x, location_y, location_z);
        if (block.getState() instanceof InventoryHolder container) {
            return container;
        }
        return null;
    }
}
