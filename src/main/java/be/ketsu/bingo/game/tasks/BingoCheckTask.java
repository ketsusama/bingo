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
                    System.out.println(1);
                    if (bingoPlayer.getHasMaterial().containsKey(itemStack.getType()) && !bingoPlayer.getHasMaterial().get(itemStack.getType())) {
                        // Replace the current state of items in the map.
                        System.out.println("before " + bingoPlayer.getHasMaterial().get(itemStack.getType()));
                        bingoPlayer.getHasMaterial().replace(itemStack.getType(), true);
                        System.out.println("after" + bingoPlayer.getHasMaterial().get(itemStack.getType()));

                        // Remove items from the player inventory
                        System.out.println(3);
                        player.getInventory().remove(itemStack);
                        // Send a message to all player's of the instance that the player find an itemstack
                        System.out.println(4);
                        gameInstance.getPlayers().forEach(playerToBroadCast -> playerToBroadCast.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getPlayerFoundItems().replace("%player%", player.getName()).replace("%item%", itemStack.getType().name())));
                        int lineNumber = gameInstance.getBingoCard().getNumLinesComplete(bingoPlayer);
                        System.out.println(5);
                        BingoBukkit.getInstance().getLogger().info("Line number " + lineNumber);
                        if (lineNumber > 0 && lineNumber > bingoPlayer.getFoundLines()) {
                            System.out.println(6);
                            bingoPlayer.setFoundLines(lineNumber);
                            gameInstance.getPlayers().forEach(playerToBroadCast -> playerToBroadCast.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getPlayerCompletedBingoLine().replace("%player%", player.getName()).replace("%numberCompleted%", String.valueOf(bingoPlayer.getFoundLines())).replace("%numberToComplete%", String.valueOf(BingoBukkit.getInstance().getSettings().getLineToComplete()))));
                        }
                        if (lineNumber >= BingoBukkit.getInstance().getSettings().getLineToComplete()) {
                            System.out.println(7);
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
