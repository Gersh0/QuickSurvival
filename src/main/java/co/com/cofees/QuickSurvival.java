package co.com.cofees;

import co.com.cofees.commands.*;
import co.com.cofees.events.*;
import co.com.cofees.recipes.CustomRecipes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

public class QuickSurvival extends JavaPlugin {
    public YamlConfiguration homesConfig, backpackConfig;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin enabled."));//Versión, Prefix PluginName
        homesConfig = getConfigFile("homes.yml", this);//create a file for homes
        backpackConfig = getConfigFile("backpacks.yml", this);
        CustomRecipes.registerCustomCrafting();
        registerCommand();
        registerEvents();
        changeSleepingPlayers("50");
    }

    public void changeSleepingPlayers(String percentage){
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
        this.getCommand("home").setExecutor(new HomeCommand(this, homesConfig));
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
