package be.ketsu.bingo.gui.types;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.gui.ClickableItem;
import be.ketsu.bingo.gui.SmartInventory;
import be.ketsu.bingo.gui.content.InventoryContents;
import be.ketsu.bingo.gui.content.InventoryProvider;
import be.ketsu.bingo.gui.content.SlotPos;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BingoInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
        .id("bingocard")
        .provider(new BingoInventory())
        .size(5, 9)
        .title(ChatColor.YELLOW + "My Bingo card")
        .build();

    Integer maxRows = 5;
    Integer maxColumn = 5;

    @Override
    public void init(Player player, InventoryContents contents) {
        // Find the bingo player
        BingoPlayer bingoPlayer = BingoBukkit.getInstance().getInstancesManager().findBingoPlayerInGameInstance(player);
        // Find the bingo player cards
        Map<Material, Boolean> materials = bingoPlayer.getHasMaterial();

        // Set the card items in inventory
        AtomicInteger count = new AtomicInteger();
        for (int row = 0; row < maxRows; row++)
            for (int column = 2; column < maxColumn + 2; column++) {
                Material material = (Material) materials.keySet().toArray()[count.getAndIncrement()];
                contents.set(SlotPos.of(row, column), ClickableItem.empty(new ItemStack(material)));
            }
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}


