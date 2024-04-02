package co.com.cofees.commands.subcommands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.LocationHandler;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SetHomeCommand implements CommandExecutor{
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

        // Set the home
        if (commandSender instanceof Player player) {
            HashMap<String, Location> playerHomes = QuickSurvival.homes.isEmpty() ? new HashMap<>() : QuickSurvival.homes.get(player.getName());
            Location home = player.getLocation();
            playerHomes.put(args[0], home);
            String path = player.getName() + "." + args[0];
            LocationHandler.serializeLocation(home, QuickSurvival.homesConfig, path);
            QuickSurvival.homes.put(player.getName(), playerHomes);
            try {
                QuickSurvival.homesConfig.save(new File(QuickSurvival.getPlugin(QuickSurvival.class).getDataFolder(), "homes.yml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            commandSender.sendMessage("Home set");
            return true;
        }

        return false;
    }
}
