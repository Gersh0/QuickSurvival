package co.com.cofees.commands;

import co.com.cofees.commands.subcommands.*;
import co.com.cofees.completers.RegionAddPlayerCompleter;
import co.com.cofees.completers.RegionDeleteCompleter;
import co.com.cofees.completers.RegionDeletePlayerCompleter;
import co.com.cofees.tools.Regions;
import co.com.cofees.tools.Tuple;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class RegionCommand implements CommandExecutor, TabCompleter {

    private final Map<String, CommandExecutor> subCommands = new HashMap<>();
    private final Map<String, TabCompleter> subCommandCompleters = new HashMap<>();

    public RegionCommand() {
        register("addPlayer", new RegionAddPlayerCommand(), new RegionAddPlayerCompleter());
        register("deletePlayer", new RegionDeletePlayerCommand(), new RegionDeletePlayerCompleter());
        register("pos", new RegionPosCommand(), null);
        register("save", new RegionSaveCommand(), null);
        register("list", new RegionListCommand(), null);
        register("current", new RegionCurrentCommand(), null);
        register("delete", new RegionDeleteCommand(), new RegionDeleteCompleter());
    }

    public static final Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) return false;

        // If there are 1 or more arguments
        String subCommandName = args[0];
        CommandExecutor subCommand = subCommands.get(subCommandName);

        //check if the subcommand exists in the HashMap
        if (subCommand == null) {
            player.sendMessage(ChatColor.RED + "Unknown argument: " + subCommandName);
            return false;
        }

        //1. Not a player checked
        //2. No arguments checked
        //3. Subcommand does not exist (any text that is not pos1, pos2, save, etc) checked
        //4. Subcommand exists, is the next step

        String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }

    //todo: check if the player is or no an Operator o has permission to use the completer
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null;

        if (command.getName().equalsIgnoreCase("region") && args.length == 1) {
            List<String> completions = new ArrayList<>(subCommands.keySet());
            completions.addAll(Regions.getInstance().getRegionsNames());
            return completions;
        }

        if (args.length > 1) {
            String subCommandName = args[0];
            TabCompleter completer = subCommandCompleters.get(subCommandName);
            if (completer != null) {
                return completer.onTabComplete(sender, command, alias, args);
            }
        }

        return null;
    }

    public void register(String name, CommandExecutor cmd, TabCompleter completer) {
        subCommands.put(name, cmd);
        subCommandCompleters.put(name, completer);
    }
}