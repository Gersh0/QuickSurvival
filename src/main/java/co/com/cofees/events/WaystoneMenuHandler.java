package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import co.com.cofees.tools.Waystone;
import co.com.cofees.tools.WaystoneMenuGui;
import org.bukkit.Material;
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

            player.sendMessage("You clicked on the main menu, on slot #" + event.getSlot());

            Material waystoneMaterial = waystone.getIcon().getType();


            switch (Objects.requireNonNull(event.getCurrentItem()).getType()) {
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
                player.closeInventory();
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

        if (event.getInventory().getType() == InventoryType.ANVIL && event.getView().getTitle().equalsIgnoreCase(WaystoneRename)) {

            //lock the player inventory
            event.setCancelled(true);

            //send message to the player

            Inventory inventory = event.getInventory();

            event.getWhoClicked().sendMessage("You clicked on the anvil menu, on slot #" + event.getSlot());
            ItemStack waystoneItem = inventory.getItem(0);


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

    //delete waystone just from the menu, not from the file
    public void deleteWaystoneFromMenu(Waystone waystone, Player player) {
        //search the waysone in the hashmap
        Waystone waystoneToDelete = QuickSurvival.waystones.get(waystone.getName());

        //remove the player from the waystone
        waystoneToDelete.removePlayer(player.getName());
        //save the waystone
        QuickSurvival.waystones.put(waystone.getName(), waystoneToDelete);
        WaystonePlacement.saveWaystone(waystoneToDelete, QuickSurvival.waystonesConfig, waystoneToDelete.getName());

        player.sendMessage(waystone.getName() + " Waystone was deleted for: " + player.getName());
    }



}
