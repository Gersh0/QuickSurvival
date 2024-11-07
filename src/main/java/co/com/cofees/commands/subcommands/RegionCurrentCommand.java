package co.com.cofees.commands.subcommands;

import co.com.cofees.tools.Region;
import co.com.cofees.tools.Regions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegionCurrentCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Regions regions = Regions.getInstance();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /region current");
            return false;
        }

        Region standingIn = regions.findRegion(player.getLocation());

        if (standingIn != null) {
            sender.sendMessage(ChatColor.GOLD + "You are standing in region: "
                    + standingIn.getName());
            return true;
        }
        sender.sendMessage(ChatColor.GOLD + "You are not in a protected region.");
        return false;

    }
}
