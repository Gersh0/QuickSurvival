package co.com.cofees.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ControlPanelListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals("Panel de control")) {
            event.setCancelled(true); //Cancel the event to avoid the player from taking the item
            Player player = (Player) event.getWhoClicked();

            if (event.getSlot() == 3) {
                changeWoolColor(player, ChatColor.RED, 3); //Red wool slot
            } else if (event.getSlot() == 5) {
                changeWoolColor(player, ChatColor.GREEN, 5); //Green wool slot
            }
        }
    }

    private void changeWoolColor(Player player, ChatColor color, int slot) {
        ItemStack wool = player.getOpenInventory().getItem(slot);
        if (wool != null) {
            ItemMeta meta = wool.getItemMeta(); // Get the item meta
            if (meta != null) {
                meta.setDisplayName(color + "Lana"); // Change the display name
                wool.setItemMeta(meta); // Set the new item meta
                player.getOpenInventory().getTopInventory().setItem(slot, wool); // Update the inventory
            }
        }
    }
}