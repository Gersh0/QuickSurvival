package co.com.cofees.commands;

import co.com.cofees.tools.ControlMenuGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ControlMenuCommand implements CommandExecutor {

    ControlMenuGUI guiManager = new ControlMenuGUI();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(sender instanceof Player){
            Player player  = (Player) sender;
            guiManager.openMainMenu(player);
            return true;
        }else{
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
            return false;
        }
    }
}
