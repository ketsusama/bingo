package be.ketsu.bingo.game.phases.bases;


import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.game.GameState;
import be.ketsu.bingo.game.phases.Phase;
import be.ketsu.bingo.utils.TimeUtils;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class LaunchingPhase extends Phase {

    public LaunchingPhase(GameInstance gameInstance, int defaultTime) {
        this.name = "Launch state";
        this.unit = TimeUnit.SECONDS;
        this.state = GameState.LAUNCHING;
        this.defaultTime = defaultTime;
        this.time = defaultTime;
        this.gameInstance = gameInstance;
    }

    @Override
    public BukkitRunnable runnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                // Start the clock (Title + Level)
                // TODO Check NMS for title
                /*for (final BingoPlayer bingoPlayer : getGameInstance().getPlayers()) {
                    bingoPlayer.getPlayer().setLevel(time);
                    if (time == 5) {
                        //new Title(BingoBukkit.getInstance().getMessages().getGame().getStartInTitle(), BingoBukkit.getInstance().getMessages().getGame().getStartInSubTitle().replace("%time%", String.valueOf(time)), 20, 10, 20).send(player);
                        bingoPlayer.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getStartInTitle() + BingoBukkit.getInstance().getMessages().getGame().getStartInSubTitle().replace("%time%", String.valueOf(time)));
                    }
                }
                 */
                // Counting the time in the chat
                if (time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    getGameInstance().getPlayers().forEach(bingoPlayer -> bingoPlayer.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getGameBeginningIn().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix()).replace("%time%", TimeUtils.getDurationString(time))));
                    getGameInstance().getPlayers().forEach(bingoPlayer -> bingoPlayer.getPlayer().playSound(bingoPlayer.getPlayer().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20.0f, 20.0f));
                }
                // Start the game
                if (time < 1) {
                    getGameInstance().getPlayers().forEach(bingoPlayer -> bingoPlayer.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getGameStarting().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix())));
                    getGameInstance().getPlayers().forEach(bingoPlayer -> bingoPlayer.getPlayer().playSound(bingoPlayer.getPlayer().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 20.0f, 20.0f));
                    getGameInstance().getGameManager().nextPhase();
                    return;
                }
                --time;
            }
        };
    }
}
