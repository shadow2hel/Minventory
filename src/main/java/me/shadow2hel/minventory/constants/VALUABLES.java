package me.shadow2hel.minventory.constants;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class VALUABLES {

    public static List<Material> GetArmorWeaponsBlacklist() {
        final List<Material> armorBlacklist = new ArrayList<>();
        armorBlacklist.add(Material.DIAMOND_HELMET);
        armorBlacklist.add(Material.DIAMOND_CHESTPLATE);
        armorBlacklist.add(Material.DIAMOND_LEGGINGS);
        armorBlacklist.add(Material.DIAMOND_BOOTS);
        armorBlacklist.add(Material.NETHERITE_HELMET);
        armorBlacklist.add(Material.NETHERITE_CHESTPLATE);
        armorBlacklist.add(Material.NETHERITE_LEGGINGS);
        armorBlacklist.add(Material.NETHERITE_BOOTS);
        armorBlacklist.add(Material.DIAMOND_AXE);
        armorBlacklist.add(Material.DIAMOND_SWORD);
        armorBlacklist.add(Material.DIAMOND_PICKAXE);
        armorBlacklist.add(Material.DIAMOND_SHOVEL);
        armorBlacklist.add(Material.NETHERITE_AXE);
        armorBlacklist.add(Material.NETHERITE_SWORD);
        armorBlacklist.add(Material.NETHERITE_PICKAXE);
        armorBlacklist.add(Material.NETHERITE_SHOVEL);
        return armorBlacklist;
    }

    public static List<Material> GetItemsBlacklist() {
        final List<Material> itemBlacklist = new ArrayList<>();
        itemBlacklist.add(Material.IRON_BLOCK);
        itemBlacklist.add(Material.DIAMOND_BLOCK);
        itemBlacklist.add(Material.EMERALD_BLOCK);
        itemBlacklist.add(Material.GOLD_BLOCK);
        itemBlacklist.add(Material.NETHERITE_BLOCK);
        itemBlacklist.add(Material.WITHER_SKELETON_SKULL);
        itemBlacklist.add(Material.BEACON);
        return itemBlacklist;
    }

    public static List<Material> GetAllBlacklist() {
        final List<Material> allBlacklist = new ArrayList<>();
        allBlacklist.addAll(GetItemsBlacklist());
        allBlacklist.addAll(GetArmorWeaponsBlacklist());
        return allBlacklist;
    }
}
