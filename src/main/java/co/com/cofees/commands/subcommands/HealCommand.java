package co.com.cofees.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class HealCommand implements CommandExecutor{
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public HealCommand() {
        // Add more subcommands as needed
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // Try to get the sender as a Player
        Optional<Player> player = getPlayer(sender);
        // If no arguments are provided
        if (args.length == 0) {
            // If the sender is a Player
            if (player.isPresent()) {
                // Send a message to the player indicating they have been healed
                player.get().sendMessage("You have been healed!");
                // Log an info message indicating the player has been healed
                sender.getServer().getLogger().info(player.get().getName() + " healed!");
                // Set the player's health to 20 (full health)
                player.get().setHealth(20.0);
                return true;
            } else {
                // If the sender is not a Player, log a warning message indicating that only players can use this command
                sender.getServer().getLogger().warning("Only players can use this command.");
                return false; // Handle the case where no subcommand is provided
            }
        }

        // If one argument is provided
        if (args.length == 1) {
            // Try to get the online player with the nickname provided in the arguments
            player = getPlayer(getOnlinePlayerByNickname(sender, args[0].toLowerCase()));
            // If the player is online
            if (player.isPresent()) {
                // Send a message to the player indicating they have been healed
                player.get().sendMessage("You have been healed!");
                // Log an info message indicating the player has been healed
                sender.getServer().getLogger().info(player.get().getName() + " healed!");
                // Set the player's health to 20 (full health)
                player.get().setHealth(20);
                return true;
            } else {
                // If the player is not online, log a warning message indicating this
                sender.getServer().getLogger().warning(args[0] + " is not online!");
                return false;
            }
        }

        // Get the name of the subcommand from the first argument
        String subCommandName = args[0];
        // Retrieve the corresponding CommandExecutor for the subcommand
        CommandExecutor subCommand = subCommands.get(subCommandName);

        // If the subcommand does not exist
        if (subCommand == null) {
            // Log a warning message indicating that the argument does not exist
            sender.getServer().getLogger().warning("Argument doesn't exists.");
            return false; // Handle the case where the subcommand does not exist
        }

        // Prepare the arguments for the subcommand by removing the first argument
        String[] subCommandArgs = new String[args.length - 1];
        // Copy the remaining arguments into the new array
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        // Execute the subcommand with the remaining arguments
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }


    private Optional<Player> getPlayer(Object sender) {
        return (sender instanceof Player) ? Optional.of((Player) sender) : Optional.empty();
    }

    public Player getOnlinePlayerByNickname(CommandSender sender, String nickname) {
        return sender.getServer().getOnlinePlayers().stream()
                .filter(player -> player.getName().equalsIgnoreCase(nickname))
                .findFirst()
                .orElse(null);
    }
}
