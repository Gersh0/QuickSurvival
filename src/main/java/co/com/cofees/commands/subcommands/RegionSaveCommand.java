package co.com.cofees.commands.subcommands;

import co.com.cofees.commands.RegionCommand;
import co.com.cofees.tools.Regions;
import co.com.cofees.tools.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RegionSaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player) || args.length != 1) return false;

        Tuple<Location, Location> selection = RegionCommand.selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));

        Regions regions = Regions.getInstance();

        if (selection.getFirst() == null || selection.getSecond() == null) {
            sender.sendMessage("§8[§c❌§8] §7Please select both positions first using /region pos1 and /region pos2");
            return false;
        }

        String name = args[0];

        if (regions.findRegion(name) != null) {
            sender.sendMessage(ChatColor.RED + "Region by this name already exists.");

            return true;
        }

        List<String> players = new ArrayList<>();
        players.add(player.getName());

        regions.saveRegion(name, selection.getFirst(), selection.getSecond(), players);

        //delete the selections after saving

        selection.setFirst(null);
        selection.setSecond(null);

        sender.sendMessage("§8[§a✔§8] §8Region saved!");
        return true;
    }
}
