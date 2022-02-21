package be.ketsu.bingo.listeners;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class DamagesListeners implements Listener {

    public static List<Player> invisible = new ArrayList<>();

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            // Cancel damage for invisible entity
            if (invisible.contains(player)) {
                event.setCancelled(true);
            } else {
                BingoBukkit.getInstance().getInstancesManager().getCurrentGameInstance().getPlayers().forEach(bingoPlayer -> {
                    if (event.getEntity().getUniqueId().equals(bingoPlayer.getPlayer().getUniqueId())) {
                        if (BingoBukkit.getInstance().getInstancesManager().getCurrentGameInstance().getState() == GameState.WAITING) {
                            event.setCancelled(true);
                        }
                    }
                });
            }
        }
    }


    @EventHandler
    private void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            // Prevent player from damage entity
            if (((Player) event.getDamager()).getGameMode() == GameMode.CREATIVE) {
                event.setCancelled(true);
            }
        }
    }
}
