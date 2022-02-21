package be.ketsu.bingo.game.phases.bases;


import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.game.GameState;
import be.ketsu.bingo.game.phases.Phase;
import be.ketsu.bingo.game.tasks.BingoCheckTask;
import be.ketsu.bingo.listeners.DamagesListeners;
import be.ketsu.bingo.utils.LocationsUtils;
import be.ketsu.bingo.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static be.ketsu.bingo.utils.TimeUtils.MIN;
import static org.bukkit.GameMode.SURVIVAL;

public class GamePhase extends Phase {

    private int[] titleTime;
    private int[] bcTime;
    @Getter
    @Setter
    private GameInstance game;
    private boolean invisibleSet = false;
    private boolean checkStarted = false;

    public GamePhase(GameInstance gameInstance, int defaultTime) {
        this.name = "The games state";
        this.unit = TimeUnit.MINUTES;
        this.state = GameState.IN_GAME;
        this.defaultTime = defaultTime;
        this.time = defaultTime;
        this.gameInstance = gameInstance;
    }

    @Override
    public BukkitRunnable runnable() {
        if (Objects.isNull(this.bcTime))
            this.bcTime = addTimeEach(new int[]{time - MIN, time - 2 * MIN, time - 3 * MIN, time - 4 * MIN, time - (4 * MIN + MIN / 2)}, MIN);
        if (Objects.isNull(this.titleTime))
            this.titleTime = new int[]{time - 1, time - 2, time - 3, time - 4, time - 5};
        return new BukkitRunnable() {
            @Override
            public void run() {

                // Launch check tasks
                if (!checkStarted) {
                    BingoBukkit.getInstance().getExecutionManager().getTasks().put(gameInstance.getId() + "-checkbingo", new BingoCheckTask(gameInstance).runTaskTimer(BingoBukkit.getInstance(), 0L, 20L * 2));
                    checkStarted = true;
                }

                // Set player invisible for 10 seconds
                if (!invisibleSet) {
                    getGameInstance().getPlayers().forEach(bingoPlayer -> DamagesListeners.invisible.add(bingoPlayer.getPlayer()));
                    invisibleSet = true;
                }

                // Remove invisibility from players
                if (currentTime >= 11) {
                    getGameInstance().getPlayers().forEach(bingoPlayer -> {
                        DamagesListeners.invisible.remove(bingoPlayer.getPlayer());
                    });
                }

                if (currentTime == 0) {
                    // Define survival game mode for all players in the instance
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            getGameInstance().getPlayers().forEach(bingoPlayer -> {
                                // Set game mode survival
                                bingoPlayer.getPlayer().setGameMode(SURVIVAL);
                                // Clean inventory
                                bingoPlayer.getPlayer().getInventory().clear();
                                // Teleport player around a circle pattern
                                bingoPlayer.getPlayer().teleport(LocationsUtils.getSafeLocation(new Location(BingoBukkit.getInstance().getServer().getWorld("world"), 0, 0, 0)));

                            });
                        }
                    }.runTask(BingoBukkit.getInstance());
                }
                // Log the game minutes in console
                if (currentTime % 60 == 0)
                    BingoBukkit.getInstance().getLogger().info(getGameInstance().getId() + " " + currentTime / 60 + " minutes de jeux");

                // All the X times send a broadcast for the remaining game time
                Arrays.stream(bcTime).filter(i -> i == currentTime).forEach(i -> getGameInstance().getPlayers().forEach(bingoPlayer -> bingoPlayer.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getRemainingGames().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix()).replace("%time%", TimeUtils.getDurationString(time - currentTime)))));

                // Every X time sent a title for the last remaining minute
                /*
                Arrays.stream(titleTime).filter(i -> i == currentTime).forEach(i -> getGameInstance().getPlayers().forEach(bingoPlayer -> {
                    // TODO Rework nms
                    //new Title(BingoBukkit.getInstance().getMessages().getGame().getRemainingTime(), TimeUtils.getDurationString(time - currentTime), 20, 5, 20).send(p);
                    bingoPlayer.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getRemainingTime(), TimeUtils.getDurationString(time - currentTime));
                }));
                 */

                // Proceed to next step if time is up
                if (currentTime >= time) getGameInstance().getGameManager().nextPhase();

                ++currentTime;
            }
        }

            ;
    }

    protected int[] addTimeEach(int[] array, int seconds) {
        int[] newArray = new int[(int) (array.length + Math.floor(time / seconds) - 1)];

        IntStream.range(0, array.length).forEach(i -> newArray[i] = array[i]);

        for (int i = 1; i < time / seconds; i++)
            newArray[array.length + i - 1] = seconds * i;

        return newArray;
    }
}
