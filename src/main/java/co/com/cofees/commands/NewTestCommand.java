package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.commands.subcommands.HealCommand;
import co.com.cofees.commands.subcommands.FlyCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NewTestCommand implements CommandExecutor {
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();
    private final QuickSurvival core;

    public NewTestCommand(QuickSurvival core) {
        this.core = core;
        subCommands.put("heal", new HealCommand());
        subCommands.put("subcommand2", new FlyCommand());
        // Add more subcommands as needed
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            String message = "Test successful from ";
            return sendMessage(sender, Optional.of(message + sender.getName()), message + "console!", core);
        }

        String subCommandName = args[0];
        CommandExecutor subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            String message = "Invalid command";
            return sendMessage(sender, Optional.empty(), message, core);
        }

        // Execute the subcommand, passing the remaining arguments
        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }

    private Optional<Player> getPlayer(Object sender) {
        return (sender instanceof Player) ? Optional.of((Player) sender) : Optional.empty();
    }

    public boolean sendMessage(Object sender, Optional<String> playerMessage, String otherMessage, QuickSurvival core) {
        Optional<Player> player = getPlayer(sender);
        if (player.isPresent()) {
            player.get().sendMessage(playerMessage.orElse(otherMessage));
            return true;
        } else {
            core.getLogger().info(otherMessage);
            return false;
        }
    }
}
