package co.com.cofees.commands.subcommands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Regions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegionDeleteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) || args.length != 1) return false;

        Regions regions = Regions.getInstance();

        String name = args[0];

        if (regions.findRegion(name) == null) {
            sender.sendMessage(ChatColor.RED + "Region by this name does not exist.");

            return false;
        }

        regions.deleteRegion(name);

        sender.sendMessage("§8[§a✔§8] §7Region deleted!");
        return true;
    }
}
