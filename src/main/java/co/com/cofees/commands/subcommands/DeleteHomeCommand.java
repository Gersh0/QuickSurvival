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
        /*
        To delete a home, the following conditions must be met:
        1. The sender is a Player
        2. There are arguments (a name for the home or something else)
        3. args have only one element (the name of the home)
        4. Home name is not 'list' (list is shown with tab completion)
        5. The home exists in the player's homes HashMap
        6. At this point, the next step is to remove the home from the HashMap and the YAML configuration
        */

        if (!(commandSender instanceof Player player)) {//Check #1
            return false;
        }

        if (args.length == 0) {//Check #2
            commandSender.sendMessage("You need to specify a name for the home");
            return false;
        }

        if (args.length > 1) {//Check #3
            commandSender.sendMessage("The name of the home can't have spaces");
            return false;
        }

        if (args[0].equalsIgnoreCase("list")) {//Check #4
            commandSender.sendMessage("You can't use 'list' as a home name");
            return false;
        }

        //At this point, the sender is a Player, so we can safely get the player's name
        String playerName = player.getName();
        String homeKey = playerName + "." + args[0];
        //try to get the player's homes from the HashMap, if it doesn't exist, create a new HashMap
        HashMap<String, Location> playerHomes = QuickSurvival.homes.getOrDefault(playerName, new HashMap<>());

        if (!playerHomes.containsKey(args[0])) {//Check #5
            player.sendMessage("Home " + args[0] + " does not exist.");
            return false;
        }

        // Remove the home from the HashMap
        playerHomes.remove(args[0]);
        QuickSurvival.homes.put(playerName, playerHomes); // Update the map

        // Remove the home from the YAML configuration
        QuickSurvival.homesConfig.set(homeKey, null);

        // Try to save the updated configuration back to the file
        try {
            QuickSurvival.homesConfig.save(new File(QuickSurvival.getPlugin(QuickSurvival.class).getDataFolder(), "homes.yml"));
            player.sendMessage("Home " + args[0] + " deleted.");
            return true; // Return true if deletion is successful
        } catch (IOException e) {
            player.sendMessage("An error occurred while saving the homes file.");
            System.out.println(e.getMessage());
            return false;
        }
    }
}

