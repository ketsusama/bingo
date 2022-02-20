package be.ketsu.bingo.commands;

import be.ketsu.bingo.BingoBukkit;
import be.ketsu.bingo.game.BingoPlayer;
import be.ketsu.bingo.gui.types.BingoInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BingoCommand implements CommandExecutor {

    public static boolean argLength0(Player player) {
        BingoInventory.INVENTORY.open(player);
        return true;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            boolean ret = false;
            if (player.hasPermission(BingoBukkit.getInstance().getSettings().getGameGlobalPermission())) {
                if (args.length == 0) {
                    ret = argLength0(player);
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
