package co.com.cofees.commands.subcommands;

import co.com.cofees.tools.TextTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class FlyCommand implements CommandExecutor {

    public FlyCommand() {
        // Add more subcommands as needed
    }

    public String text(String text) {
        return TextTools.coloredText(text);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {

        if ((!(sender instanceof Player)) && args.length == 0) {
            sender.getServer().getLogger().warning("Can't use this command from console.");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.getServer().getLogger().info("Fly mode enabled for " + args[0]);
            return toggleFly(Objects.requireNonNull(sender.getServer().getPlayer(args[0])));
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            return toggleFly(player);
        }

        if (sender.getServer().getOnlinePlayers().stream()
                .map(Player::getName)
                .noneMatch(name -> name.equalsIgnoreCase(args[0]))) {
            sender.sendMessage("Hi");

            return false;
        }
        sender.sendMessage("Fly mode toggled for " + args[0]);
        toggleFly(Objects.requireNonNull(sender.getServer().getPlayer(args[0])));

        return false;
    }

    public boolean toggleFly(Player player) {
        if (!player.getAllowFlight()) {
            player.sendMessage(text("&bFly mode enabled for " + player.getName()));
            player.setAllowFlight(true);
            return true;
        }
        player.sendMessage(text("&bFly mode disabled for " + player.getName()));
        player.setAllowFlight(false);
        return true;
    }
}
