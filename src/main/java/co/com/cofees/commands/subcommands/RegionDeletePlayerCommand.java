package co.com.cofees.commands.subcommands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Region;
import co.com.cofees.tools.Regions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RegionDeletePlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§8[§6🛑§8] §7Usage: /region deletePlayer <region> <player>");

            return true;
        }

        Regions regions = Regions.getInstance();


        String regionName = args[0];
        String playerName = args[1];

        Region region = regions.findRegion(regionName);
        List<String> players = regions.getPlayersInRegion(regionName);

        if (region == null) {
            sender.sendMessage(ChatColor.RED + "Region by this name does not exist.");

            return true;
        }

        if (players == null || !players.contains(playerName)) {
            sender.sendMessage(ChatColor.RED + "Player is not in this region.");
            return true;
        }

        region.deletePlayer(playerName);
        regions.saveRegion(region, QuickSurvival.regionsConfig, region.getName());

        sender.sendMessage("§8[§a✔§8] §7Player removed from region!");
        return true;
    }
}
