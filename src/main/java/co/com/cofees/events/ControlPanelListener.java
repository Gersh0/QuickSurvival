package co.com.cofees.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ControlPanelListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Panel de control")) {
            event.setCancelled(true); // Cancelar la interacción
            Player player = (Player) event.getWhoClicked();

            if (event.getSlot() == 3) {
                changeWoolColor(player, ChatColor.RED, 3); // Slot de la lana roja
            } else if (event.getSlot() == 5) {
                changeWoolColor(player, ChatColor.GREEN, 5); // Slot de la lana verde
            }
        }
    }

    private void changeWoolColor(Player player, ChatColor color, int slot) {
        ItemStack wool = player.getOpenInventory().getItem(slot);
        ItemMeta meta = wool.getItemMeta();
        meta.setDisplayName(color + "Lana");
        wool.setItemMeta(meta);
        player.updateInventory();
    }
}
