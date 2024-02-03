package co.com.cofees;

import co.com.cofees.commands.ExplosiveCows;
import co.com.cofees.commands.TestCommand;
import co.com.cofees.events.VacaNagasaki;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class QuickSurvival extends JavaPlugin {

    PluginDescriptionFile desc = getDescription();
    VacaNagasaki cowEvent = new VacaNagasaki();


    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin enabled, ver: "+desc.getVersion()));//Versión, Prefix PluginName
        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&bPlugin disabled"));
    }

    public void registerCommands() {

        this.getCommand("test").setExecutor(new TestCommand());
        this.getCommand("explosivecows").setExecutor(new ExplosiveCows(cowEvent));
    }

    public void registerEvents(){
        getServer().getPluginManager().registerEvents(cowEvent, this);
    }


}
