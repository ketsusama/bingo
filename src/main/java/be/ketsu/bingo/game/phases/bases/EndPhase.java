package be.ketsu.bingo.game.phases.bases;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.game.GameState;
import be.ketsu.bingo.game.phases.Phase;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class EndPhase extends Phase {

    Map<BingoPlayer, Integer> ladderOf = new HashMap<>();

    public EndPhase(GameInstance gameInstance) {
        this.name = "end of the game";
        this.unit = TimeUnit.MINUTES;
        this.state = GameState.ENDING;
        this.time = defaultTime;
        this.gameInstance = gameInstance;
    }

    @Override
    public BukkitRunnable runnable() {
        return new BukkitRunnable() {
            @Override
            public void run() {
                // No player was able to win the game in the allotted time so check which one has the most line to complete

                // Cancel BingoCheckTask
                BingoBukkit.getInstance().getExecutionManager().cancel(gameInstance.getId() + "-checkbingo");

                // Put all player in the map
                gameInstance.getPlayers().forEach(bingoPlayer -> {
                    Player player = bingoPlayer.getPlayer();
                    ladderOf.put(bingoPlayer, gameInstance.getBingoCard().getNumLinesComplete(bingoPlayer));
                });
                Map<BingoPlayer, Integer> tempsLadderOf = new HashMap<>();
                // Sort all by number of lines
                tempsLadderOf = ladderOf.entrySet().stream().sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                    ));

                Integer maxLine = tempsLadderOf.get(0);
                List<BingoPlayer> winners = new ArrayList<>();

                tempsLadderOf.forEach((player, integer) -> {
                    if (integer.equals(maxLine)) {
                        winners.add(player);
                    }
                });

                if (!(winners.size() == 1)) {
                    gameInstance.getPlayers().forEach(player -> player.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getMultiplePlayersWonTheGame().replace("%numbersOfPlayers%", String.valueOf(winners.size() + 1))));
                } else {
                    gameInstance.getPlayers().forEach(player -> player.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getOnePlayerWonTheGame().replace("%player%", winners.get(0).getPlayer().getName())));

                }
                // finish the game
                getGameInstance().getGameManager().endGame();
            }
        };
    }
}
