package co.com.cofees.commands;

import co.com.cofees.commands.subcommands.SubCommand1;
import co.com.cofees.commands.subcommands.SubCommand2;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class NewTestCommand implements CommandExecutor {
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public NewTestCommand() {
        subCommands.put("subcommand1", new SubCommand1());
        subCommands.put("subcommand2", new SubCommand2());
        // Add more subcommands as needed
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false; // Handle the case where no subcommand is provided (or show help)
        }

        // /quicksurvival hola(args[0]) fly(args[1]) GerMinatorCofee(args[2])
        String subCommandName = args[0];
        CommandExecutor subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            return false; // Handle the case where the subcommand does not exist Argumento no válido, usage: /quicksurvival <subcomando> <mas cosas>
        }

        // Execute the subcommand, passing the remaining arguments
        String[] subCommandArgs = new String[args.length - 1];

        // [fly(args[1]), GerMinatorCofee(args[2])]
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }
}
