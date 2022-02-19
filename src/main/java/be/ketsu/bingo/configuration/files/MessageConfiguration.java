package be.ketsu.bingo.configuration.files;

import be.ketsu.bingo.configuration.annotation.Configuration;
import be.ketsu.bingo.configuration.files.parts.messages.CommandConfigurationPart;
import be.ketsu.bingo.configuration.files.parts.messages.GameConfigurationPart;
import lombok.Data;

@Data
@Configuration("messages")
public final class MessageConfiguration {

    /* Global */
    String prefix = "[Bingo]";

    // Configuration part
    CommandConfigurationPart command = new CommandConfigurationPart();
    GameConfigurationPart game = new GameConfigurationPart();
}
