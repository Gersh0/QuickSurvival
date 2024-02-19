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

        ItemStack waypointStack = player.getInventory().getItemInMainHand();
        if (waypointStack != null) {
            PersistentDataContainer container = waypointStack.getItemMeta().getPersistentDataContainer();

            if (event.getAction().name().contains("RIGHT") && event.getClickedBlock() != null && container.has(Keys.WAYSTONE, PersistentDataType.STRING)) {

                Location blockLocation = event.getClickedBlock().getLocation().clone();
                blockLocation.setY(blockLocation.getY() + 1);

                // Carga el chunk antes de aplicar cambios al bloque
                blockLocation.getWorld().getChunkAt(blockLocation).load();

                // Verifica si el bloque en la ubicación deseada está vacío
                if (blockLocation.getBlock().getType() == Material.AIR) {

                    ItemStack itemInHand = player.getInventory().getItemInMainHand();
                    itemInHand.setAmount(itemInHand.getAmount() - 1);

                    // Crea un nuevo bloque en la misma ubicación
                    Block newBlock = blockLocation.getBlock();
                    newBlock.setType(Material.BLACK_BANNER);

                    // Guarda la información en el nuevo bloque
                    TileState tileState = (TileState) newBlock.getState();
                    PersistentDataContainer newContainer = tileState.getPersistentDataContainer();
                    newContainer.set(Keys.WAYSTONE, PersistentDataType.STRING, "true");
                    tileState.update();

                    player.sendMessage(ChatColor.GREEN + "Se ha colocado un nuevo Waystone correctamente.");
                } else {
                    player.sendMessage(ChatColor.RED + "No se puede colocar el Waystone aquí. El bloque no está vacío.");
                }
            }
        }

        event.setCancelled(true);
    }

}

