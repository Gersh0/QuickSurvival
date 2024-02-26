package co.com.cofees.events;

import co.com.cofees.tools.Keys;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class WaystoneInteract implements Listener {
    @EventHandler
    public void onPlayerWaystoneClick(PlayerInteractEvent e) throws EventException {
        if (e instanceof Cancellable && ((Cancellable) e).isCancelled()) {
            e.setCancelled(false);
        }

        Player p = e.getPlayer();
        Block b = null;

        if (p.getTargetBlockExact(5) != null) {
            b = p.getTargetBlockExact(5);
        } else {
            return;
        }

        if(!(b.getState() instanceof TileState))return;


        TileState tileState = (TileState) b.getState();


        PersistentDataContainer container = tileState.getPersistentDataContainer();

        if (e.getAction().name().contains("RIGHT") && container.has(Keys.WAYSTONE, PersistentDataType.STRING)) {
            p.sendMessage("menu abierto correctamente");
            openWaystoneInventory(p);

        } else {
            return;
        }
    }

    private void openWaystoneInventory(Player player) {
        // Crear el inventario del "Waystone"
        Inventory waystoneInventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&4WaystoneMenu"));

        // agregar elementos

        // Abrir el inventario para el jugador
        player.openInventory(waystoneInventory);

    }

}
