package me.shadow2hel.minventory;

import com.google.common.collect.ImmutableList;
import me.shadow2hel.minventory.constants.MESSAGES;
import me.shadow2hel.minventory.data.managers.IEntityManager;
import me.shadow2hel.minventory.data.managers.IPlayerInventoryManager;
import me.shadow2hel.minventory.data.managers.IPlayerManager;
import me.shadow2hel.minventory.data.managers.IWipeManager;
import me.shadow2hel.minventory.model.EntityItemTracker;
import me.shadow2hel.minventory.model.PlayerTracker;
import me.shadow2hel.minventory.model.InventoryTracker;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.entity.*;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class Wiper {
    private boolean isWiping;
    private final IEntityManager mobManager;
    private final IPlayerInventoryManager playerInventoryManager;
    private final IPlayerManager playerManager;
    private final IWipeManager wipeManager;
    private final JavaPlugin main;

    public Wiper(IEntityManager mobManager, IPlayerInventoryManager playerInventoryManager, IPlayerManager playerManager, IWipeManager wipeManager, JavaPlugin main) {
        this.isWiping = false;
        this.mobManager = mobManager;
        this.playerInventoryManager = playerInventoryManager;
        this.playerManager = playerManager;
        this.wipeManager = wipeManager;
        this.main = main;
        trackEntities();
        wipeCheckOnCrash();
        wipeTimer();
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

    public void wipeCheckOnCrash() {
        Date lastWipeDate = wipeManager.readLatestWipe();
        if (lastWipeDate != null) {
            Calendar lastWipe = Calendar.getInstance();
            lastWipe.setTime(lastWipeDate);
            Calendar currentTime = Calendar.getInstance();
            currentTime.setTime(new Date());
            // Check if today has already been wiped
            if (lastWipe.get(Calendar.DAY_OF_YEAR) != currentTime.get(Calendar.DAY_OF_YEAR)
            && lastWipe.get(Calendar.YEAR) == currentTime.get(Calendar.YEAR)
            && lastWipe.get(Calendar.HOUR_OF_DAY) == 0) {
                wipe();
            }
        }
    }

    private void wipeTimer() {
        Bukkit.getScheduler().runTaskTimer(main, task -> {
            Calendar currentTime = Calendar.getInstance();
            if (currentTime.get(Calendar.HOUR_OF_DAY) == 23 && currentTime.get(Calendar.MINUTE) >= 30) {
                if (currentTime.get(Calendar.MINUTE) < 45 && currentTime.get(Calendar.MINUTE) % 15 == 0 && currentTime.get(Calendar.SECOND) == 0) {
                    wipeNotification("", Math.abs(currentTime.get(Calendar.MINUTE) - 60), "minutes", 10, 200, 10 );
                } else if (currentTime.get(Calendar.MINUTE) < 59 && currentTime.get(Calendar.MINUTE) % 5 == 0 && currentTime.get(Calendar.SECOND) == 0) {
                    wipeNotification("", Math.abs(currentTime.get(Calendar.MINUTE) - 60), "minutes", 10, 200, 10 );
                } else if (currentTime.get(Calendar.MINUTE) == 59 && currentTime.get(Calendar.SECOND) == 0) {
                    wipeNotification("", Math.abs(currentTime.get(Calendar.MINUTE) - 60), "minute", 10, 200, 10 );
                } else if (currentTime.get(Calendar.MINUTE) == 59 && currentTime.get(Calendar.SECOND) == 30) {
                    wipeNotification("", Math.abs(currentTime.get(Calendar.SECOND) - 60), "seconds", 10, 200, 10 );
                } else if (currentTime.get(Calendar.MINUTE) == 59 && currentTime.get(Calendar.SECOND) >= 50) {
                    wipeNotification("" + (60 - currentTime.get(Calendar.SECOND)), currentTime.get(Calendar.SECOND), "", 5, 20, 5 );
                }
            } else if (currentTime.get(Calendar.HOUR_OF_DAY) == 0 && currentTime.get(Calendar.MINUTE) == 0 && currentTime.get(Calendar.SECOND) == 0) {
                task.cancel();
                scheduleWipe();
            }
        }, 0L, 20L);
    }

    private void wipeNotification(String title, int timeLeft, String timeType, int fadeIn, int stay, int fadeOut) {
        main.getLogger().info("Server restarting in " + timeLeft + " " + timeType);
        if (title.isEmpty()) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendTitle(ChatColor.BOLD + "" + ChatColor.GOLD + "Wipe in " + timeLeft + " " + timeType,
                        ChatColor.DARK_GREEN + "Collect your goods!",
                        fadeIn,
                        stay,
                        fadeOut);
            });
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendTitle(ChatColor.BOLD + "" + ChatColor.DARK_RED + title,
                        "",
                        fadeIn,
                        stay,
                        fadeOut);
            });
        }

    }
    public void scheduleWipe() {
        isWiping = true;
        main.getServer().getOnlinePlayers().forEach(player -> player.kickPlayer(MESSAGES.PLAYERJOINWIPE));
        wipe();
        main.getServer().shutdown();
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
        main.getLogger().info("" + new Date().getTime());
        wipeManager.createWipe(new Date());
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

    public boolean isWiping() {
        return isWiping;
    }
}
