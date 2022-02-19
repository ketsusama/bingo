package be.ketsu.bingo.game.tasks;

import be.ketsu.bingo.game.GameInstance;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckStartTask extends BukkitRunnable {

    GameInstance gameInstance;

    public CheckStartTask(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    /***
     * Task to check if the game is ready to start
     */

    @Override
    public void run() {
        if (gameInstance != null) {
            gameInstance.getGameManager().checkStart();
        }
    }
}
