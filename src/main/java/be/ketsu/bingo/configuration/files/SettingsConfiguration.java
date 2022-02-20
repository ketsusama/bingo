package be.ketsu.bingo.configuration.files;

import be.ketsu.bingo.configuration.annotation.Configuration;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
@Configuration("config")
public class SettingsConfiguration {

    /*
    General settings
     */
    Integer playersRequiredToStart = 2;
    Integer sizeOfBeamArea = 50;


    /*
    Games settings
     */
    String gameGlobalPermission = "bingo.*";
    Integer lineToComplete = 12;
    List<String> banItemStack = Arrays.asList("GLASS");

}
