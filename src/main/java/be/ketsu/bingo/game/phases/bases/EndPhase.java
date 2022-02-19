package be.ketsu.bingo.game.phases.bases;

import be.ketsu.bingo.game.GameState;
import be.ketsu.bingo.game.phases.Phase;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class EndPhase extends Phase {

    public EndPhase() {
        this.name = "end of the game";
        this.unit = TimeUnit.MINUTES;
        this.state = GameState.ENDING;
        this.time = defaultTime;
    }

    @Override
    public BukkitRunnable runnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                // finish the game
                getGameInstance().getGameManager().endGame();
            }
        };
    }
}
