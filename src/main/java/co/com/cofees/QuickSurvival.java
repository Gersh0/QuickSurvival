package co.com.cofees;

import co.com.cofees.commands.TestCommand;
import co.com.cofees.events.TestEvent;
import co.com.cofees.recipes.CustomRecipes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import co.com.cofees.events.*;
import co.com.cofees.commands.*;

public class QuickSurvival extends JavaPlugin {

    private boolean prueba;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin enabled."));//Versión, Prefix PluginName
        registerCommand();
        registerEvents();
        CustomRecipes.registerCustomCrafting();

        this.prueba = true;
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin disabled"));
    }

    public void registerCommand() {
        this.getCommand("test").setExecutor(new NewTestCommand(this));
        this.getCommand("inventory").setExecutor(new WaystoneCommand());
        this.getCommand("waystone").setExecutor(new WaystoneBannerInteract());
        this.getCommand("backpack").setExecutor(new BackpackCommand());
        this.getCommand("qspanel").setExecutor(new ControlPanelCommmand());
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new TestEvent(), this);
        getServer().getPluginManager().registerEvents(new ControlPanelListener(),this);
        getServer().getPluginManager().registerEvents(new WaystoneMenu(),this);
        getServer().getPluginManager().registerEvents(new BackpackInteract(), this);
        getServer().getPluginManager().registerEvents(new WaystonePlacement(),this);
        getServer().getPluginManager().registerEvents(new WaystoneInteract(),this);
        getServer().getPluginManager().registerEvents(new CraftBackpack(),this);


    }

    public static QuickSurvival getInstance(){
        return getPlugin(QuickSurvival.class);
    }
}
