package be.ketsu.bingo.gui.types;

import be.ketsu.bingo.gui.SmartInventory;
import be.ketsu.bingo.gui.content.InventoryContents;
import be.ketsu.bingo.gui.content.InventoryProvider;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BingoInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
        .id("machineffect")
        .provider(new BingoInventory())
        .size(3, 9)
        .title(ChatColor.YELLOW + "My Bingo card")
        .build();

    @Override
    public void init(Player player, InventoryContents contents) {

    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}


