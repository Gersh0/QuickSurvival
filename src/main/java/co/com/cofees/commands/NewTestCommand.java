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

import java.util.*;

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

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {

        if (!(sender instanceof Player)) return null; //Clauses the case if is not a player.

        if (args.length == 1) {
            // If the user is still typing the subcommand, suggest subcommands
            return new ArrayList<>(subCommands.keySet());
        } else if (args.length > 1) {
            // If a subcommand has been typed, delegate to its TabCompleter
            TabCompleter completer = subCommandCompleters.get(args[0]);
            if (completer != null) {
                return completer.onTabComplete(sender, command, alias, args);
            }
        }
        return null;
    }
}
