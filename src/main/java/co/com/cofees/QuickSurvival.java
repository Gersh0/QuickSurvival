package co.com.cofees;

import co.com.cofees.commands.*;
import co.com.cofees.events.*;
import co.com.cofees.recipes.CustomRecipes;
import com.sun.source.tree.Tree;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;

public class QuickSurvival extends JavaPlugin {


    private static QuickSurvival plugin;
    PluginDescriptionFile desc = getDescription();
    private static VacaNagasaki cowEvent = new VacaNagasaki();
    private static VeinMiner veinMiner = new VeinMiner();
    private static TreeCapitator treeCapitator = new TreeCapitator();

    private static int currentSleepPercentage = 100;


    public YamlConfiguration homesConfig, backpackConfig;

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin enabled, ver: "+desc.getVersion()));//Versión, Prefix PluginName
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin enabled, ver: " + desc.getVersion()));//Versión, Prefix PluginName
        registerCommands();
        registerEvents();
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin enabled."));//Versión, Prefix PluginName
        homesConfig = getConfigFile("homes.yml", this);//create a file for homes
        backpackConfig = getConfigFile("backpacks.yml", this);
        CustomRecipes.registerCustomCrafting();
        //changeSleepingPlayers("50");
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
        this.getCommand("test").setExecutor(new TestCommand());
        this.getCommand("explosivecows").setExecutor(new ExplosiveCows());
        this.getCommand("test").setExecutor(new NewTestCommand(this));
        this.getCommand("home").setExecutor(new HomeCommand(this, homesConfig));
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
        getServer().getPluginManager().registerEvents(new WaystonePlacement(), this);
        getServer().getPluginManager().registerEvents(new WaystoneInteract(), this);
        getServer().getPluginManager().registerEvents(cowEvent, this);
        getServer().getPluginManager().registerEvents(veinMiner, this);
        getServer().getPluginManager().registerEvents(treeCapitator, this);
        getServer().getPluginManager().registerEvents(new ControlMenuHandler(), this);
    }

    public static QuickSurvival getPlugin(){
        return plugin;
    }

    public static QuickSurvival getInstance() {
        return getPlugin(QuickSurvival.class);
    }

    public static void toggleExplosiveCows(){
        cowEvent.toggleExplosiveCows();
    }

    public static boolean areCowsExplosive(){
        return cowEvent.areCowsExplosive();
    }

    public static void toggleVeinMiner(){
        veinMiner.toggleVeinMiner();
    }

    public static boolean isVeinMinerActive(){
        return veinMiner.isActive();
    }

    public static void toggleTreeCapitator(){
        treeCapitator.toggleTreeCapitator();
    }

    public static boolean isTreeCapitatorActive(){
        return treeCapitator.isActive();
    }

    public static int getSleepingPercentage(){  return currentSleepPercentage;}

    public static void setSleepingPercentage(int percentage){
        currentSleepPercentage = percentage;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule playersSleepingPercentage " + percentage);
    }

}
