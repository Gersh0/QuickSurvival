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

import java.util.*;

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

        if (playerHomes == null || playerHomes.isEmpty()) { // Guard clause if the player has no homes
            player.sendMessage("You don't have any homes set");
            return false;
        }

        if (args.length == 0) {// Send the player to the first home if no arguments are provided
            player.teleport(getHomeLocation(playerHomes));
            return true;
        }
        // If player sends /home <subcommand> or /home <homeName>
        String subCommandName = args[0];// Get the possible subcommand name, it may also be a home name
        CommandExecutor subCommand = subCommands.get(subCommandName);//Try to get the subcommand, if it exists it will not be null

        if (subCommand == null) {// If the subcommand does not exist, it may be a home name or an invalid command
            if (!playerHomes.containsKey(subCommandName)) {
                player.sendMessage("Home " + subCommandName + " does not exist or is an invalid command");
                return false;
            }
            // Teleport the player to the home location after checking that it exists
            TextTools.sendMessage(player, "Teleporting to home " + subCommandName, "", QuickSurvival.getPlugin(QuickSurvival.class));
            player.teleport(playerHomes.get(subCommandName));
            return true;
        }

        // At this point, the subcommand exists, so we will execute it calling its onCommand method and passing the arguments
        String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);// Remove the subcommand from the arguments
        return subCommand.onCommand(sender, command, label, subCommandArgs);// Call the onCommand method of the subcommand
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player) || QuickSurvival.homes.isEmpty()) return null;
        // Get the list of homes for the player
        List<String> completions = new ArrayList<>(subCommands.keySet());

        // And add the home names to the completions list, if the player has any homes
        if (!QuickSurvival.homes.get(sender.getName()).isEmpty()) {
            completions.addAll(QuickSurvival.homes.get(sender.getName()).keySet());
        }

        // Now we will check if the player has entered a subcommand and if so, we will call the tab completer of that subcommand
        if (args.length > 1) {
            TabCompleter completer = subCommandCompleters.get(args[0]); // Try to get the tab completer of the subcommand
            if (completer != null) {
                return completer.onTabComplete(sender, command, alias, args); // Call the tab completer of the subcommand
            }
        }

        // If the player has not entered a subcommand, we will return the completions list
        return completions;
    }
}
