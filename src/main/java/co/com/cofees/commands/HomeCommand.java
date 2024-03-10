package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.commands.subcommands.DeleteHomeCommand;
import co.com.cofees.commands.subcommands.SetHomeCommand;
import co.com.cofees.completers.DeleteHomeCompleter;
import co.com.cofees.completers.SetHomeCompleter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeCommand implements CommandExecutor, TabCompleter {

    private final Map<String, CommandExecutor> subCommands = new HashMap<>();
    private final Map<String, TabCompleter> subCommandCompleter = new HashMap<>();
    private final QuickSurvival core;
    private final YamlConfiguration homesConfig;

    public HomeCommand(QuickSurvival core, YamlConfiguration homesConfig) {
        this.core = core;
        this.homesConfig = homesConfig;
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
        HashMap<String, Location> homes = getHome(player);

        if (args.length == 0) {//If player sends /home


            if (homes.isEmpty()) { // If the list is empty, send a message to the player
                player.sendMessage("You don't have any homes set");
                return false;
            }

            if (homes.size() != 1) { // If the player has more than one home
                player.sendMessage("You have more than one home set. Please specify which one you want to go to.");
                return false;
            }

            // If the player has only one home
            Location home = homes.values()
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
            player.teleport(homes.get(subCommandName));
            return true;
        }

        String[] subCommandArgs = new String[args.length - 1];
        // Copy the remaining arguments into the new array
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        // Execute the subcommand with the remaining arguments
        return subCommand.onCommand(sender, command, label, subCommandArgs);
    }

    public HashMap<String, Location> getHome(Player player) {
        homesConfig.getKeys(true);

        return null;
    }

    public World buildWorld(Player player) {
        String expectedWorld = homesConfig.getString("homes." + player.getName() + ".world");
        return player.getServer()
                .getWorlds()
                .stream()
                .filter(w -> w.getName().equalsIgnoreCase(expectedWorld)).findFirst().orElse(null);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}