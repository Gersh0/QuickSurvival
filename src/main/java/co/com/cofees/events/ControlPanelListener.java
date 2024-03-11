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

           changeWoolColor(player, event.getSlot());
        }
    }

    public boolean isActivated (ChatColor color) {
        return color == ChatColor.GREEN;
    }


    private void changeWoolColor(Player player, int slot) {
        Inventory inventory = player.getOpenInventory().getTopInventory();

        // Verificar si el slot está en el rango del inventario

            ItemStack clickedItem = inventory.getItem(slot);

            if (clickedItem != null && clickedItem.getType() == Material.GREEN_WOOL) {
                player.sendMessage("Lana verde clickeada");
                player.sendMessage(clickedItem.getType().toString());
                // Cambiar lana verde a roja
                ItemStack newWool = new ItemStack(Material.RED_WOOL, 1);
                ItemMeta meta = newWool.getItemMeta();
                meta.setDisplayName(ChatColor.RED + "Lana Desactivada");
                newWool.setItemMeta(meta);

                inventory.setItem(slot, newWool);
                player.updateInventory();
            } else {
                player.sendMessage("Lana roja clickeada");
                // Cambiar lana roja a verde
                ItemStack newWool = new ItemStack(Material.GREEN_WOOL, 1);
                ItemMeta meta = newWool.getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Lana Activada");
                newWool.setItemMeta(meta);

                inventory.setItem(slot, newWool);
                player.updateInventory();
            }

        }
    }



