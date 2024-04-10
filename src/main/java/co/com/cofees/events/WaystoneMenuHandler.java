package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import co.com.cofees.tools.Waystone;
import co.com.cofees.tools.WaystoneMenuGui;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class WaystoneMenuHandler implements Listener {
    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {

        //get a waystone
        Waystone waystone = getWaystoneFromInventoryView(event.getView());
        //Guard Clause if the waystone is null
        if (waystone == null) return;


        Player player = (Player) event.getWhoClicked();
        //send message to the player
        player.sendMessage("Waystone:" + waystone.getName());
        player.sendMessage("Inventory Title:" + event.getView().getTitle());

        String WaystoneOptions = waystone.getName() + " Options";
        String IconSelectWaystone = waystone.getName() + " Icon Select";
        final String WaystoneRename = "Rename Waystone";


        if (event.getView().getTitle().equalsIgnoreCase(WaystoneOptions)) {

            //lock the player inventory
            event.setCancelled(true);

            //disable change name if the player is not looking at the waystone

            player.sendMessage("You clicked on the main menu, on slot #" + event.getSlot());

            Material waystoneMaterial = waystone.getIcon().getType();

            if (event.getCurrentItem() == null) return;

            Material iconSelectMaterial = event.getCurrentItem().getType();

            switch (iconSelectMaterial) {
                case BOOK:
                    if (event.getRawSlot() == 2) {
                        //opens icon select waystone menu
                        player.closeInventory();
                        player.sendMessage("icon select waystone menu was opened");
                        WaystoneMenuGui.openIconSelectWaystoneMenu(player, waystone);
                    }
                    break;

                case NAME_TAG:

                    if (event.getRawSlot() == 4) {
                        //see if the player is looking at the waystone with the same name as the menu

                        //opens name change menu
                        player.closeInventory();
                        //open the anvil menu
                        //WaystoneMenuGui.makeAnvilGui(player, waystone);
                        WaystoneMenuGui.makeAnvilGui(player, waystone);
                        player.sendMessage("name change menu was opened");

                    }
                    break;

                case REDSTONE:
                    if (event.getRawSlot() == 6) {
                        //deletes waystone
                        player.closeInventory();

                        deleteWaystoneFromMenu(waystone, player);

                        WaystoneInteract.openWaystoneInventory(player);
                    }
                    break;

                case BARRIER:
                    if (event.getRawSlot() == 8) {
                        //closes menu
                        player.closeInventory();
                        player.sendMessage("waystone menu was closed");
                        WaystoneInteract.openWaystoneInventory(player);
                        player.playSound(player.getLocation(), Sound.ITEM_TRIDENT_HIT, 1, 1);
                    }
                    break;

            }
        }

        if (event.getView().getTitle().equalsIgnoreCase(IconSelectWaystone)) {
            //lock the player inventory
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null) return;
            PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();

            if (container.has(Keys.MENUOBJECT, PersistentDataType.STRING)) {
                return;
            }
            ItemStack iconSelect = waystone.getIcon();
            iconSelect.setType(item.getType());
            QuickSurvival.waystones.put(waystone.getName(), waystone);
            WaystonePlacement.saveWaystone(waystone, QuickSurvival.waystonesConfig, waystone.getName());
            player.sendMessage(waystone.getName() + " icon was changed");
            player.closeInventory();
            WaystoneInteract.openWaystoneInventory(player);

        }



    }


    //see if in the inventoryView has a waystone
    public Waystone getWaystoneFromInventoryView(InventoryView inventoryView) {

        if (inventoryView.getTopInventory().getSize() == 9) {
            ItemStack item = inventoryView.getTopInventory().getItem(0);
            if (item != null) {
                return QuickSurvival.waystones.get(Objects.requireNonNull(item.getItemMeta()).getDisplayName());
            }
        } else if (inventoryView.getTopInventory().getSize() == 54) {
            ItemStack item = inventoryView.getTopInventory().getItem(49);
            if (item != null) {
                return QuickSurvival.waystones.get(Objects.requireNonNull(item.getItemMeta()).getDisplayName());
            }
        } else if (inventoryView.getTopInventory().getSize() == 3) {
            ItemStack item = inventoryView.getTopInventory().getItem(0);
            if (item != null) {
                return QuickSurvival.waystones.get(Objects.requireNonNull(item.getItemMeta()).getDisplayName());
            }
        }
        return null;
    }

    public static boolean isPlayerLookingAtTheSameWaystone (Player player, Waystone waystone) {
        //get the player target block
        Block block = player.getTargetBlockExact(5);
        if (block == null) return false;
        //get the block state
        BlockState blockState = block.getState();
        //get the tile state
        if (!(blockState instanceof TileState) ) return false;

        TileState tileState = (TileState) blockState;
        //get the persistent data container
        PersistentDataContainer container = tileState.getPersistentDataContainer();

        //get the waystone name
        String waystoneName = waystone.getName();
        //compare the waystone name with the block name
        if (!container.has(Keys.WAYSTONE, PersistentDataType.STRING)) return false;

        return container.get(Keys.WAYSTONE, PersistentDataType.STRING).equals(waystoneName);

    }

    //delete waystone just from the menu, not from the file
    public void deleteWaystoneFromMenu(Waystone waystone, Player player) {
        //search the waysone in the hashmap
        Waystone waystoneToDelete = QuickSurvival.waystones.get(waystone.getName());

        //remove the player from the waystone
        waystoneToDelete.removePlayer(player.getName());
        //save the waystone
        WaystonePlacement.saveWaystone(waystoneToDelete, QuickSurvival.waystonesConfig, waystoneToDelete.getName());

        player.sendMessage(waystone.getName() + " Waystone was deleted for: " + player.getName());
    }



}
