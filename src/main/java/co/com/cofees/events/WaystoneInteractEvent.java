package co.com.cofees.events;


import org.bukkit.*;

import org.bukkit.block.Block;
import org.bukkit.entity.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import net.md_5.bungee.api.ChatColor;

public class WaystoneInteractEvent implements Listener {

    private static final String WAYSTONE_KEY = "Waystone";


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();


            if (clickedBlock != null && isWaystoneBlock(clickedBlock)) {
                openWaystoneInventory(event.getPlayer());
            }
        }
    }

    //CUIDADO NO ESTA LISTO ESTE METODO
    private boolean isWaystoneBlock(Block block) {

        // Verifica si el bloque tiene los metadatos de "Waystone"

        return block.hasMetadata(WAYSTONE_KEY);


    }

    private void openWaystoneInventory(Player player) {
        // Crear el inventario del "Waystone"
        Inventory waystoneInventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&6Inventario Waystone"));

        // Puedes agregar elementos u opciones al inventario según sea necesario

        // Abrir el inventario para el jugador
        player.openInventory(waystoneInventory);
    }

}
