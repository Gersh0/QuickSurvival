package co.com.cofees.tools;

import org.bukkit.ChatColor;

public class TextTools {
    public static String coloredText(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
