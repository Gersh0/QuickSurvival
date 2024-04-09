package co.com.cofees.events;


import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import co.com.cofees.tools.LocationHandler;
import co.com.cofees.tools.Waystone;
import co.com.cofees.tools.WaystoneMenuGui;
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
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaystonePlacement implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack waypointStack = player.getInventory().getItemInMainHand();

        if (waypointStack.getItemMeta() == null || !isWaystoneItem(waypointStack)) return;

        PersistentDataContainer container = Objects.requireNonNull(waypointStack.getItemMeta()).getPersistentDataContainer();
        if (!isRightClick(event) || !hasWaystoneTag(container) || !isClickedBlockValid(event)) return;

        Location blockLocation = getBlockLocationAbove(Objects.requireNonNull(event.getClickedBlock()).getLocation());
        if (!isEmptyBlock(blockLocation)) {
            player.sendMessage(ChatColor.RED + "No se puede colocar el Waystone aquí. El bloque no está vacío.");
            return;
        }

//Process to save the waystone to the yml file
        List<String> players = new ArrayList<>();
        players.add(player.getName());
        Waystone waystone = new Waystone(blockLocation, waypointStack.getItemMeta().getDisplayName(), players, null);

        consumeItemInHand(player);
        placeNewWaystoneBlock(waystone, blockLocation);

        saveWaystone(waystone, QuickSurvival.waystonesConfig, waystone.getName());

        player.sendMessage(ChatColor.GREEN + "Se ha colocado un nuevo Waystone correctamente.");
        event.setCancelled(true);
    }

    public void onPlayerWaystoneCraft (CraftItemEvent event) {
        //see if is a waystone
        ItemStack waypointStack = event.getCurrentItem();
        if (waypointStack.getItemMeta() == null || !isWaystoneItem(waypointStack)) return;

        //get the player
        Player player = (Player) event.getWhoClicked();
        //open the anvil menu
        Waystone waystone = new Waystone(player.getLocation(), waypointStack.getItemMeta().getDisplayName(), new ArrayList<>(), null);
        WaystoneMenuGui.makeAnvilGui(player, waypointStack);
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

    private void placeNewWaystoneBlock(Waystone waystone, Location location) {
        Block newBlock = location.getBlock();
        newBlock.setType(Material.BLACK_BANNER);

        TileState tileState = (TileState) newBlock.getState();
        //set waystone name to the block

        PersistentDataContainer newContainer = tileState.getPersistentDataContainer();
        newContainer.set(Keys.WAYSTONE, PersistentDataType.STRING, waystone.getName());
        tileState.update();
    }

    //this methos will be used to save the waystone to the yml file
    public static void saveWaystone(Waystone waystone, YamlConfiguration config, String path) {

        //based on the setHome class in Home command we will addapt the save method to the waystone class
        config.set(path + ".name", waystone.getName());
        config.set(path + ".icon", waystone.getIcon());
        LocationHandler.serializeLocation(waystone.getLocation(), config, path + ".location"); //saves location in waystonConfig.yml
        config.set(path + ".players", waystone.getPlayers());


        QuickSurvival.waystones.put(waystone.getName(), waystone);

        try {
            config.save(new File(QuickSurvival.getInstance().getDataFolder(), "waystones.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //send message to the console
        QuickSurvival.getInstance().getServer().getLogger().info("Waystone " + ChatColor.GREEN + waystone.getName() + ChatColor.RESET + " has been saved to the waystones.yml file.");
    }

    public static void removeWaystone(String waystoneName) {
        QuickSurvival.waystonesConfig.set(waystoneName, null);
        QuickSurvival.waystones.remove(waystoneName);
        try {
            QuickSurvival.waystonesConfig.save(new File(QuickSurvival.getInstance().getDataFolder(), "waystones.yml"));
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


