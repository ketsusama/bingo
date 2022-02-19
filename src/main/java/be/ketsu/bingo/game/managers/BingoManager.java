package be.ketsu.bingo.game.managers;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoCard;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class BingoManager {

    List<Material> materials;
    Integer totalItems = 0;

    public BingoManager() {
        // Cache all minecraft materials
        materials = new ArrayList<>(List.of(Material.values()));
        // Remove all ban materials from cache
        BingoBukkit.getInstance().getSettings().getBanItemStack().forEach(itemStack -> materials.remove(itemStack.getType()));
        // Define total items in menus
        totalItems = BingoBukkit.getInstance().getSettings().getGui_column() * BingoBukkit.getInstance().getSettings().getGui_row();
    }


    /***
     * Generate a new bingo card with X items
     * @return - A new BingoCard instance
     */
    public BingoCard generateBingoCard() {
        Collections.shuffle(materials); // Randomize list of materials
        Random rand = new Random();
        // The items list to put in bingo cards
        List<ItemStack> itemStacks = IntStream.range(0, totalItems).map(i -> rand.nextInt(materials.size())).mapToObj(index -> new ItemStack(materials.get(index))).toList();
        return new BingoCard(itemStacks.toArray(new ItemStack[totalItems]));
    }
}
