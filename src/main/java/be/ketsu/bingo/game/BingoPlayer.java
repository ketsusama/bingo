package be.ketsu.bingo.game;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Data
public class BingoPlayer {

    private Player player;
    private Map<Material, Boolean> hasMaterial; // Lists the items the player found
    private Integer foundLines; // The lines the player found

    public BingoPlayer(Player player) {
        this.player = player;
        hasMaterial = new HashMap<>();
        foundLines = 0;
    }
}
