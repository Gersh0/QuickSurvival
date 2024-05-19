package co.com.cofees.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RegionAddPlayerCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player) || args.length == 1) {
            return null;
        }

        if (args.length > 1) {
            return sender.getServer().getOnlinePlayers().stream().map(Player::getName).toList();
        }

        sender.getServer().getLogger().warning("An error occurred while trying to tab complete the command");
        return null;
    }
}