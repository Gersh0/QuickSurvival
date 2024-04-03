
package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import co.com.cofees.tools.Waystone;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

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

        if (!(b.getState() instanceof TileState)) return;


        TileState tileState = (TileState) b.getState();


        PersistentDataContainer container = tileState.getPersistentDataContainer();

        if (e.getAction().name().contains("RIGHT") && container.has(Keys.WAYSTONE, PersistentDataType.STRING)) {
            openWaystoneInventory(p);
            p.sendMessage("menu abierto correctamente");
        } else {
            return;
        }
    }

    private void openWaystoneInventory(Player player) {
        // Crear el inventario del "Waystone"
        Inventory waystoneInventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&4WaystoneMenu"));
        InventoryHolder waystoneHolder = waystoneInventory.getHolder();

        // Cargar los elementos del inventario
        Queue<ItemStack> itemStacks = loadWaystoneItems(player);

        // Agregar los elementos al inventario
        itemStacks.forEach(waystoneInventory::addItem);
        //send message to player
        player.sendMessage("Inventario cargado correctamente");
        // Abrir el inventario para el jugador
        player.openInventory(waystoneInventory);


    }


    private Queue<ItemStack> loadWaystoneItems(Player player) {
        // Cargar los elementos del inventario
        Queue<ItemStack> itemStacks = new LinkedList<>();

        HashMap<String, Waystone> waystoneHashMap = QuickSurvival.waystones;

        waystoneHashMap.keySet().forEach(waystoneName -> {
            Waystone waystone = waystoneHashMap.get(waystoneName);
            if (waystone.containsPlayer(player.getName())) {
                ItemStack icon = waystone.getIcon();
                ItemMeta meta = icon.getItemMeta();
                assert meta != null;
                meta.setDisplayName(waystone.getName());
                icon.setItemMeta(meta);
                itemStacks.add(icon);
            }
        });

        return itemStacks;
    }

//


}
