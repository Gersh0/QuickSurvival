package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TextTools {
    public static String coloredText(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static boolean sendMessage(Object sender, String playerMessage, String otherMessage, QuickSurvival core) {
        if (sender instanceof Player player) {
            player.sendMessage(playerMessage != null ? playerMessage : otherMessage);
            return true;
        } else {
            core.getLogger().info(otherMessage);
            return false;
        }
    }
}