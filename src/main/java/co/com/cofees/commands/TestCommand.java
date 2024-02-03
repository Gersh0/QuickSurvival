package co.com.cofees.commands;

import co.com.cofees.events.VacaNagasaki;
import co.com.cofees.tools.TextTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        //Issued from console
        if (!(sender instanceof Player)) {
            sender.sendMessage(text("&4Can't use this command from console!"));
            return true;
        }

        //Player issued command
        Player player = (Player) sender;
        if (args.length <= 0) {
            player.sendMessage(text("&bTest Successful."));
            return true;
        }

        switch (args[0]) {
            case "help":
                player.sendMessage(text("&b&kiii&rHelp menu&b&kiii"));
                break;
            case "xp":
                if(!player.getAllowFlight()){
                    player.sendMessage(text("&bFlight mode enabled for " + String.valueOf(player.getName())));
                    player.setAllowFlight(true);
                    break;
                }
                player.sendMessage(text("&bFlight mode disabled for " + String.valueOf(player.getName())));
                player.setAllowFlight(false);
                break;
            default:
                noArgs(player);
        }

        return true;
    }

    public void noArgs(CommandSender sender) {
        sender.sendMessage(text("&4&lUse &r/test help &4&lfor usage of command"));
    }

    public String text(String text) {
        return TextTools.coloredText(text);
    }
}
