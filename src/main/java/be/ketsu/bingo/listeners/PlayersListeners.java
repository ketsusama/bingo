package be.ketsu.bingo.listeners;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.utils.LocationsUtils;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Optional;

public class PlayersListeners implements Listener {

    @EventHandler
    private void OnPlayerJoin(PlayerJoinEvent event) {
        // Remove base message
        event.setJoinMessage("");

        // Join the current bingo
        BingoBukkit.getInstance().getBingoManager().joinBingo(event.getPlayer());
    }

    @EventHandler
    private void OnPlayerLeave(PlayerQuitEvent event) {
        // Remove base message
        event.setQuitMessage("");

        // Get the bingo player
        BingoPlayer bingoPlayer = BingoBukkit.getInstance().getInstancesManager().findBingoPlayerInGameInstance(event.getPlayer());
        // Get the instance of bingo player
        Optional<GameInstance> gameInstance = BingoBukkit.getInstance().getInstancesManager().findPlayerGameInstance(bingoPlayer);
        // Remove bingo player from the instance
        gameInstance.get().getPlayers().remove(bingoPlayer);
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        // Remove base message
        event.setDeathMessage("");

        //Teleport player on safe locations
        event.getEntity().teleport(LocationsUtils.getSafeLocation(new Location(BingoBukkit.getInstance().getServer().getWorld("world"), 0, 0, 0)));

        // Clear the player inventory
        event.getEntity().getInventory().clear();

    }
}
