package me.shadow2hel.minventory.pdc;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.lang.constant.Constable;

public final class PDCUtils {
    private static PersistentDataType persistentDataType;

    @Nullable
    public static <T extends Constable> T getNbt(JavaPlugin main, PersistentDataHolder inventoryHolder, String strKey, PersistentDataType<T,T> persistentDataType) {
        NamespacedKey key = new NamespacedKey(main, strKey);
        if (inventoryHolder instanceof Container container) {
            PersistentDataContainer pdc = container.getPersistentDataContainer();
            if (pdc.has(key, persistentDataType)) {
                return pdc.get(key, persistentDataType);
            }
        }
        if (inventoryHolder instanceof Entity entity) {
            PersistentDataContainer pdc = entity.getPersistentDataContainer();
            if (pdc.has(key, persistentDataType)) {
                return pdc.get(key, persistentDataType);
            }
        }

        return null;
    }

    public static <T extends Constable> boolean setNbt(JavaPlugin main, PersistentDataHolder inventoryHolder, String strKey, PersistentDataType<T,T> persistentDataType, T primitive ) {
        NamespacedKey key = new NamespacedKey(main, strKey);
        if (inventoryHolder instanceof Container container) {
            PersistentDataContainer pdc = container.getPersistentDataContainer();
            pdc.set(key, persistentDataType, primitive);
            return container.update();
        }
        if (inventoryHolder instanceof Entity entity) {
            PersistentDataContainer pdc = entity.getPersistentDataContainer();
            pdc.set(key, persistentDataType, primitive);
            T value = getNbt(main, inventoryHolder, strKey, persistentDataType);
            return true;
        }

        return false;
    }
}
