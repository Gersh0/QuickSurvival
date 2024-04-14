package co.com.cofees.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class HealCommand implements CommandExecutor {

    public HealCommand() {
        // Add more subcommands as needed
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Optional<Player> player = getPlayer(sender);

        if (args.length == 0) {
            if (player.isEmpty()) {
                sender.getServer().getLogger().warning("Only players can use this command.");
                return false;
            }
            healPlayer(player.get(), sender);
            return true;
        }

        if (args.length == 1) {
            player = getPlayer(getOnlinePlayerByNickname(sender, args[0].toLowerCase()));
            if (player.isEmpty()) {
                sender.getServer().getLogger().warning(args[0] + " is not online!");
                return false;
            }
            healPlayer(player.get(), sender);
            return true;
        }

        return false;
    }

    private void healPlayer(Player player, CommandSender sender) {
        player.sendMessage("You have been healed!");
        sender.getServer().getLogger().info(player.getName() + " healed!");
        player.setHealth(20);
    }


    private Optional<Player> getPlayer(Object sender) {
        return (sender instanceof Player) ? Optional.of((Player) sender) : Optional.empty();
    }

    public Player getOnlinePlayerByNickname(CommandSender sender, String nickname) {
        return sender.getServer().getOnlinePlayers().stream()
                .filter(player -> player.getName().equalsIgnoreCase(nickname))
                .findFirst()
                .orElse(null);
    }
}
