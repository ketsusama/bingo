package be.ketsu.bingo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class DamagesListeners implements Listener {

    public static List<Player> invisible = new ArrayList<>();

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (invisible.contains(player)) {
                event.setDamage(0.0);
            }
        }
    }
}
