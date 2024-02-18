package co.com.cofees.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class SubCommand1 implements CommandExecutor {
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public SubCommand1() {
        // Add more subcommands as needed
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false; // Handle the case where no subcommand is provided
        }

        String subCommandName = args[0];
        CommandExecutor subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            return false; // Handle the case where the subcommand does not exist
        }

        // Execute the subcommand, passing the remaining arguments
        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }
}
