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
import java.util.Optional;

public class DeleteHomeCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) || args.length == 1) {
            return null;
        }

        if (args.length > 1) {
            return Optional.ofNullable(QuickSurvival.homesConfig.getConfigurationSection(sender.getName()))
                    .map(section -> section.getKeys(false).stream().toList())
                    .orElse(Collections.emptyList());
        }

        sender.getServer().getLogger().warning("An error occurred while trying to tab complete the command");
        return null;
    }
}