package co.com.cofees.commands.subcommands;

import co.com.cofees.tools.TextTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FlyCommand implements CommandExecutor {

    public FlyCommand() {
        // Add more subcommands as needed
    }

    public String text(String text) {
        return TextTools.coloredText(text);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        // If the sender is not a player and no arguments are provided
        if ((!(sender instanceof Player)) && args.length == 0) {
            // Log a warning message indicating that this command can't be used from the console
            sender.getServer().getLogger().warning("Can't use this command from console.");
            return true;
        }

        // If the sender is not a player
        if (!(sender instanceof Player)) {
            // Log an info message indicating that fly mode has been enabled for the player specified in the arguments
            sender.getServer().getLogger().info("Fly mode toggled for " + args[0]);
            // Toggle fly mode for the player specified in the arguments
            return toggleFly(Objects.requireNonNull(sender.getServer().getPlayer(args[0])));
        }
        // Cast the sender to a Player
        Player player = (Player) sender;
        // If no arguments are provided
        if (args.length == 0) {
            // Toggle fly mode for the player who sent the command
            return toggleFly(player);
        }

        // If none of the online players' names match the name provided in the arguments
        if (sender.getServer().getOnlinePlayers().stream()
                .map(Player::getName)
                .noneMatch(name -> name.equalsIgnoreCase(args[0]))) {
            // Send a message to the sender
            sender.sendMessage("There is no player online with that name.");

            return false;
        }
        // Toggle fly mode for the player specified in the arguments
        toggleFly(Objects.requireNonNull(sender.getServer().getPlayer(args[0])));
        return true;
    }

    public boolean toggleFly(Player player) {
        // If the player does not currently have flight enabled
        if (!player.getAllowFlight()) {
            // Send a message to the player indicating that fly mode has been enabled
            player.sendMessage(text("&bFly mode enabled for " + player.getName()));
            // Enable flight for the player
            player.setAllowFlight(true);
            // Return true indicating that the operation was successful
            return true;
        }
        // Send a message to the player indicating that fly mode has been disabled
        player.sendMessage(text("&bFly mode disabled for " + player.getName()));
        // Disable flight for the player
        player.setAllowFlight(false);
        // Return true indicating that the operation was successful
        return true;
    }

}
