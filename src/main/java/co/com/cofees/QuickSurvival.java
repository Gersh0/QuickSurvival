package co.com.cofees;


import co.com.cofees.commands.*;
import co.com.cofees.events.*;
import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickSurvival extends JavaPlugin {

    private boolean prueba;

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin enabled."));//Versión, Prefix PluginName
        registerCommand();

        registerEvents();
        this.prueba = true;
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin disabled"));
    }

    public void registerCommand() {
        this.getCommand("test").setExecutor(new TestCommand());
        this.getCommand("inventory").setExecutor(new WaystoneCommand());

    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new TestEvent(), this);
        getServer().getPluginManager().registerEvents(new WaystoneMenu(),this);
        getServer().getPluginManager().registerEvents(new WaystoneInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new BackpackInteract(), this);
    }

    public static QuickSurvival getInstance() {
        return QuickSurvival.getPlugin(QuickSurvival.class);
    }

}
