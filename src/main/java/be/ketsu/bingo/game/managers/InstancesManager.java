package be.ketsu.bingo.game.managers;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.game.GameInstance;
import lombok.Data;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class InstancesManager {

    private List<GameInstance> gameInstances; // List of instances

    private GameInstance currentGameInstance; // Current instance


    public InstancesManager() {
        // Setup instance
        gameInstances = new ArrayList<>();
        // Add first game instance
        createGameInstance();
    }

    /***
     * Create a Party Instance
     */
    private void createGameInstance() {
        currentGameInstance = new GameInstance();
        // Add the instance to the list
        gameInstances.add(currentGameInstance);
    }

    /***
     * Delete a party instance
     * @param gameInstance - The game instance to delete
     */
    public void removeGameInstance(GameInstance gameInstance) {
        gameInstances.remove(gameInstance);
    }

    /***
     * Proceed to the next party instance
     */
    private void nextGameInstance() {
        if (gameInstances != null && gameInstances.contains(currentGameInstance)) {
            createGameInstance();
        } else {
            BingoBukkit.getInstance().getLogger().severe("The game instance " + currentGameInstance.getId() + "was already added");
        }
    }

    /***
     * Find the game instance of a player
     * @param bingoPlayer - The bingo player in the game instance
     * @return - Returns the game instance of player
     */
    public Optional<GameInstance> findPlayerGameInstance(@NonNull BingoPlayer bingoPlayer) {
        return Optional.of(gameInstances.stream().filter(gameInstance -> gameInstance.getPlayers().contains(bingoPlayer)).findFirst().get());
    }

    /***
     * Find the player in game instances
     * @param player - The player to find in game instances
     * @return - The bingo player
     */
    public BingoPlayer findBingoPlayerInGameInstance(@NonNull Player player) {
        BingoPlayer tempsBingoPlayer = null;
        for (GameInstance gameInstance : gameInstances) {
            for (BingoPlayer bingoPlayer : gameInstance.getPlayers()) {
                if (bingoPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                    tempsBingoPlayer = bingoPlayer;
                }
            }
        }
        return tempsBingoPlayer;
    }
}
