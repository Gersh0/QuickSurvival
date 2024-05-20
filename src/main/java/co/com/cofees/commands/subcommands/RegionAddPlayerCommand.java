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

public class RegionAddPlayerCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§8[§6🛑§8] §7Usage: /region addPlayer <region> <player>");

            return true;
        }

        Regions regions = Regions.getInstance();

        String regionName = args[0];
        String playerName = args[1];

        List<String> players = regions.getPlayersInRegion(regionName);
        Region region = regions.findRegion(regionName);

        if (region == null) {
            sender.sendMessage(ChatColor.RED + "Region by this name does not exist.");

            return false;
        }

        if ((players == null || !players.contains(playerName)) && QuickSurvival.getInstance().getServer().getPlayer(playerName) != null) {
            region.addPlayer(playerName);
            regions.saveRegion(region, QuickSurvival.regionsConfig, region.getName());

            sender.sendMessage("§8[§a✔§8] §7Player added to region!");
            return true;
        }
        sender.sendMessage(ChatColor.RED + "You cannot add a player, playes is already in this region or does not exists in this region or is not online.");
        return false;
    }
}
