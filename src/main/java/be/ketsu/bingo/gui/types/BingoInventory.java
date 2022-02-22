package be.ketsu.bingo.gui.types;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.gui.ClickableItem;
import be.ketsu.bingo.gui.SmartInventory;
import be.ketsu.bingo.gui.content.InventoryContents;
import be.ketsu.bingo.gui.content.InventoryProvider;
import be.ketsu.bingo.gui.content.SlotPos;
import be.ketsu.bingo.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.atomic.AtomicInteger;

public class BingoInventory implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
        .id("bingocard")
        .provider(new BingoInventory())
        .size(5, 9)
        .title(ChatColor.YELLOW + "My Bingo card")
        .build();


    protected static final ClickableItem blackGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.BLACK_STAINED_GLASS_PANE)).setName("§a").build());
    protected static final ClickableItem grayGlasses = ClickableItem.empty(new ItemBuilder(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)).setName("§a").build());
    protected static final Integer maxRows = 5;
    protected static final Integer maxColumn = 5;

    @Override
    public void init(Player player, InventoryContents contents) {
        // Find the bingo player
        BingoPlayer bingoPlayer = BingoBukkit.getInstance().getInstancesManager().findBingoPlayerInGameInstance(player);
        // Find the player game instance
        GameInstance instance = BingoBukkit.getInstance().getInstancesManager().findPlayerGameInstance(bingoPlayer).get();
        // Find the bingo player cards
        ItemStack[][] itemStacks = instance.getBingoCard().getBingoItems();

        // Set the card items in inventory
        AtomicInteger count = new AtomicInteger();
        for (int row = 0; row < maxRows; row++)
            for (int column = 2; column < maxColumn + 2; column++) {
                Material material = itemStacks[row][column - 2].getType();
                contents.set(SlotPos.of(row, column), ClickableItem.empty(bingoPlayer.getHasMaterial().get(material) ? new ItemBuilder(material).addGLow().build() : new ItemBuilder(material).build()));
            }

        // Fill some glasses for a little design :o
        // Left side
        contents.fillColumn(0, blackGlasses);
        contents.fillColumn(1, grayGlasses);
        // Right side
        contents.fillColumn(7, grayGlasses);
        contents.fillColumn(8, blackGlasses);
    }

    @Override
    public void update(Player player, InventoryContents contents) {

    }

}


