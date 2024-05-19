package co.com.cofees.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ControlPanelListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Panel de control")) return;

        event.setCancelled(true); // Cancelar la interacción
        Player player = (Player) event.getWhoClicked();

        if (event.getSlot() == 3) {
            changeWoolColor(player, ChatColor.RED);
        } else if (event.getSlot() == 5) {
            changeWoolColor(player, ChatColor.GREEN);
        }
    }

    private void changeWoolColor(Player player, ChatColor color) {
        var wool = player.getOpenInventory().getItem(color == ChatColor.RED ? 3 : 5);
        var meta = wool.getItemMeta();
        meta.setDisplayName(color + "Lana");
        wool.setItemMeta(meta);
        player.updateInventory();
    }
}