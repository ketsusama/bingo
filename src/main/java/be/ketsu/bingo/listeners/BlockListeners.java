package be.ketsu.bingo.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListeners implements Listener {

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        // Prevent player in waiting state to break block
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onBlockPlace(BlockPlaceEvent event) {
        // Prevent player in waiting state to place block
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            event.setCancelled(true);
        }
    }

}
