package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
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

public class ControlPanelCommmand implements CommandExecutor, Listener {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            openControlPanel(player);
            return true;
        } else {
            sender.sendMessage("Este comando solo puede ser ejecutado por un jugador.");
            return false;
        }
    }

    private void openControlPanel(Player player) {
        // Crear el inventario
        Inventory panelInventory = Bukkit.createInventory(null, 9, "Panel de control");

        // Crear los ítems de lana roja y verde
        ItemStack lanaRoja = new ItemStack(Material.RED_WOOL, 1, (short) 14);  // Lana roja
        ItemStack lanaVerde = new ItemStack(Material.GREEN_WOOL, 1, (short) 5);   // Lana verde

        // Configurar los ítems
        ItemMeta metaRoja = lanaRoja.getItemMeta();
        metaRoja.setDisplayName(ChatColor.RED + "Lana Roja");
        lanaRoja.setItemMeta(metaRoja);

        ItemMeta metaVerde = lanaVerde.getItemMeta();
        metaVerde.setDisplayName(ChatColor.GREEN + "Lana Verde");
        lanaVerde.setItemMeta(metaVerde);

        // Añadir los ítems al inventario
        panelInventory.setItem(3, lanaRoja);
        panelInventory.setItem(5, lanaVerde);

        // Abrir el inventario para el jugador
        player.openInventory(panelInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Panel de control")) {
            event.setCancelled(true);
        }
    }


}
