package co.com.cofees.events;

import co.com.cofees.tools.Keys;
import co.com.cofees.tools.TextTools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class WaystoneMenu implements Listener {

    @EventHandler
    public void onWaystoneMenuClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() == null) {
            // Check if the inventory is the one you opened
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&2Inventario Prueba"))) {
                // Cancel the event so the inventory cannot be edited
                event.setCancelled(true);

                // Check if they clicked on slot 20
                if (event.getRawSlot() == 20) {
                    event.getWhoClicked().sendMessage(text("&abuenas"));
                }

                if (event.getRawSlot() == 22) {
                    // Check if they clicked on slot 22 (button to open another inventory)
                    event.setCancelled(true);

                    // Open another inventory
                    Player player = (Player) event.getWhoClicked();
                    Inventory otroInventario = Bukkit.createInventory(null, 27, "Ajustes");
                    // Configure the other inventory
                    player.openInventory(otroInventario);
                }

                if (event.getRawSlot() == 24) {
                    // "Create Waypoint" button
                    event.setCancelled(true);

                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage(ChatColor.RED + "Waystone created!");

                    giveBeacon(player);

                    // Get the player's location and launch the light beam
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.spawnParticle(Particle.END_ROD, player.getLocation(), 100, 0.5, 1, 0.5, 0.1);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }
            }
        }
    }

    // Auxiliary method
    private void giveBeacon(Player player) {
        // Create a Beacon item
        ItemStack waystoneItem = new ItemStack(Material.BLACK_BANNER);

        // Get or create the ItemMeta of the item
        ItemMeta waystoneItemItemMeta = waystoneItem.getItemMeta();

        if (waystoneItemItemMeta != null) {
            // Store the name "Waystone" in the ItemMeta
            waystoneItemItemMeta.setDisplayName("Waystone");

            waystoneItemItemMeta.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, "true");

            waystoneItem.setItemMeta(waystoneItemItemMeta);
        }

        // Add the item to the player's inventory or drop it on the ground
        if (player.getInventory().firstEmpty() != -1) {
            // There is space in the inventory
            player.getInventory().addItem(waystoneItem);
            player.sendMessage(ChatColor.GREEN + "You have received a Waystone in your inventory.");
        } else {
            // The inventory is full, drop the item on the ground
            Item item = player.getWorld().dropItemNaturally(player.getLocation(), waystoneItem);
            item.setInvulnerable(true); // Prevent other players from picking it up
            player.sendMessage(ChatColor.GREEN + "There is no space in your inventory. A Waystone has been dropped on the ground.");

            // Get the Beacon block
            Block block = item.getLocation().getBlock();

            // Get the TileState associated with the block
            TileState tileState = null;
            if (block.getState() instanceof TileState) {
                tileState = (TileState) block.getState();
            } else if (block.getRelative(BlockFace.DOWN).getState() instanceof TileState) {
                tileState = (TileState) block.getRelative(BlockFace.DOWN).getState();
            }

            if (tileState != null) {
                // Store the NamespacedKey in the PersistentDataContainer
                tileState.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, waystoneItemItemMeta != null ? waystoneItemItemMeta.getDisplayName() : "Waystone");
                tileState.update(); // Update the state to apply the changes

                player.sendMessage(ChatColor.GREEN + "Waystone created successfully.");
            }
        }
        // Play the activation sound
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
    }

    public String text(String text) {
        return TextTools.coloredText(text);
    }

}