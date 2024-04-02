package co.com.cofees.completers;

import co.com.cofees.QuickSurvival;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class DeleteHomeCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null; //Clauses the case if is not a player.


        if (args.length == 1) {
            return null;

        } else if (args.length > 1) {
            ConfigurationSection section = QuickSurvival.homesConfig.getConfigurationSection(sender.getName());
            return (section != null) ? section.getKeys(false).stream().toList() : Collections.emptyList();

        }
        sender.getServer().getLogger().warning("An error occurred while trying to tab complete the command");
        return null;
    }

}
