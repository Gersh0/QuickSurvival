package co.com.cofees.events;

import co.com.cofees.tools.Keys;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BackpackInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {


        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        player.sendMessage("Objeto:" + handItem.getItemMeta().getPersistentDataContainer().getKeys());


        PersistentDataContainer container = handItem.getItemMeta().getPersistentDataContainer();

        // Verificar si el jugador interactuó con un "Backpack lv1"
        if (event.getAction().name().contains("RIGHT")) {
            if (container.has(Keys.BACKPACKLV1, PersistentDataType.STRING)) {
                // Abrir el inventario de la mochila
                openBackpackInventory(player, 1);
                event.setCancelled(true);
            } else if (container.has(Keys.BACKPACKLV2, PersistentDataType.STRING)) {
                openBackpackInventory(player, 2);
                event.setCancelled(true);
            } else if (container.has(Keys.BACKPACKLV3, PersistentDataType.STRING)) {
                openBackpackInventory(player, 3);
                event.setCancelled(true);
            }else if (container.has(Keys.BACKPACKLV4, PersistentDataType.STRING)) {
                // Abrir el inventario de la mochila
                openBackpackInventory(player, 4);
                event.setCancelled(true);
            }else if (container.has(Keys.BACKPACKLV5, PersistentDataType.STRING)) {
                // Abrir el inventario de la mochila
                openBackpackInventory(player, 6);
                event.setCancelled(true);
            }


        }
    }


    private void openBackpackInventory(Player player, int backpackLevel) {
        int inventorySize = 9 * backpackLevel;

        // Crear el inventario de la mochila
        Inventory backpackInventory = Bukkit.createInventory(null, inventorySize, ChatColor.translateAlternateColorCodes('&', "&6Mochila Nivel " + backpackLevel));


        // Duplicar el inventario si el nivel es mayor que 1
        if (backpackLevel > 1) {
            Inventory duplicateInventory = Bukkit.createInventory(null, inventorySize, ChatColor.translateAlternateColorCodes('&', "&6Mochila Nivel " + backpackLevel));
            duplicateInventory.setContents(backpackInventory.getContents());
            player.openInventory(duplicateInventory);
        } else {
            // Abrir el inventario para el jugador
            player.openInventory(backpackInventory);

        }
        player.sendMessage(org.bukkit.ChatColor.GREEN+"Mochila abierta");
    }

}
