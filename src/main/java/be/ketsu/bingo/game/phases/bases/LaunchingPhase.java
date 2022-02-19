package be.ketsu.bingo.game.phases.bases;


import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.GameState;
import be.ketsu.bingo.game.phases.Phase;
import be.ketsu.bingo.utils.NMSPacket;
import be.ketsu.bingo.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class LaunchingPhase extends Phase {

    public LaunchingPhase(int defaultTime) {
        this.name = "Launch state";
        this.unit = TimeUnit.SECONDS;
        this.state = GameState.LAUNCHING;
        this.defaultTime = defaultTime;
        this.time = defaultTime;
    }

    @Override
    public BukkitRunnable runnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                // Check if the game is ready to start
                if (!getGameInstance().isReady()) {
                    return;
                }
                // Start the clock (Title + Level)
                for (final Player player : Bukkit.getOnlinePlayers()) {
                    player.setLevel(time);
                    if (time == 30 || time == 20 || time == 10 || time == 5) {
                        new NMSPacket.Title(BingoBukkit.getInstance().getMessages().getGame().getStartInTitle(), BingoBukkit.getInstance().getMessages().getGame().getStartInSubTitle().replace("%time%", String.valueOf(time)), 20, 10, 20).send(player);
                    }
                }
                // Counting the time in the chat
                if (time % 10 == 0 || time == 10 || time == 5 || time == 4 || time == 3 || time == 2 || time == 1) {
                    getGameInstance().getPlayers().forEach(player -> player.sendMessage(BingoBukkit.getInstance().getMessages().getGame().getGameBeginningIn().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix()).replace("%time%", TimeUtils.getDurationString(time))));
                    Bukkit.getOnlinePlayers().forEach(player2 -> player2.playSound(player2.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 20.0f, 20.0f));
                }
                // Start the game
                if (time < 1) {
                    getGameInstance().getPlayers().forEach(player -> player.sendMessage(BingoBukkit.getInstance().getMessages().getGame().getGameStarting().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix())));
                    Bukkit.getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 20.0f, 20.0f));
                    getGameInstance().getGameManager().nextPhase();
                    return;
                }
                --time;
            }
        };
    }
}
