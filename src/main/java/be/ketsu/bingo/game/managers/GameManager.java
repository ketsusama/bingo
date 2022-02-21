package be.ketsu.bingo.game.managers;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.game.GameState;
import be.ketsu.bingo.game.tasks.CheckStartTask;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.val;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameManager {

    // Instance
    private GameInstance gameInstance;
    private BukkitTask currentTask;

    public GameManager(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
        // Launch check start task
        startGame();
    }

    /***
     * Start a game
     * - Define the party as ready
     * - Run the check task
     */
    public void startGame() {
        BingoBukkit.getInstance().getExecutionManager().getTasks().put(gameInstance.getId() + "-checkstart", new CheckStartTask(gameInstance).runTaskTimer(BingoBukkit.getInstance(), 0L, 20L * 15));
    }


    /***
     * Check if the game can start
     */
    @SneakyThrows
    public void checkStart() {
        if (this.shouldStart()) {
            // Cancel check task
            BingoBukkit.getInstance().getExecutionManager().cancel(gameInstance.getId() + "-checkstart");
            // Launch the next phase
            this.nextPhase();
        } else {
            // Warn players that there are not enough players present
            BingoBukkit.getInstance().getInstancesManager().getCurrentGameInstance().getPlayers().forEach(bingoPlayer ->
                bingoPlayer.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getNoEnoughPlayer().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix())));
        }
    }

    /***
     * Returns if the game is ready to start
     */
    public boolean shouldStart() {
        return gameInstance.getState() == GameState.WAITING
            && gameInstance.getPlayers().size() >= BingoBukkit.getInstance().getSettings().getPlayersRequiredToStart() || gameInstance.isReady();
    }

    /***
     * Check the state of the game
     */
    public boolean isRunning() {
        return gameInstance.getState() != GameState.WAITING;
    }

    /***
     * Cancel the current game and
     * reset the system
     */
    public void cancelGame() {
        // Stop the current phase if not already done
        this.cancelPhase();
        // On stopper toutes les task
        //BingoBukkit.getInstance().getExecutionManager().cancelAllTasks();
    }

    /***
     * Stop the game in progress
     */
    public void endGame() {
        // Define the end state of the game
        gameInstance.setState(gameInstance.getCurrentPhase().getState());
        // Cut the current phase
        this.cancelPhase();
        // Temps list players
        List<BingoPlayer> bingoPlayers = new ArrayList<>(gameInstance.getPlayers());
        // Delete this game instance
        BingoBukkit.getInstance().getInstancesManager().getGameInstances().remove(getGameInstance());
        // Put players to current game
        bingoPlayers.forEach(bingoPlayer -> BingoBukkit.getInstance().getBingoManager().joinBingo(bingoPlayer.getPlayer()));

    }

    // PHASE SYSTEM

    /***
     * Start a new phase
     * @param runnable - The task bukkit
     */
    public void startPhase(BukkitRunnable runnable) {
        if (getCurrentTask() != null) {
            getCurrentTask().cancel();
        }
        setCurrentTask(runnable.runTaskTimerAsynchronously(BingoBukkit.getInstance(), 0, 20));
    }

    /***
     * Stop the current phase
     */
    public void cancelPhase() {
        BingoBukkit.getInstance().getLogger().info("Phase cancel : " + gameInstance.getCurrentPhase().getName());
        if (!this.getCurrentTask().isCancelled()) {
            System.out.println("Cancel phase task " + getCurrentTask().getTaskId());
            getCurrentTask().cancel();
        }
    }

    /***
     * Start the next phase
     */
    @SneakyThrows
    public void nextPhase() {
        val gameInstance = this.gameInstance;
        // Get & Poll la prochaine phase
        gameInstance.setCurrentPhase(gameInstance.getPhases().poll());
        BingoBukkit.getInstance().getLogger().info("Phase en cours : " + gameInstance.getCurrentPhase().getName());
        // Définir le status de la prochaine phase
        gameInstance.setState(gameInstance.getCurrentPhase().getState());
        // Lancer la Task de la prochaine phase
        this.startPhase(gameInstance.getCurrentPhase().runnable());
    }
}
