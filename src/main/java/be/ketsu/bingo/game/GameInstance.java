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
    String id; // The identifier of the instance
    @Getter
    List<BingoPlayer> players; // List of players for this instance
    BingoCard bingoCard; // The current card for this instance
    // Game settings
    private boolean isReady = false; // Indicates if the game has been forced
    private GameState state = GameState.WAITING; // The default state
    private Deque<Phase> phases = new ArrayDeque<>(); // The list of phases
    private Phase currentPhase; // Current phase

    // Managers
    private final GameManager gameManager;

    public GameInstance() {
        // Set up the instance
        // Generate a human-readable instance identifiers
        id = UUID.randomUUID().toString().replace(" ", "").substring(0, 8);
        players = new ArrayList<>();
        bingoCard = BingoBukkit.getInstance().getBingoManager().generateBingoCard();

        // Setup managers
        gameManager = new GameManager(this);

        // Setup phases
        phases.add(new LaunchingPhase(this, 5));
        phases.add(new GamePhase(this, 5 * MIN));
        phases.add(new EndPhase(this));
    }

}
