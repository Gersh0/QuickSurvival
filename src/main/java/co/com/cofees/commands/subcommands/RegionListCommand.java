package co.com.cofees.commands.subcommands;

import co.com.cofees.tools.Regions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RegionListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 0) {
            return false;
        }

        if (!sender.hasPermission("cofees.regions.list")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        Regions regions = Regions.getInstance();
        sender.sendMessage(ChatColor.GOLD + "Installed regions: " + String.join(", ", regions.getRegionsNames()));
        return true;
    }
}
