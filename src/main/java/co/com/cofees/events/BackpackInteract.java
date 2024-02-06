package co.com.cofees.events;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BackpackInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        // Verificar si el jugador interactuó con un "Waystone"
        if (isBackpack(handItem)) {
            // Abrir el inventario del "Waystone"
            openBackpackInventory(player);
            event.setCancelled(true); // Cancelar la interacción original
        }
    }

    private boolean isBackpack(ItemStack item) {
        // Verificar si el ítem es un "Waystone" (puedes ajustar esto según tus metadatos u otras características)
        return item.getType() == Material.BEACON && ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase("Mochila");
    }

    private void openBackpackInventory(Player player) {
        // Crear el inventario del "Waystone"
        Inventory waystoneInventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&6Mochila"));

        // agregar elementos

        // Abrir el inventario para el jugador
        player.openInventory(waystoneInventory);
    }
}
