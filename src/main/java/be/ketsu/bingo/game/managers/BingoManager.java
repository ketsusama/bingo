package be.ketsu.bingo.game.managers;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoCard;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.game.GameInstance;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.IntStream;

public class BingoManager {

    List<Material> materials;
    Integer totalItems = 25;

    public BingoManager() {
        // Cache all minecraft materials
        materials = new ArrayList<>(List.of(Material.values()));
        // Remove all ban materials from cache
        BingoBukkit.getInstance().getSettings().getBanItemStack().forEach(material -> materials.remove(Material.getMaterial(material)));
    }


    /***
     * Generate a new bingo card with X items
     * @return - A new BingoCard instance
     */
    public BingoCard generateBingoCard() {
        Collections.shuffle(materials); // Randomize list of materials
        Random rand = new Random();
        // The items list to put in bingo cards
        List<ItemStack> itemStacks = IntStream.range(0, totalItems).mapToObj(i -> new ItemStack(materials.get(i))).toList();
        return new BingoCard(itemStacks.toArray(ItemStack[]::new));
    }

    /***
     * Join the current bingo
     * @param player
     */
    public void joinBingo(Player player) {
        // Get the current game instance
        GameInstance currentGameInstance = BingoBukkit.getInstance().getInstancesManager().getCurrentGameInstance();
        // Create a new bingo player
        BingoPlayer bingoPlayer = new BingoPlayer(player);
        // Get the bingo card from the current instance
        ItemStack[] itemStacks = currentGameInstance.getBingoCard().items();

        // Convert the bingo card
        Map<Material, Boolean> tempsMap = new HashMap<>();
        for (ItemStack itemStack : itemStacks) tempsMap.put(itemStack.getType(), false);

        // Set the bingo card to the bingo player
        bingoPlayer.setHasMaterial(tempsMap);

        // Finally, set the bingo player to the current instance
        currentGameInstance.getPlayers().add(bingoPlayer);
    }
}
