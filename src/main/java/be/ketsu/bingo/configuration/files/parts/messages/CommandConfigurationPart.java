package be.ketsu.bingo.configuration.files.parts.messages;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class CommandConfigurationPart {

    /* Global Command Configuration */
    String mustBePlayer = "%prefix% §cYou must be a player to execute this command";
    String badSyntaxe = "%prefix% §cBad command syntax";
    String noPermission = "%prefix% §cYou don't have the permission";

    /* Game Command Configuration */
    List<String> gameInfoList = Arrays.asList("§7--- §6Game Command §7---", "§a/game start §b: force the launch of the current instance", "§a/game stop <instance> §b: stop the current proceeding");
    String youStopTheGame = "§cYou stopped the game";

    /*Bingo Command Configuration */
    String youAreNotInGame = "§cYou can only use this command during the game phase";

}
