package co.com.cofees.commands;

import co.com.cofees.commands.subcommands.*;
import co.com.cofees.completers.RegionAddPlayerCompleter;
import co.com.cofees.completers.ShowAllRegionsCompleter;
import co.com.cofees.completers.RegionDeletePlayerCompleter;
import co.com.cofees.completers.ShowAllRegionsCompleter;
import co.com.cofees.tools.Region;
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
import java.util.stream.Collectors;

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
        register("delete", new RegionDeleteCommand(), new ShowAllRegionsCompleter());
        register("show", new ShowRegionCommand(), new ShowAllRegionsCompleter());
        register("scroll", new RegionScrollCommand(), null);
    }

    public void register(String name, CommandExecutor cmd, TabCompleter completer) {
        subCommands.put(name, cmd);
        subCommandCompleters.put(name, completer);
    }

    // TODO Move into PlayerCache
    public static final Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            //sender.sendMessage("Usage: /region <pos1|pos2|save <name>|paste <name>>");
            return false;
        }

        //if are there 1 or more arguments
        String subCommandName = args[0];
        CommandExecutor subCommand = subCommands.get(subCommandName);

        //check if the subcommand exists in the HashMap
        if (subCommand == null) {//there are arguments but the subcommand does not exist
            sender.sendMessage(ChatColor.RED + "Unknown argument: " + subCommandName);
            return false;
        }


        //1. Not a player
        //2. No arguments
        //3. Subcommand does not exist (any text that is not pos1, pos2, save, etc)
        //4. Subcommand exists

        String[] subCommandArgs = new String[args.length - 1];
        // Copy the remaining arguments into the new array
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        // Execute the subcommand with the remaining arguments
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null; //Clauses the case if is not a player.
        if (command.getName().equalsIgnoreCase("region") && args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("pos");
            completions.add("list");
            completions.add("addPlayer");
            completions.add("deletePlayer");
            completions.add("current");
            completions.add("delete");
            completions.add("save");
            completions.add("show");
            completions.add("scroll");
            return completions;
        }


        if (args.length > 1 && args[0].equalsIgnoreCase("save")) {
            return Collections.singletonList("<name>");
        }



        return null;
    }


    // Register a subcommand with its executor and completer



}