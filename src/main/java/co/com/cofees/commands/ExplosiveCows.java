package co.com.cofees.commands;

import co.com.cofees.events.VacaNagasaki;
import co.com.cofees.tools.TextTools;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ExplosiveCows implements CommandExecutor, TabExecutor {
    private VacaNagasaki event;
    public ExplosiveCows(VacaNagasaki event){
        this.event = event;
    }
    private JavaPlugin plugin;
    public ExplosiveCows(JavaPlugin p){
        this.plugin = p;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0){
            event.toggleExplosiveCows();
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return null;
    }
}
