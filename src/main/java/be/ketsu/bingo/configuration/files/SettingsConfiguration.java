package be.ketsu.bingo.configuration.files;

import be.ketsu.bingo.configuration.annotation.Configuration;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
    List<ItemStack> banItemStack = List.of(new ItemStack(Material.GLASS));

    /*
    Menu settings
     */
    Integer gui_row = 5;
    Integer gui_column = 5;
}
