
package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import co.com.cofees.tools.Waystone;
import co.com.cofees.tools.WaystoneMenuGui;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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

            String name = tileState.getPersistentDataContainer().get(Keys.WAYSTONE, PersistentDataType.STRING);
            //create the waystone object to add the player

            Waystone waystone = QuickSurvival.waystones.get(name);
            //check if the waysone is null


            // Just de
            if (waystone == null) {
                p.sendMessage("Waystone Fail: " + name);
            } else {
                p.sendMessage("Clicked Waystone Name: " + name);
            }


            if (!waystone.containsPlayer(p.getName())) {
                waystone.addPlayer(p.getName());
                //e.getItem().getItemMeta().getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, waystone.getName());
                WaystonePlacement.saveWaystone(waystone, QuickSurvival.waystonesConfig, name);

            } else {
                p.sendMessage("Ya estás en la lista de este waystone");
            }

            openWaystoneInventory(p);
            p.sendMessage("menu abierto correctamente");
        } else {
            return;
        }
    }

    //event that locks the manipulation of the items in the waystone menu
    @EventHandler
    public void onPlayerMenuInteraction(InventoryClickEvent e) throws EventException {
        if (!e.getView().getTitle().equalsIgnoreCase("WaystoneMenu")) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        e.setCancelled(true);
    }


    //event that if the player uses a warp scroll
    @EventHandler
    public void onPlayerWarpScrollUse(PlayerInteractEvent e) throws EventException {
        //guard clause that sees if the event is cancelled
        if (e instanceof Cancellable && ((Cancellable) e).isCancelled()) {
            e.setCancelled(false);
        }

        Player p = e.getPlayer();
        //guard Clause that sees if a tilestate block is being looked at
        if (isPlayerLookingAtTileState(p)) return;

        ItemStack item = p.getInventory().getItemInMainHand();
        //guard clause that sees if the item has a meta
        if (item.getItemMeta() == null) return;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (e.getAction().name().contains("RIGHT") && container.has(Keys.WARP_SCROLL, PersistentDataType.STRING)) {

            openWaystoneInventory(p);
            p.sendMessage("menu de warp scroll abierto correctamente");

        } else {
            return;
        }

    }


    //Event that comprobates if the player break a waystone
    @EventHandler
    public void onPlayerWaystoneBreak(BlockBreakEvent e) throws EventException {
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

        //search the info of the waystone and remove the waystone from the hashmap and the file
        if (container.has(Keys.WAYSTONE, PersistentDataType.STRING)) {
            String name = tileState.getPersistentDataContainer().get(Keys.WAYSTONE, PersistentDataType.STRING);
            Waystone waystone = QuickSurvival.waystones.get(name);
            if (waystone != null) {
                //remove the waystone from the hashmap and the file
                WaystonePlacement.removeWaystone(name);
                p.sendMessage("Waystone " + name + " has been removed");
            }
        }
    }

    @EventHandler
    public void onPlayerWaystoneMenuClick(InventoryClickEvent e) throws EventException {

        if (!e.getView().getTitle().equalsIgnoreCase("WaystoneMenu")) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }
        //get the name of the item who clicked the player
        ItemStack item = e.getCurrentItem();
        //guard clause that sees if the item is null
        if (item == null) return;
        ItemMeta itemMeta = item.getItemMeta();
        //guard clause that sees if the item has a meta
        if (itemMeta == null) return;

        String itemName = itemMeta.getDisplayName();
        //search the waystone in the hashmap
        Waystone waystone = QuickSurvival.waystones.get(itemName);
        //guard clause that sees if the waystone is null
        if (waystone == null) return;


        //comprobate if the player clicked "Left"(Teleport) or "Right"(open OptionMenu)

        if (e.getClick().isRightClick()) {
            //open the waystone menu
            WaystoneMenuGui.openWaystoneOptionMenu((Player) e.getWhoClicked(), waystone);
            //send message to player
            e.getWhoClicked().sendMessage("Menu de " + ChatColor.GOLD + " " + waystone.getName() + " " + ChatColor.RESET + "abierto correctamente");
            return;
        } else if (e.getClick().isLeftClick()) {
            //teleport the player to the waystone

            teleportPlayer((Player) e.getWhoClicked(), waystone);
            //send message to player
            e.getWhoClicked().sendMessage("Teleporting to " + ChatColor.GOLD + " " + waystone.getName());

        }
    }


    public static void openWaystoneInventory(Player player) {
        // Crear el inventario del "Waystone"
        Inventory waystoneInventory = Bukkit.createInventory(null, 54, "WaystoneMenu");


        // Cargar los elementos del inventario
        Queue<ItemStack> itemStacks = loadWaystoneItems(player);

        // Agregar los elementos al inventario
        itemStacks.forEach(waystoneInventory::addItem);
        //send message to player
        player.sendMessage("Inventario cargado correctamente");
        // Abrir el inventario para el jugador
        player.openInventory(waystoneInventory);


    }

    private void teleportPlayer(Player player, Waystone waystone) {
        //guard clause that sees if the waystone is null
        if (waystone == null) return;

        consumeWarpScroll(player);
        //teleport the player to the waystone location
        player.teleport(waystone.getLocation());
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_LAUNCH, 1.0f, 1.0f);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);

        //make a particle effect intense to the player

        player.spawnParticle(org.bukkit.Particle.PORTAL, player.getLocation(), 100, 0.5, 1, 0.5, 0.1);
        player.spawnParticle(org.bukkit.Particle.END_ROD, player.getLocation(), 100, 0.5, 1, 0.5, 0.1);
        player.spawnParticle(org.bukkit.Particle.PORTAL, player.getLocation(), 100, 0.5, 1, 0.5, 0.1);

    }

    //method that consumes the warpscroll
    private void consumeWarpScroll(Player player) {
        //guard clause that sees if the player is looking at a tilestate block
        if (isPlayerLookingAtTileState(player)) return;

        //see if the player is in creative mode
        if (player.getGameMode().name().contains("CREATIVE")) return;

        //get the item in the main hand
        ItemStack item = player.getInventory().getItemInMainHand();
        //guard clause that sees if the item is null
        if (item == null) return;
        //get the item meta
        ItemMeta meta = item.getItemMeta();
        //guard clause that sees if the item meta is null
        if (meta == null) return;
        //get the container
        PersistentDataContainer container = meta.getPersistentDataContainer();
        //guard clause that sees if the container has the warp scroll key
        if (container.has(Keys.WARP_SCROLL, PersistentDataType.STRING)) {

            //remove the item from the inventory
            player.getInventory().getItemInMainHand().setAmount(item.getAmount() - 1);
        }
    }


    private static Queue<ItemStack> loadWaystoneItems(Player player) {
        // Cargar los elementos del inventario
        Queue<ItemStack> itemStacks = new LinkedList<>();

        HashMap<String, Waystone> waystoneHashMap = QuickSurvival.waystones;

        waystoneHashMap.keySet().forEach(waystoneName -> {
            Waystone waystone = waystoneHashMap.get(waystoneName);
            if (waystone.containsPlayer(player.getName())) {
                ItemStack icon = waystone.getIcon();
                itemStacks.add(icon);
            }
        });

        return itemStacks;
    }

    //method that sees if a player is looking a tilestate block
    private boolean isPlayerLookingAtTileState(Player p) {
        Block b = p.getTargetBlockExact(5);
        return b != null && b.getState() instanceof TileState;
    }

//


}
