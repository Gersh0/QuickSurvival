package co.com.cofees.commands.subcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HealCommand implements CommandExecutor {
    private final Map<String, CommandExecutor> subCommands = new HashMap<>();

    public HealCommand() {
        // Add more subcommands as needed
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Optional<Player> player = getPlayer(sender);
        if (args.length == 0) {
            if (player.isPresent()) {
                player.get().sendMessage("You have been healed!");
                sender.getServer().getLogger().info(player.get().getName() + " healed!");
                player.get().setHealth(20.0);
                return true;
            } else {
                sender.getServer().getLogger().warning("Only players can use this command.");
                return false; // Handle the case where no subcommand is provided
            }
        }

        if (args.length == 1) {
            player = getPlayer(getOnlinePlayerByNickname(sender, args[0].toLowerCase()));
            if (player.isPresent()) {
                sender.getServer().getLogger().info(player.get().getName() + " healed!");
                player.get().setHealth(20);
                return true;
            }else{
                sender.getServer().getLogger().warning(args[0] + " is not online!");
                return false;
            }
        }

        String subCommandName = args[0];
        CommandExecutor subCommand = subCommands.get(subCommandName);

        if (subCommand == null) {
            sender.getServer().getLogger().warning("Argument doesn't exists.");
            return false; // Handle the case where the subcommand does not exist
        }

        // Execute the subcommand, passing the remaining arguments
        String[] subCommandArgs = new String[args.length - 1];
        System.arraycopy(args, 1, subCommandArgs, 0, subCommandArgs.length);
        return subCommand.onCommand(sender, command, label, subCommandArgs);
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
