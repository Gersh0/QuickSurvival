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
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WaystonePlacement implements Listener {

    @EventHandler
    public void onPlayerWaystonePlacement(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        ItemStack waypointStack = player.getInventory().getItemInMainHand();

        if (waypointStack.getItemMeta() == null || !isWaystoneItem(waypointStack)) return;

        PersistentDataContainer container = Objects.requireNonNull(waypointStack.getItemMeta()).getPersistentDataContainer();
        if (!isRightClick(event) || !hasWaystoneTag(container) || !isClickedBlockValid(event)) return;

//        Location blockLocation = getBlockLocationAbove(Objects.requireNonNull(event.getClickedBlock()).getLocation());
////        if (!isEmptyBlock(blockLocation)) {
////            player.sendMessage(ChatColor.RED + "No se puede colocar el Waystone aquí. El bloque no está vacío.");
////            return;
////        }
        if (isPlayerClickingSideOfBlock(event)) return;
        //open anvil gui to rename the waystone
        WaystoneMenuGui.makeAnvilGuiFirstRename(player);

        player.sendMessage(ChatColor.GREEN + "Waystone placed!");
        event.setCancelled(true);
    }

    //event that blocks the placement of the waystone if the place side is not valid
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        // Get the face of the block that was clicked
        BlockFace clickedFace = event.getBlockAgainst().getFace(event.getBlock());

        // Check if the player is clicking a side of a block
        if (clickedFace != BlockFace.UP ) {
            //check if the player is placing a waystone
            ItemStack item = event.getItemInHand();
            if (item.getItemMeta() == null || !isWaystoneItem(item)) return;

            //send message to the player
            event.getPlayer().sendMessage(ChatColor.RED + "You can't place the waystone on the side of a block");
            // Cancel the event
            event.setCancelled(true);

        }else{event.setCancelled(false);}
    }

    //event that assigns the data container to the item that drops when you break the waystone
    @EventHandler
    public void waystoneBreakEvent(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (!(block.getState() instanceof TileState tileState)) {
            checkWaystoneSurroundingsForElimination(event.getPlayer());
            //debug message

            return;
        };
        PersistentDataContainer container = tileState.getPersistentDataContainer();
        //debug message
        //event.getPlayer().sendMessage(container.toString());
        if (!container.has(Keys.WAYSTONE, PersistentDataType.STRING)) return;
        //debug message
        event.getPlayer().sendMessage("Waystone has been broken");
        ItemStack item = new ItemStack(block.getType());
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName("Waystone");
        itemMeta.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, "Waystone");
        itemMeta.setLore(List.of("Place this to create a waystone"));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);

        item.setItemMeta(itemMeta);

        //drop the item and remove the other drop
        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation(), item);
    }

    //event that checks if the player get a dropped waystone and it has a waystone key
    @EventHandler
    public void onWaystonePickup(EntityPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        String waystoneName = "Waystone";
        ItemMeta itemMeta = item.getItemMeta();
        String actualName = ChatColor.stripColor(itemMeta.getDisplayName());
        if (itemMeta == null || !actualName.equalsIgnoreCase(waystoneName)) return;
        PersistentDataContainer container = Objects.requireNonNull(itemMeta.getPersistentDataContainer());
        if (container.has(Keys.WAYSTONE, PersistentDataType.STRING)) return;
        itemMeta.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, waystoneName);
        itemMeta.setLore(List.of("Place this to create a waystone"));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);

        item.setItemMeta(itemMeta);
        //debug message
        event.getEntity().sendMessage("Waystone has been picked up");

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

    //method that sees if the player is looking at the same waystone
    public static boolean isPlayerLookingAtWaystone(Player player) {
        //get the player target block
        Block block = player.getTargetBlockExact(5);
        if (block == null) return false;
        //get the block state
        BlockState blockState = block.getState();
        //get the tile state
        if (!(blockState instanceof TileState)) return false;

        TileState tileState = (TileState) blockState;
        //get the persistent data container
        PersistentDataContainer container = tileState.getPersistentDataContainer();

        //see if the block has a waystone key
        return true;

    }

    //see if im clicking a side of a block to cancell the placement of the waystone
    public static boolean isPlayerClickingSideOfBlock(PlayerInteractEvent event) {
        return event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getBlockFace() != BlockFace.UP;
    }


    //if the player isn't looking at the waystone well see if there's a block that has up the waystone and update the tilestate of the banner
    public static void checkWaystoneSurroundings(Player player, String name) {

        Block block = player.getTargetBlockExact(5);

        if (block == null) return;

        player.sendMessage(block.toString());
        if (isPlayerLookingAtWaystone(player)) {
            //get the info of the block and assign the new name
            //send message

            //say that the player is looking at the waystone
            player.sendMessage(ChatColor.RED + "You are looking at the waystone");

            TileState blockState = (TileState) block.getState();
            blockState.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, name);
            blockState.update();

            //save the waystone
            makeSaveProcessOfWaystone(player, block.getLocation(), name);
            return;
        }


        //make a check waystoneSuroundings for eliminating the waystone


        //get the block above the block

        Block waystoneBlock = block.getRelative(0, 1, 0);

        BlockState WblockState = waystoneBlock.getState();
        if (!(WblockState instanceof TileState tileState)) return;

        PersistentDataContainer container = tileState.getPersistentDataContainer();
        container.set(Keys.WAYSTONE, PersistentDataType.STRING, name);
        tileState.update();
        Location location = new Location(waystoneBlock.getWorld(), waystoneBlock.getX(), waystoneBlock.getY(), waystoneBlock.getZ());

        //save the waystone
        makeSaveProcessOfWaystone(player, location, name);

    }

    //method that checks the surrounding of the block to see if theres a waystone and eliminate it
    public static void checkWaystoneSurroundingsForElimination(Player player) {
        Block block = player.getTargetBlockExact(5);
        if (block == null) return;
        Block waystoneBlock = block.getRelative(0, 1, 0);
        BlockState WblockState = waystoneBlock.getState();
        if (!(WblockState instanceof TileState tileState)) return;
        PersistentDataContainer container = tileState.getPersistentDataContainer();
        if (!container.has(Keys.WAYSTONE, PersistentDataType.STRING)) return;
        String waystoneName = container.get(Keys.WAYSTONE, PersistentDataType.STRING);
        removeWaystone(waystoneName);
        tileState.update();
        player.sendMessage("Waystone has fallen");
    }


    //method that will be used to update the waystone in the yml file
    public static void makeSaveProcessOfWaystone(Player player, Location blockLocation, String waystoneName) {
        //Process to save the waystone to the yml file
        List<String> players = new ArrayList<>();
        players.add(player.getName());

        //center the location of the block
        Location location = new Location(player.getWorld(), blockLocation.getX() + 0.5, blockLocation.getY(), blockLocation.getZ() + 0.5);
        Waystone waystone = new Waystone(location, waystoneName, players, null);

        //save the waystone
        saveWaystone(waystone, QuickSurvival.waystonesConfig, waystoneName);

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


