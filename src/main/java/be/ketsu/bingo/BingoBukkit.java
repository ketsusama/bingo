package be.ketsu.bingo;

import be.ketsu.bingo.commands.BingoCommand;
import be.ketsu.bingo.commands.GameCommand;
import be.ketsu.bingo.commands.tab.GameTabCompleter;
import be.ketsu.bingo.configuration.Configurations;
import be.ketsu.bingo.configuration.files.MessageConfiguration;
import be.ketsu.bingo.configuration.files.SettingsConfiguration;
import be.ketsu.bingo.game.managers.BingoManager;
import be.ketsu.bingo.game.managers.InstancesManager;
import be.ketsu.bingo.gui.InventoryManager;
import be.ketsu.bingo.listeners.BlockListeners;
import be.ketsu.bingo.listeners.DamagesListeners;
import be.ketsu.bingo.listeners.PlayersListeners;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

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
    private BingoManager bingoManager;
    @Getter
    private InstancesManager instancesManager;

    @Override
    public void onEnable() {
        // Instance
        instance = this;

        // Read or create configurations
        messages = Configurations.readOrCreateConfiguration(this, MessageConfiguration.class);
        settings = Configurations.readOrCreateConfiguration(this, SettingsConfiguration.class);

        // Managers
        executionManager = new ExecutionManager();
        inventoryManager = new InventoryManager(this);
        bingoManager = new BingoManager();
        instancesManager = new InstancesManager();

        // Init Managers
        executionManager.start();
        inventoryManager.init();

        // Register Listeners
        this.getServer().getPluginManager().registerEvents(new PlayersListeners(), this);
        this.getServer().getPluginManager().registerEvents(new DamagesListeners(), this);
        this.getServer().getPluginManager().registerEvents(new BlockListeners(), this);

        // Register Commands
        getCommand("game").setExecutor(new GameCommand());
        getCommand("bingo").setExecutor(new BingoCommand());

        // Register Tab Completer
        getCommand("game").setTabCompleter(new GameTabCompleter());
    }

    @Override
    public void onDisable() {
        // Cancel all tasks
        //executionManager.shutdown();
    }
}
