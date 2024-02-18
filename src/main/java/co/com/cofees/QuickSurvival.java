package co.com.cofees;

import co.com.cofees.commands.TestCommand;
import co.com.cofees.events.TestEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

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
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new TestEvent(), this);
    }
}
