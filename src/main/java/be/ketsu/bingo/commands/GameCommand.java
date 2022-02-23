package be.ketsu.bingo.commands;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.game.GameInstance;
import be.ketsu.bingo.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCommand implements CommandExecutor {

    /***
     * Command with no args
     * @param player - The sender of the command
     * @return
     */
    public static boolean argLength0(Player player) {
        List<String> commandList = new ArrayList<>(BingoBukkit.getInstance().getMessages().getCommand().getGameInfoList());
        for (String s : commandList) {
            player.sendMessage(s);
        }
        return true;
    }

    /***
     * Command with one args
     * @param player - The sender of the command
     * @param args1 - The first argument
     * @return
     */
    public boolean argLength1(Player player, String args1) {
        args1 = args1.toLowerCase();
        switch (args1) {
            case "help":
            case "info":
                // View Order Aids
                argLength0(player);
                break;
            case "start":
                // Get current game instance
                GameInstance currentGameInstance = BingoBukkit.getInstance().getInstancesManager().getCurrentGameInstance();
                // Force the game instance to start
                if (currentGameInstance.getState() == GameState.WAITING) {
                    currentGameInstance.setReady(true);
                    currentGameInstance.getGameManager().checkStart();
                }
                break;
            case "stop":
                // Get the sender from the game
                BingoPlayer bingoPlayer = BingoBukkit.getInstance().getInstancesManager().findBingoPlayerInGameInstance(player);
                // Get the instance from the sender and end the game
                GameInstance gameInstance = BingoBukkit.getInstance().getInstancesManager().findPlayerGameInstance(bingoPlayer).get();
                // Temps List players
                List<BingoPlayer> bingoPlayers = gameInstance.getPlayers();
                // Send message to the sender
                player.sendMessage(BingoBukkit.getInstance().getMessages().getCommand().getYouStopTheGame());
                // Send message to players
                bingoPlayers.forEach(bingoPlayer1 -> bingoPlayer1.getPlayer().sendMessage(BingoBukkit.getInstance().getMessages().getGame().getGameCancel()));
                // Stop the game
                gameInstance.getGameManager().endGame();
            default:
                return false;
        }
        return true;
    }

    /***
     * Command with two args
     * @param player - The sender of the command
     * @param args1 - The first argument
     * @param args2 - The second argument
     * @return
     */
    public boolean argLength2(Player player, String args1, String args2) {
        args1 = args1.toLowerCase();
        if ("stop".equals(args1)) {
            // Get the instance
            GameInstance gameInstance = BingoBukkit.getInstance().getInstancesManager().getGameInstances().stream()
                .filter(findInstance -> args2.equals(findInstance.getId())).findAny().orElse(null);
            // End the party
            gameInstance.getGameManager().endGame();
            // Remove the instance from the list
            BingoBukkit.getInstance().getInstancesManager().removeGameInstance(gameInstance);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean ret = false;
            if (player.hasPermission(BingoBukkit.getInstance().getSettings().getGameGlobalPermission())) {
                switch (args.length) {
                    case 1:
                        ret = argLength1(player, args[0]);
                        break;
                    case 2:
                        ret = argLength2(player, args[0], args[1]);
                        break;
                    default:
                        ret = argLength0(player);
                        break;
                }

                if (!ret) {
                    player.sendMessage(BingoBukkit.getInstance().getMessages().getCommand().getBadSyntaxe().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix()));
                }

                return ret;
            } else {

            }

            sender.sendMessage(BingoBukkit.getInstance().getMessages().getCommand().getMustBePlayer().replace("%prefix%", BingoBukkit.getInstance().getMessages().getPrefix()));
            return true;
        }
        return false;
    }
}
