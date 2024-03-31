package co.com.cofees.commands.subcommands;

import co.com.cofees.QuickSurvival;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class DeleteHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            commandSender.sendMessage("You need to specify a name for the home");
            return false;
        }
        if (args.length > 1) {
            commandSender.sendMessage("The name of the home can't have spaces");
            return false;
        }
        if (args[0].equalsIgnoreCase("list")) {
            commandSender.sendMessage("You can't use 'list' as a home name");
            return false;
        }
        if (commandSender instanceof Player player) {
            String playerName = player.getName();
            String homeKey = playerName + "." + args[0];
            HashMap<String, Location> playerHomes = QuickSurvival.homes.getOrDefault(playerName, new HashMap<>());

            if (playerHomes.containsKey(args[0])) {
                // Remove the home from the HashMap
                playerHomes.remove(args[0]);
                QuickSurvival.homes.put(playerName, playerHomes); // Update the map

                // Remove the home from the YAML configuration
                QuickSurvival.homesConfig.set(homeKey, null);

                // Save the updated configuration back to the file
                try {
                    QuickSurvival.homesConfig.save(new File(QuickSurvival.getPlugin(QuickSurvival.class).getDataFolder(), "homes.yml"));
                    return true; // Return true if deletion is successful
                } catch (IOException e) {
                    player.sendMessage("An error occurred while saving the homes file.");
                    e.printStackTrace();
                }
            } else {
                player.sendMessage("Home " + args[0] + " does not exist.");
            }
            return false; // Return false if the home does not exist
        }
        return false;
    }
}

