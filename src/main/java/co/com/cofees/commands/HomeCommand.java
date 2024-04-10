package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.commands.subcommands.DeleteHomeCommand;
import co.com.cofees.commands.subcommands.SetHomeCommand;
import co.com.cofees.completers.DeleteHomeCompleter;
import co.com.cofees.completers.SetHomeCompleter;
import co.com.cofees.tools.TextTools;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeCommand implements CommandExecutor, TabCompleter {

    private final Map<String, CommandExecutor> subCommands = new HashMap<>();
    private final Map<String, TabCompleter> subCommandCompleters = new HashMap<>();

    public HomeCommand() {
        register("set", new SetHomeCommand(), new SetHomeCompleter());//home set <name>
        register("delete", new DeleteHomeCommand(), new DeleteHomeCompleter());//home delete <name>
    }

    public void register(String name, CommandExecutor cmd, TabCompleter completer) {
        subCommands.put(name, cmd);
        subCommandCompleters.put(name, completer);
    }

    public Location getHomeLocation(HashMap<String, Location> homes) {
        return homes.values()
                .stream()
                .findFirst()
                .orElse(null); // Get the home location
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command");
            return false;
        }
        // Get the list of homes for the player /home <subcommand> or /home <homeName>
        HashMap<String, Location> playerHomes = QuickSurvival.homes.get(player.getName());

        if (args.length == 0) {//If player sends /home


            if (playerHomes.isEmpty()) { // If the list is empty, send a message to the player
                player.sendMessage("You don't have any homes set");
                return false;
            }

            if (playerHomes.size() != 1) { // If the player has more than one home
                player.teleport(getHomeLocation(playerHomes)); // Teleport the player to the home
                return true;
            }
            //payer has only one home
            player.teleport(getHomeLocation(playerHomes));
            return true;
        }
        //todo: check if args[0] is named "set" or "delete" and teleport the player to the home with the name of args[0]

        // if player sends /home <subcommand> or /home <homeName>
        String subCommandName = args[0];
        CommandExecutor subCommand = subCommands.get(subCommandName);//check if the subcommand exists in the HashMap


        if (subCommand == null) {//If the subcommand does not exist in HashMap teleports the player to the home with the name of the subcommand
            if (playerHomes == null || playerHomes.isEmpty()) {
                player.sendMessage("You don't have any homes set");
                return false;
            }
            if (!playerHomes.containsKey(subCommandName)) {
                player.sendMessage("Home " + subCommandName + " does not exist");
                return false;
            }
            // Just debug
            // TextTools.sendMessage(player, Optional.of("Teleporting to home " + playerHomes.get(subCommandName).toString()), "", QuickSurvival.getPlugin(QuickSurvival.class));
            TextTools.sendMessage(player, "Teleporting to home " + subCommandName, "", QuickSurvival.getPlugin(QuickSurvival.class));
            player.teleport(playerHomes.get(subCommandName));
            return true;
        }


        if ((subCommandName.equalsIgnoreCase("set") || subCommandName.equalsIgnoreCase("delete")) && args.length == 1) {
            if (QuickSurvival.homes.get(player.getName()).containsKey(subCommandName)) {
                TextTools.sendMessage(player, "Teleporting to home " + subCommandName, "", QuickSurvival.getPlugin(QuickSurvival.class));
                player.teleport(playerHomes.get(subCommandName));
                return true;
            }
        }

        String[] subCommandArgs = new String[args.length - 1];
        // Copy the remaining arguments into the new array
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        // Execute the subcommand with the remaining arguments
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        // If the sender is not a player, return null
        if (!(sender instanceof Player)) return null;
        if (QuickSurvival.homes.isEmpty()) return null;

        // If the user has only typed the command but not the subcommand
        if (args.length == 1) {
            // Return a list of all available subcommands for auto-completion
            List<String> completions = new ArrayList<>(subCommands.keySet());
            if (!QuickSurvival.homes.get(sender.getName()).isEmpty()) {
                completions.addAll(QuickSurvival.homes.get(sender.getName()).keySet());
            }
            return completions;
        } else if (args.length > 1) {
            // If a subcommand has been typed
            // Retrieve the corresponding TabCompleter for the subcommand
            TabCompleter completer = subCommandCompleters.get(args[0]);
            // If the TabCompleter exists
            if (completer != null) {
                // Delegate to the TabCompleter to provide auto-completion options
                return completer.onTabComplete(sender, command, alias, args);
            }
        }
        // If no conditions are met, return null
        return null;
    }
}
