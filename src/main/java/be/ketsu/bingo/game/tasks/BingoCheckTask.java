package be.ketsu.bingo.game.tasks;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.GameInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class BingoCheckTask extends BukkitRunnable {

    GameInstance gameInstance;

    public BingoCheckTask(GameInstance gameInstance) {
        this.gameInstance = gameInstance;
    }

    /***
     * Task to check if player a realized a bingo line
     */

    @Override
    public void run() {
        gameInstance.getPlayers().forEach(bingoPlayer -> {
            Player player = bingoPlayer.getPlayer();
            List<ItemStack> itemStacks = gameInstance.getBingoCard().getItems();
            for (ItemStack itemStack : itemStacks) {
                if (player.getInventory().contains(itemStack)) {
                    if (bingoPlayer.getHasMaterial().containsKey(itemStack.getType()) && !bingoPlayer.getHasMaterial().get(itemStack.getType())) {
                        // Replace the current state of items in the map.
                        bingoPlayer.getHasMaterial().replace(itemStack.getType(), true);

                        // Remove items from the player inventory
                        player.getInventory().remove(itemStack);
                        // Send a message to all player's of the instance that the player find an itemstack
                        gameInstance.getPlayers().forEach(playerToBroadCast -> playerToBroadCast.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getPlayerFoundItems().replace("%player%", player.getName()).replace("%item%", itemStack.getType().name())));
                        int lineNumber = gameInstance.getBingoCard().getNumLinesComplete(bingoPlayer);
                        if (lineNumber > 0 && lineNumber > bingoPlayer.getFoundLines()) {
                            bingoPlayer.setFoundLines(lineNumber);
                            gameInstance.getPlayers().forEach(playerToBroadCast -> playerToBroadCast.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getPlayerCompletedBingoLine().replace("%player%", player.getName()).replace("%numberCompleted%", String.valueOf(bingoPlayer.getFoundLines())).replace("%numberToComplete%", String.valueOf(BingoBukkit.getInstance().getSettings().getLineToComplete()))));
                        }
                        if (lineNumber >= BingoBukkit.getInstance().getSettings().getLineToComplete()) {
                            gameInstance.getPlayers().forEach(player1 -> player1.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getOnePlayerWonTheGame().replace("%player", bingoPlayer.getPlayer().getName())));
                            gameInstance.getGameManager().endGame();
                            break;
                        }
                    }
                }
            }
        });
    }
}
