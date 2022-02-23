package be.ketsu.bingo.game;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/***
 *
 * The logic of this class has been taken from the following
 * project main page : https://github.com/Extremelyd1/minecraft-bingo
 *
 * Project ressources used:
 * https://github.com/Extremelyd1/minecraft-bingo/blob/master/src/main/java/com/extremelyd1/bingo/BingoCard.java
 * @author Extremelyd1
 *
 * Note :The class has been adapted to match with my system
 *
 */
@Data
public final class BingoCard {
    private final int boardSize = 5;
    private final ItemStack[][] bingoItems;
    private List<ItemStack> items;

    /***
     * Represents a bingo card
     * @param itemStacks - The items generated
     */
    public BingoCard(List<ItemStack> itemStacks) {
        this.items = itemStacks;
        if (itemStacks.size() < 25) {
            throw new IllegalArgumentException("The size of the given material list is less than 25");
        }

        List<ItemStack> itemLeft = new ArrayList<>(itemStacks);
        bingoItems = new ItemStack[boardSize][boardSize];

        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                ItemStack item = itemLeft.get(
                    new Random().nextInt(itemLeft.size())
                );
                this.bingoItems[y][x] = item;
                itemLeft.remove(item);
            }
        }

    }


    /**
     * Gets the number of lines (rows, columns or diagonals) that is completed on this bingo card for the given Team
     *
     * @param bingoPlayer -The bingo player to check for lines completed
     * @return The number of lines completed
     */
    public int getNumLinesComplete(BingoPlayer bingoPlayer) {
        int numLinesComplete = 0;

        for (int y = 0; y < boardSize; y++) {
            if (checkRow(bingoPlayer, y)) {
                numLinesComplete++;
            }
        }

        for (int x = 0; x < boardSize; x++) {
            if (checkColumn(bingoPlayer, x)) {
                numLinesComplete++;
            }
        }

        if (checkDiagonal(bingoPlayer, true)) {
            numLinesComplete++;
        }

        if (checkDiagonal(bingoPlayer, false)) {
            numLinesComplete++;
        }

        return numLinesComplete;
    }

    /**
     * Whether this bingo card is fully completed for the given team
     *
     * @param player The bingo player to check for
     * @return Whether this bingo is fully completed
     */
    public boolean isCardComplete(BingoPlayer player) {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                ItemStack bingoItem = bingoItems[y][x];
                if (!player.getHasMaterial().get(bingoItem.getType())) {
                    continue;
                }
                return false;
            }
        }

        return true;
    }

    /**
     * Check whether the row with index y is completed for the given team
     *
     * @param player The bingo player to check for
     * @param y      The index to check for
     * @return Whether the row is completed
     */
    private boolean checkRow(BingoPlayer player, int y) {
        for (int x = 0; x < boardSize; x++) {
            if (!player.getHasMaterial().get(bingoItems[y][x].getType())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check whether the column with index x is completed for the given team
     *
     * @param player The bingo player to check for
     * @param x      The index to check for
     * @return Whether the column is completed
     */
    private boolean checkColumn(BingoPlayer player, int x) {
        for (int y = 0; y < boardSize; y++) {
            if (!player.getHasMaterial().get(bingoItems[y][x].getType())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check whether a diagonal is completed for the given team
     *
     * @param player       The bingo player to check for
     * @param startTopLeft Whether to check the diagonal starting in the top left
     *                     or the diagonal starting in the top right
     * @return Whether the diagonal is completed
     */
    private boolean checkDiagonal(BingoPlayer player, boolean startTopLeft) {
        if (startTopLeft) {
            for (int i = 0; i < boardSize; i++) {
                if (!player.getHasMaterial().get(bingoItems[i][i].getType())) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < boardSize; i++) {
                if (!player.getHasMaterial().get(bingoItems[i][boardSize - 1 - i].getType())) {
                    return false;
                }
            }
        }

        return true;
    }
}
