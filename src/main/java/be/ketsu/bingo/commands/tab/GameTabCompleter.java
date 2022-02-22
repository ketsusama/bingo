package be.ketsu.bingo.commands.tab;

import be.ketsu.bingo.BingoBukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return new ArrayList<>(Arrays.asList("start", "stop"));
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("stop")) {
                List<String> list = new ArrayList<>();
                BingoBukkit.getInstance().getInstancesManager().getGameInstances().forEach(gameInstance -> list.add(gameInstance.getId().toString()));
                return list;
            }
        }

        return null;
    }
}
