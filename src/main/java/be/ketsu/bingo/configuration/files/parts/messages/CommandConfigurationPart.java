package be.ketsu.bingo.configuration.files.parts.messages;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class CommandConfigurationPart {

    /* Global Command Configuration */
    String mustBePlayer = "%prefix% §cYou must be a player to execute this command";
    String badSyntaxe = "%prefix% §cBad command syntax";

    /* Game Command Configuration */
    List<String> gameInfoList = Arrays.asList("§a/game start §b: force the launch of the current instance", "§a/game stop <instance> §b: stop the current proceeding");

}
