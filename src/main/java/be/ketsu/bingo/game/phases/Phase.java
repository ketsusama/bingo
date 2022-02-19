package be.ketsu.bingo.game.phases;

import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.game.GameState;
import lombok.Data;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

/***
 * Represents a phase/steps of a part
 */
@Data
public abstract class Phase {

    public int time; // Display time of a phase
    public int currentTime; // Current time of phase
    public int defaultTime; // Phase default time
    protected String name;
    protected TimeUnit unit;
    protected GameState state;
    protected BukkitRunnable runnable; // Bukkit runnable of the phase
    private GameInstance gameInstance;

    /**
     * Returns the name of a phase
     *
     * @return - The name
     */
    public final String getName() {
        return name;
    }

    /**
     * Returns the unit of time of the phase
     *
     * @return - L'unité de temps
     */
    public final TimeUnit getTimeUnit() {
        return unit;
    }

    /**
     * Returns the state of the phase
     *
     * @return - The party state
     */
    public final GameState getState() {
        return state;
    }

    /**
     * Bukkit runnable of the phase
     */
    public abstract BukkitRunnable runnable();
}
