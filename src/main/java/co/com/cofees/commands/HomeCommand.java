package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.commands.subcommands.DeleteHomeCommand;
import co.com.cofees.commands.subcommands.SetHomeCommand;
import co.com.cofees.completers.DeleteHomeCompleter;
import co.com.cofees.completers.SetHomeCompleter;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeCommand implements CommandExecutor, TabCompleter {

    private final Map<String, CommandExecutor> subCommands = new HashMap<>();
    private final Map<String, TabCompleter> subCommandCompleter = new HashMap<>();
    private final Map<String, HashMap<String, Location>> homes;

    public HomeCommand(HashMap<String, HashMap<String, Location>> homes) {
        this.homes = homes;
        register("set", new SetHomeCommand(), new SetHomeCompleter());//home set <name>
        register("delete", new DeleteHomeCommand(), new DeleteHomeCompleter());//home delete <name>
    }

    public void register(String name, CommandExecutor cmd, TabCompleter completer) {
        subCommands.put(name, cmd);
        subCommandCompleter.put(name, completer);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command");
            return false;
        }
        // Get the list of homes for the player
        HashMap<String, Location> playerHomes = homes.get(player.getName());

        if (args.length == 0) {//If player sends /home


            if (playerHomes.isEmpty()) { // If the list is empty, send a message to the player
                player.sendMessage("You don't have any homes set");
                return false;
            }

            if (playerHomes.size() != 1) { // If the player has more than one home
                player.sendMessage("You have more than one home set. Please specify which one you want to go to.");
                return false;
            }

            // If the player has only one home
            Location home = playerHomes.values()
                    .stream()
                    .findFirst()
                    .orElse(null); // Get the home
            player.teleport(home); // Teleport the player to the home
            return true;

        }

        // if player sends /home <subcommand> or /home <homeName>
        String subCommandName = args[0];
        CommandExecutor subCommand = subCommands.get(subCommandName);//check if the subcommand exists in the HashMap

        if (subCommand == null) {//If the subcommand does not exist in HashMap teleports the player to the home with the name of the subcommand
            if (!playerHomes.containsKey(subCommandName)) {
                player.sendMessage("Home " + subCommandName + " does not exist");
                return false;
            }
            player.teleport(playerHomes.get(subCommandName));
            return true;
        }

        String[] subCommandArgs = new String[args.length - 1];
        // Copy the remaining arguments into the new array
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        // Execute the subcommand with the remaining arguments
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
