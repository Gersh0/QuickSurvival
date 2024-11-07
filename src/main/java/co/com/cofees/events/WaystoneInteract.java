
package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import co.com.cofees.tools.Waystone;
import co.com.cofees.tools.WaystoneMenuGui;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class WaystoneInteract implements Listener {

    @EventHandler
    public void onPlayerWaystoneClick(PlayerInteractEvent e) {
        if (e == null) return;
        //guard clause that sees if the event is cancelled
        if (((Cancellable) e).isCancelled()) {
            ((Cancellable) e).setCancelled(false);
        }

        Player p = e.getPlayer();
        Block b;

        if (p.getTargetBlockExact(5) == null) return;

        b = p.getTargetBlockExact(5);

        if (b == null) return;

        if (!(b.getState() instanceof TileState tileState)) return;

        PersistentDataContainer container = tileState.getPersistentDataContainer();

        // All the logic to add the player to the waystone when right-clicking
        if (e.getAction().name().contains("RIGHT") && container.has(Keys.WAYSTONE, PersistentDataType.STRING)) {

            String name = tileState.getPersistentDataContainer().get(Keys.WAYSTONE, PersistentDataType.STRING);
            //create the waystone object to add the player

            Waystone waystone = QuickSurvival.waystones.get(name);
            //check if the waysone is null


            // Just debugging
            {
                if (waystone == null) {
                    p.sendMessage("Waystone Fail: " + name);
                    return;
                }
                p.sendMessage("Clicked Waystone Name: " + name);
            }

            if (!waystone.containsPlayer(p.getName())) {
                waystone.addPlayer(p.getName());
                //e.getItem().getItemMeta().getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, waystone.getName());
                WaystonePlacement.saveWaystone(waystone, QuickSurvival.waystonesConfig, name);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            } else {
                p.sendMessage("Ya estás en la lista de este waystone");
            }

            openWaystoneInventory(p);
            p.sendMessage("menu abierto correctamente");
        }
    }

    //event that locks the manipulation of the items in the waystone menu
    @EventHandler
    public void onPlayerMenuInteraction(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("WaystoneMenu")) return;

        if (e.getClickedInventory() == null) return;

        e.setCancelled(true);
    }


    //event that if the player uses a warp scroll
    @EventHandler
    public void onPlayerWarpScrollUse(PlayerInteractEvent e) {
        if (e == null) return;
        //guard clause that sees if the event is cancelled
        if (((Cancellable) e).isCancelled()) {
            ((Cancellable) e).setCancelled(false);
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
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);

        }
    }

    //Event to check if the player break a waystone
    @EventHandler
    public void onPlayerWaystoneBreak(BlockBreakEvent e) {
        if (e == null) return;
        if (((Cancellable) e).isCancelled()) {
            ((Cancellable) e).setCancelled(false);
        }

        Player p = e.getPlayer();

        if (p.getTargetBlockExact(5) == null) return;

        Block b = p.getTargetBlockExact(5);

        if (b == null) return;

        if (!(b.getState() instanceof TileState tileState)) return;

        PersistentDataContainer container = tileState.getPersistentDataContainer();

        //search the info of the waystone and remove the waystone from the hashmap and the file
        if (!container.has(Keys.WAYSTONE, PersistentDataType.STRING)) return;

        String name = container.get(Keys.WAYSTONE, PersistentDataType.STRING);
        Waystone waystone = QuickSurvival.waystones.get(name);

        if (waystone == null) return;

        WaystonePlacement.removeWaystone(name);
        p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0f, 1.0f);
        p.sendMessage("Waystone " + name + " has been removed");
    }

    @EventHandler
    public void onPlayerWaystoneMenuClick(InventoryClickEvent e) {

        //guard clause that sees if the event is cancelled and if the clicked inventory is null
        if ((!e.getView().getTitle().equalsIgnoreCase("WaystoneMenu")) || (e.getClickedInventory() == null)) return;

        ItemStack item = e.getCurrentItem();//get the name of the item who clicked the player

        //guard clause that sees if the item is null
        if (item == null) return;

        ItemMeta itemMeta = item.getItemMeta();//get the item meta

        if (itemMeta == null) return;//guard clause that sees if the item has a meta

        String itemName = itemMeta.getDisplayName();//get the name of the item
        Waystone waystone = QuickSurvival.waystones.get(itemName);//search the waystone in the hashmap

        if (waystone == null) return;//guard clause that sees if the waystone is null

        //check if the player clicked "Left"(Teleport) or "Right"(open OptionMenu)
        if (e.getClick().isRightClick()) {
            //get the item in main hand to see if is a warp scroll
            ItemStack itemInHand = e.getWhoClicked().getInventory().getItemInMainHand();
            //get the item meta
            ItemMeta meta = itemInHand.getItemMeta();
            if (meta != null) {
                //get the container
                PersistentDataContainer container = meta.getPersistentDataContainer();
                //guard clause that sees if the container has the warp scroll key
                if (container.has(Keys.WARP_SCROLL, PersistentDataType.STRING) && !isPlayerLookingAtTileState((Player) e.getWhoClicked()))
                    return;
            }
            //open the waystone menu

            WaystoneMenuGui.openWaystoneOptionMenu((Player) e.getWhoClicked(), waystone);
            //send message to player
            e.getWhoClicked().sendMessage("Menu de " + ChatColor.GOLD + " " + waystone.getName() + " " + ChatColor.RESET + "Successfully opened");
            return;
        }

        if (e.getClick().isLeftClick()) {
            //teleport the player to the waystone
            teleportPlayer((Player) e.getWhoClicked(), waystone);
            //send message to player
            e.getWhoClicked().sendMessage("Teleporting to " + ChatColor.GOLD + " " + waystone.getName());
        }
    }


    public static void openWaystoneInventory(Player player) {
        // Create the inventory
        Inventory waystoneInventory = Bukkit.createInventory(null, 54, "WaystoneMenu");
        Queue<ItemStack> itemStacks = loadWaystoneItems(player);// Load the items
        // Add the items to the inventory
        itemStacks.forEach(waystoneInventory::addItem);
        //send message to player
        player.sendMessage("Inventory loaded successfully");
        // Open the inventory
        player.openInventory(waystoneInventory);
    }

    private void teleportPlayer(Player player, Waystone waystone) {
        //guard clause that sees if the waystone is null
        if (waystone == null) return;

        consumeWarpScroll(player);
        //teleport the player to the waystone location
        player.teleport(waystone.getLocation());

        player.playSound(player.getLocation(), Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1.0f, 1.0f);
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.75f, 2.0f);
        player.playSound(player.getLocation(), Sound.BLOCK_PORTAL_AMBIENT, 1.2f, 1.0f);


        generateParticleEffect(player);

    }

    //method that consumes the warpscroll
    private void consumeWarpScroll(Player player) {
        //guard clause that sees if the player is looking at a tilestate block or if the player is in creative mode
        if (isPlayerLookingAtTileState(player) || player.getGameMode().name().contains("CREATIVE")) return;

        PlayerInventory inventory = player.getInventory();
        //get the item in the main hand
        ItemStack item = inventory.getItemInMainHand();
        //guard clause to check if the player has no item in the main hand
        if (item.getType().equals(Material.AIR)) return;
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
        //Load the items in the inventory
        Queue<ItemStack> itemStacks = new LinkedList<>();

        HashMap<String, Waystone> waystoneHashMap = QuickSurvival.waystones;

        waystoneHashMap.keySet().forEach(waystoneName -> {
            Waystone waystone = waystoneHashMap.get(waystoneName);
            if (waystone.containsPlayer(player.getName())) {
                ItemStack icon = waystone.getIcon();
                ItemMeta meta = icon.getItemMeta();
                if (meta != null) {
                    meta.setLore(Arrays.asList("Right click to open the menu", "Left click to teleport"));
                    if (isCurrentWaystone(player, waystone)) {
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        meta.addEnchant(Enchantment.DURABILITY, 1, true);
                    } else {
                        meta.removeEnchant(Enchantment.DURABILITY);
                    }
                    icon.setItemMeta(meta);
                    itemStacks.add(icon);
                }
            }
        });

        return itemStacks;
    }

    //method that sees if a player is looking a tilestate block
    private boolean isPlayerLookingAtTileState(Player p) {
        Block b = p.getTargetBlockExact(5);
        return b != null && b.getState() instanceof TileState;
    }

    //method that enchants the waystone if is the current waystone
    private static Boolean isCurrentWaystone(Player player, Waystone waystone) {
        //get the player target block
        Block block = player.getTargetBlockExact(5);
        if (block == null) return false;
        //get the block state
        BlockState blockState = block.getState();
        //get the tile state
        if (!(blockState instanceof TileState tileState)) return false;

        //get the persistent data container
        PersistentDataContainer container = tileState.getPersistentDataContainer();

        //get the waystone name
        String waystoneName = waystone.getName();
        //compare the waystone name with the block name
        if (!container.has(Keys.WAYSTONE, PersistentDataType.STRING)) return false;

        String containerWaystoneName = container.get(Keys.WAYSTONE, PersistentDataType.STRING);
        return containerWaystoneName != null && containerWaystoneName.equals(waystoneName);
    }

    //method to generate a plarticle effect
    private void generateParticleEffect(Player player) {

        // Execute the task every tick (20 times per second)
        new BukkitRunnable() {
            double t = 0; // Variable to store the angle of the circle


            @Override
            public void run() {
                if (t >= 0.25 * Math.PI) {
                    // Cancel task after 3 seconds
                    cancel();
                    return;
                }
                // Calculate the location of the circle
                summonCircle2(0.75, 0.75, 000.1, Color.PURPLE, player, 4);
                summonCircle2(3, 1, 000.1, Color.BLACK, player, 3);
                //summonCircle2(1, 3, 000.1,Color.BLACK, player);
                summonCircle2(0.25, 0.25, 0.1, Color.WHITE, player, 5);

                // Increment the angle for the next tick
                t += Math.PI / 16; // Adjust the speed of the circle
            }
        }.runTaskTimer(QuickSurvival.getInstance(), 0, 1); // Execute the task every tick (20 times per second)
    }

    public void summonCircle2(double scaleX, double scaleY, double density, Color particleColor, Player player, float size) {

        for (double i = 0; i < 2 * Math.PI; i += density) {
            double x = Math.cos(i) * scaleX;
            double y = Math.sin(i) * scaleY;

            // spawn particle
            player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(x, 0, y), 1, new Particle.DustOptions(particleColor, size));

        }
    }


}







