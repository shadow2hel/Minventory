package me.shadow2hel.minventory.listeners;

import me.shadow2hel.minventory.Wiper;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

public class ChunkListener implements Listener {
    Wiper wiper;

    public ChunkListener(Wiper wiper) {
        this.wiper = wiper;
    }

    @EventHandler
    private void onChunkLoad(ChunkLoadEvent loadEvent) {
        wiper.wipeEntities(loadEvent.getChunk());
        wiper.wipeContainers(loadEvent.getChunk());
    }
}
