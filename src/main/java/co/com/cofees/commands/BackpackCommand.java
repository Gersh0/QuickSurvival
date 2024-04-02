package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // Verificar si el jugador tiene la mochila en la mano
            ItemStack handItem = player.getInventory().getItemInMainHand();
            PersistentDataContainer container = handItem.getItemMeta().getPersistentDataContainer();

            if (container.has(Keys.BACKPACKLV1, PersistentDataType.STRING)) {
                // Abrir el inventario de la mochila
                openBackpackInventory(player);
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "No tienes una mochila en la mano.");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Este comando solo puede ser ejecutado por jugadores.");
        }
        return false;
    }

    private void openBackpackInventory(Player player) {
        // Crear el inventario de la mochila
        Inventory backpackInventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', "&6Mochila"));

        // Agregar elementos al inventario si es necesario

        // Abrir el inventario para el jugador
        player.openInventory(backpackInventory);
    }
}