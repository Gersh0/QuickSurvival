package co.com.cofees.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&4Can't use this command from console."));
            return true;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',"&4Test Successfull."));

        return true;
    }
}
