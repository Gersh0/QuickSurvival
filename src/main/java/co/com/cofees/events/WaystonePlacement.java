package co.com.cofees.events;


import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import co.com.cofees.tools.LocationHandler;
import co.com.cofees.tools.Waystone;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class WaystonePlacement implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack waypointStack = player.getInventory().getItemInMainHand();

        if (waypointStack.getItemMeta() == null) return;//el mismo guard clause

        if (isWaystoneItem(waypointStack)) {
            PersistentDataContainer container = Objects.requireNonNull(waypointStack.getItemMeta()).getPersistentDataContainer();

            if (isRightClick(event) && hasWaystoneTag(container) && isClickedBlockValid(event)) {
                Location blockLocation = getBlockLocationAbove(Objects.requireNonNull(event.getClickedBlock()).getLocation());

                if (isEmptyBlock(blockLocation)) {
                    consumeItemInHand(player);
                    placeNewWaystoneBlock(blockLocation);


                    //Process to save the waystone to the yml file
                    List<String> players = new ArrayList<>();
                    players.add(player.getName());
                    Waystone waystone = new Waystone(blockLocation, waypointStack.getItemMeta().getDisplayName(), players, null
                    );


                    saveWaystone(waystone, QuickSurvival.waystonesConfig, waystone.getName());

                    player.sendMessage(ChatColor.GREEN + "Se ha colocado un nuevo Waystone correctamente.");
                } else {
                    player.sendMessage(ChatColor.RED + "No se puede colocar el Waystone aquí. El bloque no está vacío.");
                }
            }
        }

        event.setCancelled(true);
    }

    private boolean isWaystoneItem(ItemStack itemStack) {
        if (itemStack != null) {
            PersistentDataContainer container = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
            return container.has(Keys.WAYSTONE, PersistentDataType.STRING);
        }
        return false;
    }


    private boolean isRightClick(PlayerInteractEvent event) {
        return event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK;
    }

    private boolean hasWaystoneTag(PersistentDataContainer container) {
        return container.has(Keys.WAYSTONE, PersistentDataType.STRING);
    }

    private boolean isClickedBlockValid(PlayerInteractEvent event) {
        return event.getClickedBlock() != null;
    }

    private Location getBlockLocationAbove(Location location) {
        return location.clone().add(0, 1, 0);
    }

    private boolean isEmptyBlock(Location location) {
        return location.getBlock().getType() == Material.AIR;
    }

    private void consumeItemInHand(Player player) {
        ItemStack itemInHand = player.getInventory().getItemInMainHand();
        itemInHand.setAmount(itemInHand.getAmount() - 1);
    }

    private void placeNewWaystoneBlock(Location location) {
        Block newBlock = location.getBlock();
        newBlock.setType(Material.BLACK_BANNER);

        TileState tileState = (TileState) newBlock.getState();
        PersistentDataContainer newContainer = tileState.getPersistentDataContainer();
        newContainer.set(Keys.WAYSTONE, PersistentDataType.STRING, "true");
        tileState.update();
    }

    //this methos will be used to save the waystone to the yml file
    private void saveWaystone(Waystone waystone, YamlConfiguration config, String path) {
    //based on the setHome class in Home command we will addapt the save method to the waystone class
        config.set(path + ".name", waystone.getName());
        config.set(path + ".icon", waystone.getIcon().toString());
        LocationHandler.serializeLocation(waystone.getLocation(), config,path + ".location"); //saves location in waystonConfig.yml
        config.set(path + ".players", waystone.getPlayers());


        QuickSurvival.waystones.put(waystone.getName(), waystone);

        try {
            config.save(new File(QuickSurvival.getInstance().getDataFolder(), "waystones.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//obtener lista de jugadores:

//QuickSurvival.waystonesConfig.getStringList(waypointStack.getItemMeta().getDisplayName() + ".players");

//loader
//item assigner
//manager

//pensar el stream con filter, etc

//player.teleport()

// Crear Clase Waystone con:
//Location
//lista o arreglo, etc de String de jugadores
//en un futuro ItemStack con el item que se coloca en el inventario

//borrar waystone del yml cuando se rompa el bloque

//ademas de los debidos metodos de acceso y modificacion



//Hashmap<String, Waystone> waystones = new Hasmap<>();


