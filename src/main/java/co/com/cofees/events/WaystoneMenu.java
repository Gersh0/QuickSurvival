package co.com.cofees.events;

import co.com.cofees.tools.Keys;
import co.com.cofees.tools.TextTools;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.command.CommandSender;
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
            // Verifica si el inventario es el que abriste
            if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&2Inventario Prueba"))) {
                // Cancela el evento para que no se pueda editar el inventario
                event.setCancelled(true);

                // Verifica si hicieron clic en la casilla 20
                if (event.getRawSlot() == 20) {
                    event.getWhoClicked().sendMessage(text("&abuenas"));
                }

                if (event.getRawSlot() == 22) {
                    // Verificar si hicieron clic en la casilla 22 (botón para abrir otro inventario)
                    event.setCancelled(true);

                    // Abrir otro inventario
                    Player player = (Player) event.getWhoClicked();
                    Inventory otroInventario = Bukkit.createInventory(null, 27, "Ajustes");
                    // Configurar el otro inventario
                    player.openInventory(otroInventario);
                }

                if (event.getRawSlot() == 24) {
                    // Botón "Crear Waypoint"
                    event.setCancelled(true);


                    Player player = (Player) event.getWhoClicked();
                    player.sendMessage(ChatColor.RED + "¡Waypoint creado!");

                    giveBeacon(player.getLocation(),player);

                    // Obtener la ubicación del jugador y lanzar el rayo de luz
                    player.getWorld().strikeLightningEffect(player.getLocation());
                    player.spawnParticle(Particle.END_ROD, player.getLocation(), 100, 0.5, 1, 0.5, 0.1);
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }
            }
        }
    }

    //metodo auxiliar
    private void giveBeacon(Location location, Player player) {
        // Crear un ítem Beacon
        ItemStack waystoneItem = new ItemStack(Material.BLACK_BANNER);

        // Obtener o crear el ItemMeta del ítem
        ItemMeta waystoneItemItemMeta = waystoneItem.getItemMeta();

        // Almacenar el nombre "Waystone" en el ItemMeta
        waystoneItemItemMeta.setDisplayName(ChatColor.GREEN + "Waystone");

        waystoneItemItemMeta.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, "true");

        waystoneItem.setItemMeta(waystoneItemItemMeta);

        // Añadir el ítem al inventario del jugador o soltarlo al suelo
        if (player.getInventory().firstEmpty() != -1) {
            // Hay espacio en el inventario
            player.getInventory().addItem(waystoneItem);
            player.sendMessage(ChatColor.GREEN + "Has recibido un Waystone en tu inventario.");
        } else {
            // El inventario está lleno, dejar caer el ítem al suelo
            Item item = player.getWorld().dropItemNaturally(player.getLocation(), waystoneItem);
            item.setInvulnerable(true); // Evitar que otros jugadores lo recojan
            player.sendMessage(ChatColor.GREEN + "No hay espacio en tu inventario. Se ha dejado caer un Waystone al suelo.");

            // Obtener el bloque del Beacon
            Block block = item.getLocation().getBlock();

            // Obtener el TileState asociado al bloque
            TileState tileState = null;
            if (block.getState() instanceof TileState) {
                tileState = (TileState) block.getState();
            } else if (block.getRelative(BlockFace.DOWN).getState() instanceof TileState) {
                tileState = (TileState) block.getRelative(BlockFace.DOWN).getState();
            }

            if (tileState != null) {
                // Almacenar el NamespacedKey en el PersistentDataContainer
                tileState.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, "true");
                tileState.update(); // Actualizar el estado para aplicar los cambios

                player.sendMessage(ChatColor.GREEN + "Se creó Waystone correctamente.");
            }
        }
        // Reproducir el sonido de activación
        player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);

    }


    public void noArgs(CommandSender sender) {
        sender.sendMessage(text("&4&lUse &r/test help &4&lfor usage of command"));
    }

    public String text(String text) {
        return TextTools.coloredText(text);
    }
}
