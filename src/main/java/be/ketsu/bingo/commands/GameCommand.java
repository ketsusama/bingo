package be.ketsu.bingo.commands;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.GameInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GameCommand implements CommandExecutor {


    public static boolean argLength0(Player player) {
        List<String> commandList = new ArrayList<>(BingoBukkit.getInstance().getMessages().getCommand().getGameInfoList());
        for (String s : commandList) {
            player.sendMessage(s);
        }
        return true;
    }

    public boolean argLength1(Player player, String args1) {
        args1 = args1.toLowerCase();
        switch (args1) {
            case "help":
            case "info":
                // View Order Aids
                argLength0(player);
                break;
            case "start":
                // Force the game instance to start
                BingoBukkit.getInstance().getInstancesManager().getCurrentGameInstance().setReady(true);
                // Force instance checking start
                BingoBukkit.getInstance().getInstancesManager().getCurrentGameInstance().getGameManager().checkStart();
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean argLength2(Player player, String args1, String args2) {
        args1 = args1.toLowerCase();
        if ("stop".equals(args1)) {
            // Get the instance
            GameInstance gameInstance = BingoBukkit.getInstance().getInstancesManager().getGameInstances().get(Integer.parseInt(args2));
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
                    player.sendMessage(BingoBukkit.getInstance().getMessages().getPrefix() + BingoBukkit.getInstance().getMessages().getCommand().getBadSyntaxe());
                }

                return ret;
            }

            sender.sendMessage(BingoBukkit.getInstance().getMessages().getPrefix() + BingoBukkit.getInstance().getMessages().getCommand().getMustBePlayer());
            return true;
        }
        return false;
    }
}
