package co.com.cofees.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HealCompleter implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (!(sender instanceof Player)) return null; //Clauses the case if is not a player.

        if (args.length == 1) {
            return null;

        } else if (args.length > 1) {
            return sender.getServer().getOnlinePlayers()
                    .stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
