package be.ketsu.bingo.game;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.managers.GameManager;
import be.ketsu.bingo.game.phases.Phase;
import be.ketsu.bingo.game.phases.bases.EndPhase;
import be.ketsu.bingo.game.phases.bases.GamePhase;
import be.ketsu.bingo.game.phases.bases.LaunchingPhase;
import lombok.Data;
import lombok.Getter;

import java.util.*;

import static be.ketsu.bingo.utils.TimeUtils.MIN;

@Data
public class GameInstance {

    // Variables
    @Getter
    UUID id; // The ID of the instance
    @Getter
    List<BingoPlayer> players; // List of players for this instance
    BingoCard bingoCard; // The current card for this instance
    // Managers
    private final GameManager gameManager;

    // Game settings
    private boolean isReady = false;
    private GameState state = GameState.WAITING;
    private Deque<Phase> phases = new ArrayDeque<>();
    private Phase currentPhase;


    public GameInstance() {
        // Set up the instance
        id = UUID.randomUUID();
        players = new ArrayList<>();
        bingoCard = BingoBukkit.getInstance().getBingoManager().generateBingoCard();

        gameManager = new GameManager(this);

        phases.add(new LaunchingPhase(this, 5));
        phases.add(new GamePhase(this, 5 * MIN));
        phases.add(new EndPhase(this));
    }

}
