package co.com.cofees;

import co.com.cofees.commands.*;
import co.com.cofees.events.*;
import co.com.cofees.recipes.CustomRecipes;
import co.com.cofees.tools.LocationHandler;
import co.com.cofees.tools.Waystone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class QuickSurvival extends JavaPlugin {
    public static YamlConfiguration homesConfig, backpackConfig, waystonesConfig;

    public static HashMap<String, HashMap<String, Location>> homes = new HashMap<>();

    public static HashMap<String, Waystone> waystones = new HashMap<>();


    private static QuickSurvival plugin;
    PluginDescriptionFile desc = getDescription();
    private static VacaNagasaki cowEvent = new VacaNagasaki();
    private static VeinMiner veinMiner = new VeinMiner();
    private static TreeCapitator treeCapitator = new TreeCapitator();

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bQUICKSURVIVAL PLUGIN enabled."));//Versión, Prefix PluginName
        homesConfig = getConfigFile("homes.yml", this);//create a file for homes
        backpackConfig = getConfigFile("backpacks.yml", this);
        waystonesConfig = getConfigFile("waystones.yml", this);
        CustomRecipes.registerCustomCrafting();
        registerCommands();
        registerEvents();
        getHomes();
        getWaystones();
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

    //add getWaystones method
    public void getWaystones() {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            waystonesConfig.getKeys(false).forEach(waystoneName -> {
                //Load the waystones for each player and add them to the waystones map
                ConfigurationSection section = QuickSurvival.waystonesConfig.getConfigurationSection(waystoneName);
                assert section != null;
                Location waystoneLocation = LocationHandler.createLocationFromConfig(section, "location", this);
                List<String> players = QuickSurvival.waystonesConfig.getStringList(waystoneName + ".players");
                ItemStack icon = waystonesConfig.getItemStack(waystoneName + ".icon");


                Waystone waystone = new Waystone(waystoneLocation, waystoneName, players, icon);

                waystones.put(waystoneName, waystone);
                this.getServer().getLogger().info("Loaded waystone: " + waystoneName);
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

    public void registerCommands() {
        this.getCommand("explosivecows").setExecutor(new ExplosiveCows());
        this.getCommand("test").setExecutor(new NewTestCommand(this));
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("inventory").setExecutor(new WaystoneCommand());
        this.getCommand("waystone").setExecutor(new WaystoneBannerInteract());
        this.getCommand("backpack").setExecutor(new BackpackCommand());
        this.getCommand("eventmenu").setExecutor(new ControlMenuCommand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new TestEvent(), this);
        getServer().getPluginManager().registerEvents(new ControlPanelListener(), this);
        getServer().getPluginManager().registerEvents(new WaystoneMenu(), this);
        getServer().getPluginManager().registerEvents(new BackpackInteract(), this);
        getServer().getPluginManager().registerEvents(new CraftBackpack(), this);
        getServer().getPluginManager().registerEvents(new WaystonePlacement(), this);
        getServer().getPluginManager().registerEvents(new WaystoneInteract(), this);
        getServer().getPluginManager().registerEvents(cowEvent, this);
        getServer().getPluginManager().registerEvents(veinMiner, this);
        getServer().getPluginManager().registerEvents(treeCapitator, this);
        getServer().getPluginManager().registerEvents(new ControlMenuHandler(), this);
    }

    public static QuickSurvival getPlugin() {
        return plugin;
    }

    public static QuickSurvival getInstance() {
        return getPlugin(QuickSurvival.class);
    }

    public static void toggleExplosiveCows() {
        cowEvent.toggleExplosiveCows();
    }

    public static boolean areCowsExplosive() {
        return cowEvent.areCowsExplosive();
    }

    public static void toggleVeinMiner() {
        veinMiner.toggleVeinMiner();
    }

    public static boolean isVeinMinerActive() {
        return veinMiner.isActive();
    }

    public static void toggleTreeCapitator() {
        treeCapitator.toggleTreeCapitator();
    }

    public static boolean isTreeCapitatorActive() {
        return treeCapitator.isActive();
    }

}
