package me.shadow2hel.minventory;

import com.google.common.collect.ImmutableList;
import me.shadow2hel.minventory.constants.KEYS;
import me.shadow2hel.minventory.constants.MESSAGES;
import me.shadow2hel.minventory.constants.VALUABLES;
import me.shadow2hel.minventory.data.managers.*;
import me.shadow2hel.minventory.model.EntityItemTracker;
import me.shadow2hel.minventory.model.PlayerTracker;
import me.shadow2hel.minventory.model.InventoryTracker;
import me.shadow2hel.minventory.pdc.PDCUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;
import org.bukkit.block.Furnace;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
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
    private BukkitTask wipeTask;

    public Wiper(IEntityManager mobManager, IPlayerInventoryManager playerInventoryManager, IPlayerManager playerManager, IWipeManager wipeManager, JavaPlugin main) {
        this.isWiping = false;
        this.mobManager = mobManager;
        this.playerInventoryManager = playerInventoryManager;
        this.playerManager = playerManager;
        this.wipeManager = wipeManager;
        this.main = main;
        wipeCheckOnCrash();
        wipeTimer();
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
                    wipeNotification("", Math.abs(currentTime.get(Calendar.MINUTE) - 60), "minutes", 10, 200, 10);
                } else if (currentTime.get(Calendar.MINUTE) < 59 && currentTime.get(Calendar.MINUTE) % 5 == 0 && currentTime.get(Calendar.SECOND) == 0) {
                    wipeNotification("", Math.abs(currentTime.get(Calendar.MINUTE) - 60), "minutes", 10, 200, 10);
                } else if (currentTime.get(Calendar.MINUTE) == 59 && currentTime.get(Calendar.SECOND) == 0) {
                    wipeNotification("", Math.abs(currentTime.get(Calendar.MINUTE) - 60), "minute", 10, 200, 10);
                } else if (currentTime.get(Calendar.MINUTE) == 59 && currentTime.get(Calendar.SECOND) == 30) {
                    wipeNotification("", Math.abs(currentTime.get(Calendar.SECOND) - 60), "seconds", 10, 200, 10);
                } else if (currentTime.get(Calendar.MINUTE) == 59 && currentTime.get(Calendar.SECOND) >= 50) {
                    wipeNotification("" + (60 - currentTime.get(Calendar.SECOND)), (60 - currentTime.get(Calendar.SECOND)), "", 5, 20, 5);
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
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(ChatColor.BOLD + "" + ChatColor.DARK_GREEN + "Wiped!",
                    ChatColor.GOLD + "Spread your wealth",
                    5,
                    200,
                    5);
        });
        main.getServer().shutdown();
    }

    public boolean wipe() {
        wipeManager.createWipe(new Date());
        List<Chunk> loadedChunks = Bukkit.getWorlds()
                .stream()
                .flatMap(world -> Arrays.stream(world.getLoadedChunks()))
                .toList();
        for (Chunk loadedChunk : loadedChunks) {
            wipeEntities(loadedChunk);
            wipeContainers(loadedChunk);
        }
        wipeEnderchests();
        wipeEnderchestsOnline();
        upgradePrestiges();
        return true;
    }

    public void wipeEntities(Chunk chunk) {
        Arrays.stream(chunk.getEntities())
                .filter(entity -> PDCUtils.getNbt(main, entity, KEYS.LASTWIPED, PersistentDataType.LONG) != null)
                .forEach(entity -> {
                    Calendar currentTime = Calendar.getInstance();
                    Calendar lastWipe = Calendar.getInstance();
                    lastWipe.setTime(wipeManager.readLatestWipe());
                    Calendar lastInteracted = Calendar.getInstance();
                    Long millisLast = PDCUtils.getNbt(main, entity, KEYS.LASTWIPED, PersistentDataType.LONG);
                    if (millisLast != null) {
                        lastInteracted.setTimeInMillis(millisLast);
                    }
                    if (lastWipe.after(lastInteracted)) {
                        if (entity instanceof InventoryHolder inventoryHolder) {
                            PDCUtils.setNbt(main, entity, KEYS.LASTWIPED, PersistentDataType.LONG, currentTime.getTimeInMillis());
                            if (inventoryHolder instanceof ChestedHorse chestedHorse) {
                                ItemStack saddle = chestedHorse.getInventory().getSaddle();
                                chestedHorse.getInventory().clear();
                                chestedHorse.getInventory().setSaddle(saddle);
                            } else if (inventoryHolder instanceof Mob mob) {
                                if (mob.getEquipment() != null) {
                                    ItemStack[] armors = mob.getEquipment().getArmorContents();
                                    for (int i = 0; i < armors.length; i++) {
                                        if (VALUABLES.GetAllBlacklist().contains(armors[i].getType())) {
                                            armors[i] = null;
                                        }
                                    }
                                    mob.getEquipment().setArmorContents(armors);
                                    mob.getEquipment().getItemInMainHand();
                                    if (VALUABLES.GetArmorWeaponsBlacklist().contains(mob.getEquipment().getItemInMainHand().getType())) {
                                        mob.getEquipment().setItemInMainHand(null);
                                    }
                                }

                            } else {
                                inventoryHolder.getInventory().clear();
                            }
                        } else {
                            entity.remove();
                        }
                    }


                });
    }

    public void wipeContainers(Chunk chunk) {
        Arrays.stream(chunk.getTileEntities())
                .filter(container -> container instanceof PersistentDataHolder)
                .filter(container -> PDCUtils.getNbt(main, (PersistentDataHolder) container, KEYS.LASTWIPED, PersistentDataType.LONG) != null)
                .forEach(container -> {
                    Inventory inv = ((Container) container).getSnapshotInventory();
                    Calendar currentTime = Calendar.getInstance();
                    Calendar lastWipe = Calendar.getInstance();
                    lastWipe.setTime(wipeManager.readLatestWipe());
                    Calendar lastInteracted = Calendar.getInstance();
                    Long millisLast = PDCUtils.getNbt(main, ((PersistentDataHolder) container), KEYS.LASTWIPED, PersistentDataType.LONG);
                    if (millisLast != null) {
                        lastInteracted.setTimeInMillis(millisLast);
                    }
                    if (lastWipe.after(lastInteracted)) {
                        if (((Container) container) instanceof Furnace furnaceContainer) {
                            furnaceContainer.getSnapshotInventory().clear(0);
                        } else {
                            ((Container) container).getSnapshotInventory().clear();
                        }
                    }
                    PDCUtils.setNbt(main, ((PersistentDataHolder) container), KEYS.LASTWIPED, PersistentDataType.LONG, currentTime.getTimeInMillis());
                });
    }


    private void wipeEnderchests() {
        List<PlayerTracker> players = playerManager.readAllPlayers();
        for (PlayerTracker player : players) {
            player.setEnderChestWiped(false);
            PlayerTracker updatedPlayer = playerManager.updatePlayer(player);
        }
        main.getLogger().info("Enderchests are marked for wipe");
    }

    private void wipeEnderchestsOnline() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getEnderChest().clear();
        });
    }

    public void wipeEnderchest(Player player) {
        player.getEnderChest().clear();
        PlayerTracker dataPlayer = playerManager.readPlayer(player.getUniqueId().toString());
        dataPlayer.setEnderChestWiped(true);
        playerManager.updatePlayer(dataPlayer);
    }

    private void upgradePrestiges() {
        List<PlayerTracker> dataPlayers = playerManager.readAllPlayers();
        dataPlayers.forEach(dataPlayer -> {
            if (dataPlayer.isMarkedForPrestige()){
                dataPlayer.setPrestige(dataPlayer.getPrestige() + 1);
                playerManager.updatePlayer(dataPlayer);
            }
        });
    }

    public boolean isWiping() {
        return isWiping;
    }
}
