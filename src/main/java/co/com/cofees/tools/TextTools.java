package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Optional;

public class TextTools {
    public static String coloredText(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    private static Optional<Player> getPlayer(Object sender) {
        return (sender instanceof Player) ? Optional.of((Player) sender) : Optional.empty();
    }
    public static boolean sendMessage(Object sender, Optional<String> playerMessage, String otherMessage, QuickSurvival core) {
        Optional<Player> player = getPlayer(sender);
        if (player.isPresent()) {
            player.get().sendMessage(playerMessage.orElse(otherMessage));
            return true;
        } else {
            core.getLogger().info(otherMessage);
            return false;
        }
    }
}

