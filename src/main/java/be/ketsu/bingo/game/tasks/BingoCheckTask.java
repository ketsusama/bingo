package be.ketsu.bingo.game.tasks;

import be.ketsu.bingo.game.GameInstance;
import org.bukkit.scheduler.BukkitRunnable;

public class BingoCheckTask extends BukkitRunnable {

    GameInstance gameInstance;

    public BingoCheckTask(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    /***
     * Task to check if player a realized a bingo line
     */

    @Override
    public void run() {
        if (gameInstance != null) {
            gameInstance.getGameManager().checkStart();
        }
    }
}
