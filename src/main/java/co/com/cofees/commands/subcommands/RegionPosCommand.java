package co.com.cofees.commands.subcommands;

import co.com.cofees.commands.RegionCommand;
import co.com.cofees.tools.Tuple;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RegionPosCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return false;

        Tuple<Location, Location> selection = RegionCommand.selections.getOrDefault(player.getUniqueId(), new Tuple<>(null, null));
        Block bLoc = player.getTargetBlockExact(5);

        if (bLoc == null) {
            sender.sendMessage("§8[§c❌§8] §7Please look at a block!");
            return false;
        }

        Location loc = bLoc.getLocation();
        switch (args[0]) {
            case "1" -> {
                selection.setFirst(loc);
                player.sendMessage("§8[§a✔§8] §7First location set!");
            }
            case "2" -> {
                selection.setSecond(loc);
                player.sendMessage("§8[§a✔§8] §7Second location set!");
            }
            default -> {
                player.sendMessage("§8[§c❌§8] §7Invalid argument! Use 1 or 2.");
                return false;
            }
        }
        RegionCommand.selections.put(player.getUniqueId(), selection);
        return true;
    }
}
