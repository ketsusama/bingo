package be.ketsu.bingo.game;

import org.bukkit.inventory.ItemStack;

public record BingoCard(ItemStack[] items) {

    /***
     * Represents a bingo card
     * @param items
     */
    public BingoCard(ItemStack[] items) {
        this.items = items;
    }
}
