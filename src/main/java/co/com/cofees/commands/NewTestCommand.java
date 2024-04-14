package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.commands.subcommands.FlyCommand;
import co.com.cofees.commands.subcommands.HealCommand;
import co.com.cofees.completers.FlyCompleter;
import co.com.cofees.completers.HealCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static co.com.cofees.tools.TextTools.sendMessage;

public class NewTestCommand implements CommandExecutor, TabCompleter {
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();
    private final Map<String, TabCompleter> subCommandCompleters = new HashMap<>();
    private final QuickSurvival core;

    public NewTestCommand(QuickSurvival core) {
        this.core = core;
        register("heal", new HealCommand(), new HealCompleter()); //<"heal", new HealCommand()> ---- <"heal", new HealCompleter()>
        register("fly", new FlyCommand(), new FlyCompleter());
    }

    public void register(String name, CommandExecutor cmd, TabCompleter completer) {
        subCommands.put(name, cmd);
        subCommandCompleters.put(name, completer);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // If no arguments are provided
        if (args.length == 0) {
            // Prepare a success message
            String message = "Test successful from ";
            // Send the message to the sender (player or console)
            return sendMessage(sender, message + sender.getName(), message + "console!", core);
        }

        // Get the name of the subcommand from the first argument
        String subCommandName = args[0];
        // Retrieve the corresponding CommandExecutor for the subcommand trying to take it
        // from the subCommands HashMap.
        CommandExecutor subCommand = subCommands.get(subCommandName);

        // If the subcommand does not exist in HashMap
        if (subCommand == null) {
            // Prepare an error message
            String message = "Invalid command";
            // Send the error message to the sender
            return sendMessage(sender, "", message, core);
        }

        // Prepare the arguments for the subcommand by removing the first argument
        String[] subCommandArgs = new String[args.length - 1];
        // Copy the remaining arguments into the new array
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        // Execute the subcommand with the remaining arguments
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (!(sender instanceof Player)) return null;

        if (args.length == 1) {
            return new ArrayList<>(subCommands.keySet());
        }

        if (args.length > 1) {
            TabCompleter completer = subCommandCompleters.get(args[0]);
            if (completer != null) {
                return completer.onTabComplete(sender, command, alias, args);
            }
        }

        return null;
    }

}
