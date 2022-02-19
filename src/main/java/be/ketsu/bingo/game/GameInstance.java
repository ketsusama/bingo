package be.ketsu.bingo.game;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.managers.GameManager;
import be.ketsu.bingo.game.phases.Phase;
import lombok.Data;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.UUID;

@Data
public class GameInstance {

    // Managers
    private final GameManager gameManager;
    // Variables
    @Getter
    UUID id; // The ID of the instance
    @Getter
    List<Player> players; // List of players for this instance
    BingoCard bingoCard; // The current card for this instance

    // Game settings
    private boolean isReady = false;
    private GameState state = GameState.WAITING;
    private Deque<Phase> instancePhases;
    private Phase currentPhase;


    public GameInstance() {
        // Set up the instance
        id = UUID.randomUUID();
        players = new ArrayList<>();
        bingoCard = BingoBukkit.getInstance().getBingoManager().generateBingoCard();
        gameManager = new GameManager(this);
    }

}
