package be.ketsu.bingo.game.phases.bases;


import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.game.GameState;
import be.ketsu.bingo.game.phases.Phase;
import be.ketsu.bingo.utils.NMSPacket;
import be.ketsu.bingo.utils.TimeUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static be.ketsu.bingo.utils.TimeUtils.HOUR;
import static be.ketsu.bingo.utils.TimeUtils.MIN;
import static org.bukkit.Bukkit.getOnlinePlayers;
import static org.bukkit.GameMode.CREATIVE;

public class GamePhase extends Phase {

    private int[] titleTime;
    private int[] bcTime;
    @Getter
    @Setter
    private GameInstance game;

    public GamePhase(int defaultTime) {
        this.name = "The games state";
        this.unit = TimeUnit.MINUTES;
        this.state = GameState.IN_GAME;
        this.defaultTime = defaultTime;
        this.time = defaultTime;
    }

    @Override
    public BukkitRunnable runnable() {
        if (Objects.isNull(this.bcTime))
            this.bcTime = addTimeEach(new int[]{time - 10 * MIN, time - 30 * MIN, time / 2}, HOUR);
        if (Objects.isNull(this.titleTime))
            this.titleTime = new int[]{time - 1, time - 2, time - 3, time - 4, time - 5, time - 10, time - 20, time - 30, time - MIN};
        return new BukkitRunnable() {
            @Override
            public void run() {
                if (currentTime == 0) {
                    // Define survival game mode for all players in the instance
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            getGameInstance().getPlayers().forEach(p -> {
                                p.setGameMode(CREATIVE);
                            });
                        }
                    }.runTask(BingoBukkit.getInstance());
                }
                // Log the game minutes in console
                if (currentTime % 60 == 0)
                    BingoBukkit.getInstance().getLogger().info(" " + currentTime / 60 + " minutes de jeux");

                // All the X times send a broadcast for the remaining game time
                Arrays.stream(bcTime).filter(i -> i == currentTime).forEach(i -> BingoBukkit.getInstance().getServer().getOnlinePlayers().forEach(player -> player.sendMessage(BingoBukkit.getInstance().getMessages().getGame().getRemainingGames().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix()).replace("%time%", TimeUtils.getDurationString(time - currentTime)))));

                // Every X time sent a title for the last remaining minute
                Arrays.stream(titleTime).filter(i -> i == currentTime).forEach(i -> getOnlinePlayers().forEach(p -> {
                    new NMSPacket.Title(BingoBukkit.getInstance().getMessages().getGame().getRemainingTime(), TimeUtils.getDurationString(time - currentTime), 20, 5, 20).send(p);
                }));

                // Proceed to next step if time is up
                if (currentTime >= time) getGameInstance().getGameManager().nextPhase();

                ++currentTime;
            }
        };
    }

    protected int[] addTimeEach(int[] array, int seconds) {
        int[] newArray = new int[(int) (array.length + Math.floor(time / seconds) - 1)];

        IntStream.range(0, array.length).forEach(i -> newArray[i] = array[i]);

        for (int i = 1; i < time / seconds; i++)
            newArray[array.length + i - 1] = seconds * i;

        return newArray;
    }
}
