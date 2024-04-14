package co.com.cofees.commands;

import co.com.cofees.tools.Keys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BackpackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by players.");
            return false;
        }

        ItemStack handItem = player.getInventory().getItemInMainHand();
        if (handItem.getItemMeta() == null) {
            player.sendMessage(ChatColor.RED + "You don't have an item in your hand.");
            return false;
        }
        PersistentDataContainer container = handItem.getItemMeta().getPersistentDataContainer();

        if (!container.has(Keys.BACKPACKLV1, PersistentDataType.STRING)) {
            player.sendMessage(ChatColor.RED + "You don't have a backpack in your hand.");
            return false;
        }

        openBackpackInventory(player);
        return true;
    }

    private void openBackpackInventory(Player player) {
        // Create the inventory
        Inventory backpackInventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&6Backpack"));

        // Open the inventory
        player.openInventory(backpackInventory);
    }
}