package be.ketsu.bingo.configuration.files.parts.messages;

import lombok.Data;

@Data
public class GameConfigurationPart {

    /**
     * Generic game configuration
     */
    String noEnoughPlayer = "%prefix% §cThere is not enough player to start the game!";
    String startInTitle = "§eBeginning in";
    String startInSubTitle = "§6%time% §eseconds";
    String gameBeginningIn = "%prefix% §eThe game starts in %time%";
    String gameStarting = "%prefix% §eThe game begins! Good luck!";
    String remainingGames = "%prefix% §a%time% §fof remaining games !";
    String remainingTime = "§aTime's remaining";
    String gameCancel = "%prefix% §eThe game has been canceled";

    /**
     * Bingo game configuration
     */
    String playerFoundItems = "%player% found the %item% item!";
    String playerCompletedBingoLine = "%player% completed a bingo line ! (%numberCompleted%/%numberToComplete%)";
    String onePlayerWonTheGame = "%player% won the game!";
    String multiplePlayersWonTheGame = "%numbersOfPlayers% players won the games!";
}
