package co.com.cofees;

import co.com.cofees.commands.TestCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickSurvival extends JavaPlugin {
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&bPlugin enabled."));//Versión, Prefix PluginName
        registerCommand();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&',"&bPlugin disabled"));
    }

    public void registerCommand(){
        this.getCommand("test").setExecutor(new TestCommand());
    }
}
