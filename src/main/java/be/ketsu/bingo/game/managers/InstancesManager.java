package be.ketsu.bingo.game.managers;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.GameInstance;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class InstancesManager {

    private List<GameInstance> gameInstances;

    private GameInstance currentGameInstance;

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
        if (gameInstances != null && !gameInstances.contains(currentGameInstance)) {
            gameInstances.add(currentGameInstance);
            createGameInstance();
        } else {
            BingoBukkit.getInstance().getLogger().severe("The game instance " + currentGameInstance.getId() + "was already added");
        }
    }

    /***
     * Find a player in a game instance
     * @param player - The player in the game instance
     * @return - Returns the game instance of player
     */
    private Optional<GameInstance> findPlayerInstance(Player player) {
        return Optional.of(gameInstances.stream().filter(gameInstance -> gameInstance.getPlayers().contains(player)).findFirst().get());
    }
}
