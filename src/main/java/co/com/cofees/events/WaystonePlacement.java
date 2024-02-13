package co.com.cofees.events;


import co.com.cofees.tools.Keys;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class WaystonePlacement implements Listener {


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction().name().contains("RIGHT") && event.getClickedBlock() != null && event.getMaterial() == Material.BLACK_BANNER) {
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(Keys.WAYSTONE, PersistentDataType.STRING)) {
                Location blockLocation = event.getClickedBlock().getLocation();
                //blockLocation.setZ(blockLocation.getZ() + 10);

                // Carga el chunk antes de aplicar cambios al bloque
                blockLocation.getWorld().getChunkAt(blockLocation).load();

                // Crea un nuevo bloque en la misma ubicación
                Block newBlock = blockLocation.getWorld().getBlockAt(blockLocation);
                newBlock.setType(Material.BLACK_BANNER);

                // Guarda la información en el nuevo bloque
                TileState tileState = (TileState) newBlock.getState();
                PersistentDataContainer container = tileState.getPersistentDataContainer();
                container.set(Keys.WAYSTONE, PersistentDataType.STRING, "true");
                tileState.update();

               ItemStack iteminHand= player.getInventory().getItemInMainHand();
               iteminHand.setAmount(iteminHand.getAmount()-1);
                player.sendMessage(ChatColor.GREEN + "Se ha colocado un nuevo Waystone correctamente.");
            }
        }
        event.setCancelled(true);
    }
}

