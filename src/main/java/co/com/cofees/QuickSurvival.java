package co.com.cofees;

import co.com.cofees.commands.*;
import co.com.cofees.events.*;
import co.com.cofees.recipes.CustomRecipes;
import co.com.cofees.tools.LocationHandler;
import co.com.cofees.tools.Regions;
import co.com.cofees.tools.Waystone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class QuickSurvival extends JavaPlugin {
    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&b[QuickSurvival] ");
    public static YamlConfiguration homesConfig, backpackConfig, waystonesConfig;
    public static HashMap<String, HashMap<String, Location>> homes = new HashMap<>();
    public static HashMap<String, Waystone> waystones = new HashMap<>();
    private static QuickSurvival plugin;
    private static VacaNagasaki cowEvent = new VacaNagasaki();
    private static VeinMiner veinMiner = new VeinMiner();
    private static TreeCapitator treeCapitator = new TreeCapitator();

    @Override
    public void onEnable() {
        plugin = this;
        //Register the custom recipes
        CustomRecipes.registerCustomCrafting();
        registerConfigFiles();
        registerMaps();
        registerCommands();
        registerEvents();
        changeSleepingPlayers("50");
        Regions.getInstance().load();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + "&bPlugin enabled."));//Versión, Prefix PluginName


    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', PREFIX + "&bPlugin disabled"));
    }

    public void registerCommands() {
        this.getCommand("explosivecows").setExecutor(new ExplosiveCows());
        this.getCommand("test").setExecutor(new NewTestCommand(this));
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("inventory").setExecutor(new WaystoneCommand());
        this.getCommand("waystone").setExecutor(new WaystoneBannerInteract());
        this.getCommand("backpack").setExecutor(new BackpackCommand());
        this.getCommand("eventmenu").setExecutor(new ControlMenuCommand());
        this.getCommand("region").setExecutor(new RegionCommand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new SpawnerEvents(), this);
        getServer().getPluginManager().registerEvents(new WaystoneMenu(), this);
        getServer().getPluginManager().registerEvents(new BackpackInteract(), this);
        getServer().getPluginManager().registerEvents(new CraftBackpack(), this);
        getServer().getPluginManager().registerEvents(new WaystonePlacement(), this);
        getServer().getPluginManager().registerEvents(new WaystoneInteract(), this);
        getServer().getPluginManager().registerEvents(cowEvent, this);
        getServer().getPluginManager().registerEvents(veinMiner, this);
        getServer().getPluginManager().registerEvents(treeCapitator, this);
        getServer().getPluginManager().registerEvents(new ControlMenuHandler(), this);
        getServer().getPluginManager().registerEvents(new WaystoneMenuHandler(), this);
        getServer().getPluginManager().registerEvents(new RegionListener(), this);
    }

    public void registerConfigFiles() {
        //Load the configuration files
        homesConfig = getConfigFile("homes.yml", this);
        backpackConfig = getConfigFile("backpacks.yml", this);
        waystonesConfig = getConfigFile("waystones.yml", this);
    }

    public void registerMaps() {
        //Fill the information from the configuration files to the maps
        fillInfoFromYML(homesConfig, getHomes());
        fillInfoFromYML(waystonesConfig, getWaystones());
    }

    public void changeSleepingPlayers(String percentage) {
        Bukkit.getScheduler()
                .runTaskLater(
                        this,
                        () -> Bukkit.dispatchCommand(
                                Bukkit.getConsoleSender(),
                                "gamerule playersSleepingPercentage " + percentage
                        ),
                        1);
    }

    public void fillInfoFromYML(YamlConfiguration config, Consumer<String> function) {
        Bukkit.getScheduler()
                .runTaskLater(this,
                        () -> config.getKeys(false)
                                .forEach(function),
                        3);
    }

    public Consumer<String> getHomes() {
        //Load the homes for each player and add them to the homes map
        return (playerName) -> {
            HashMap<String, Location> playerHomes = LocationHandler.loadLocations(homesConfig, this, playerName);
            homes.put(playerName, playerHomes);
            this.getServer().getLogger().info("Loaded homes for: " + playerName);
        };
    }

    public Consumer<String> getWaystones() {
        //Load the waystones and add them to the waystones map
        return (waystoneName) -> {
            ConfigurationSection waystoneSection = waystonesConfig.getConfigurationSection(waystoneName);
            assert waystoneSection != null;
            Location location = LocationHandler.createLocationFromConfig(waystoneSection, "location", this);
            String name = waystoneSection.getString("name");
            List<String> players = waystoneSection.getStringList("players");
            ItemStack icon = waystoneSection.getItemStack("icon");
            Waystone waystone = new Waystone(location, name, players, icon);
            waystones.put(waystoneName, waystone);
            this.getServer().getLogger().info("Loaded waystone: " + ChatColor.GREEN + " " + waystoneName);

        };

    }

    public YamlConfiguration getConfigFile(String fileName, JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder(), fileName);
        if (!configFile.exists()) {
            plugin.getLogger().warning("File " + fileName + " doesn't exists. Creating file...");
            plugin.saveResource(fileName, false);
        }
        return YamlConfiguration.loadConfiguration(configFile);
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
