package co.com.cofees;

import co.com.cofees.commands.*;
import co.com.cofees.events.*;
import co.com.cofees.recipes.CustomRecipes;
import co.com.cofees.tools.LocationHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;

public class QuickSurvival extends JavaPlugin {
    public static YamlConfiguration homesConfig;
    public YamlConfiguration backpackConfig;
    public static HashMap<String, HashMap<String, Location>> homes = new HashMap<>();

    @Override
    public void onEnable() {
        homesConfig = getConfigFile("homes.yml", this);//create a file for homes
        backpackConfig = getConfigFile("backpacks.yml", this);
        CustomRecipes.registerCustomCrafting();
        registerCommand();
        registerEvents();
        getHomes();
        changeSleepingPlayers("50");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin enabled."));//Versión, Prefix PluginName
    }

    public void getHomes() {
        //Uncomment this if you want to test if the players are loaded correctly from the config
        //this.getLogger().warning(homesConfig.getKeys(false).toString());
        Bukkit.getScheduler().runTaskLater(this, () -> {
            homesConfig.getKeys(false).forEach(playerName -> {
                //Load the homes for each player and add them to the homes map
                HashMap<String, Location> playerHomes = LocationHandler.loadLocations(homesConfig, this, playerName);
                homes.put(playerName, playerHomes);
                this.getServer().getLogger().info("Loaded homes for: " + playerName);
            });
        }, 3);
    }

    public void changeSleepingPlayers(String percentage) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule playersSleepingPercentage " + percentage);
        }, 1);
    }

    public YamlConfiguration getConfigFile(String fileName, JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists()) {

            plugin.getLogger().warning(fileName + " doesn't exists. Creating file...");
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(configFile);
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin disabled"));
    }

    public void registerCommand() {
        this.getCommand("test").setExecutor(new NewTestCommand(this));
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("inventory").setExecutor(new WaystoneCommand());
        this.getCommand("waystone").setExecutor(new WaystoneBannerInteract());
        this.getCommand("backpack").setExecutor(new BackpackCommand());
        this.getCommand("qspanel").setExecutor(new ControlPanelCommmand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new TestEvent(), this);
        getServer().getPluginManager().registerEvents(new ControlPanelListener(), this);
        getServer().getPluginManager().registerEvents(new WaystoneMenu(), this);
        getServer().getPluginManager().registerEvents(new BackpackInteract(), this);
        getServer().getPluginManager().registerEvents(new WaystonePlacement(), this);
        getServer().getPluginManager().registerEvents(new WaystoneInteract(), this);


    }

    public static QuickSurvival getInstance() {
        return getPlugin(QuickSurvival.class);
    }
}
