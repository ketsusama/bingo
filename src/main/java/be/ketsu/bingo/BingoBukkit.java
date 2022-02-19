package be.ketsu.bingo;

import be.ketsu.bingo.configuration.files.MessageConfiguration;
import be.ketsu.bingo.configuration.files.SettingsConfiguration;
import be.ketsu.bingo.game.managers.InstancesManager;
import be.ketsu.bingo.gui.InventoryManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static be.ketsu.bingo.configuration.Configurations.readOrCreateConfiguration;

public class BingoBukkit extends JavaPlugin {

    // Instance variables
    @Getter
    public static BingoBukkit instance;
    @Getter
    private final Logger logger = Logger.getLogger(this.getName());

    // Configuration
    @Getter
    private SettingsConfiguration settings;
    @Getter
    private MessageConfiguration messages;

    // Managers
    @Getter
    private ExecutionManager executionManager;
    @Getter
    private InventoryManager inventoryManager;
    @Getter
    private InstancesManager instancesManager;

    @Override
    public void onEnable() {
        // Instance
        instance = this;

        // Read or create configurations
        messages = readOrCreateConfiguration(this, MessageConfiguration.class);
        settings = readOrCreateConfiguration(this, SettingsConfiguration.class);

        // Managers
        executionManager = new ExecutionManager();
        inventoryManager = new InventoryManager(this);

        // Init Managers
        executionManager.start();
        inventoryManager.init();
    }

    @Override
    public void onDisable() {
        // Cancel all tasks
        executionManager.shutdown();
    }
}
