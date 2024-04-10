package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import co.com.cofees.events.WaystoneInteract;
import co.com.cofees.events.WaystoneMenuHandler;
import co.com.cofees.events.WaystonePlacement;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.emptyList;

public class WaystoneMenuGui {

    //waystone menu section
    public static void openWaystoneOptionMenu(Player player, Waystone waystone) {
        String waystoneName = waystone.getName();
        Inventory waystoneOptionMenu = Bukkit.createInventory(player, 9, waystoneName + " Options");

        ItemStack iconSelect = new ItemStack(Material.BOOK);
        ItemMeta iconSelectMeta = iconSelect.getItemMeta();
        iconSelectMeta.setDisplayName(ChatColor.GREEN + "Select Icon");
        iconSelect.setItemMeta(iconSelectMeta);
        waystoneOptionMenu.setItem(2, iconSelect);
        //if the player is not looking at the waystone disable the change name
        if (!WaystoneMenuHandler.isPlayerLookingAtTheSameWaystone(player, waystone)) {
            ItemStack nameChange = new ItemStack(Material.DAMAGED_ANVIL);
            ItemMeta nameChangeMeta = nameChange.getItemMeta();
            nameChangeMeta.setDisplayName(ChatColor.RED + " "+ ChatColor.BOLD + "Cannot Change Name");
            nameChangeMeta.setLore(Collections.singletonList(ChatColor.RED + "You are not looking at the same waystone"));
            nameChange.setItemMeta(nameChangeMeta);
            waystoneOptionMenu.setItem(4, nameChange);
        } else {
            ItemStack nameChange = new ItemStack(Material.NAME_TAG);
            ItemMeta nameChangeMeta = nameChange.getItemMeta();
            nameChangeMeta.setDisplayName(ChatColor.GREEN + "Change Name");
            nameChange.setItemMeta(nameChangeMeta);
            waystoneOptionMenu.setItem(4, nameChange);
        }

        ItemStack deleteWaystone = new ItemStack(Material.REDSTONE);
        ItemMeta deleteWaystoneMeta = deleteWaystone.getItemMeta();
        deleteWaystoneMeta.setDisplayName(ChatColor.RED + "Delete Waystone");
        deleteWaystone.setItemMeta(deleteWaystoneMeta);
        waystoneOptionMenu.setItem(6, deleteWaystone);

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close");
        close.setItemMeta(closeMeta);
        waystoneOptionMenu.setItem(8, close);

        //add the current waystone icon
        ItemStack CurrentWaystoneIcon = showWaystoneInfo(waystone.getIcon(), waystone);

        waystoneOptionMenu.setItem(0, CurrentWaystoneIcon);
        clearLore(CurrentWaystoneIcon);


        player.openInventory(waystoneOptionMenu);

    }

    public static void openIconSelectWaystoneMenu(Player player, Waystone waystone) {
        String waystoneName = waystone.getName();
        Inventory iconSelectWaystoneMenu = Bukkit.createInventory(player, 54, waystoneName + " Icon Select");

        //add a queue to store the items
        List<ItemStack> itemStacks = Arrays.asList(
                new ItemStack(Material.REDSTONE_BLOCK),
                new ItemStack(Material.LAPIS_BLOCK),
                new ItemStack(Material.EMERALD_BLOCK),
                new ItemStack(Material.DIAMOND_BLOCK),
                new ItemStack(Material.GOLD_BLOCK),
                new ItemStack(Material.IRON_BLOCK),
                new ItemStack(Material.COAL_BLOCK),
                new ItemStack(Material.QUARTZ_BLOCK),
                new ItemStack(Material.NETHERITE_BLOCK),
                new ItemStack(Material.BONE_BLOCK),
                new ItemStack(Material.BRICKS),
                new ItemStack(Material.BOOKSHELF),
                new ItemStack(Material.CRAFTING_TABLE),
                new ItemStack(Material.FURNACE),
                new ItemStack(Material.CHEST),
                new ItemStack(Material.BEDROCK),
                new ItemStack(Material.BEACON),
                new ItemStack(Material.BREWING_STAND),
                new ItemStack(Material.CAULDRON),
                new ItemStack(Material.COMMAND_BLOCK),
                new ItemStack(Material.DISPENSER),
                new ItemStack(Material.DROPPER),
                new ItemStack(Material.ENCHANTING_TABLE),
                new ItemStack(Material.GRINDSTONE),
                new ItemStack(Material.JUKEBOX),
                new ItemStack(Material.LANTERN),
                new ItemStack(Material.LOOM),
                new ItemStack(Material.NOTE_BLOCK),
                new ItemStack(Material.OBSERVER),
                new ItemStack(Material.SMOKER),
                new ItemStack(Material.SMITHING_TABLE),
                new ItemStack(Material.STONECUTTER),
                new ItemStack(Material.TARGET),
                new ItemStack(Material.LECTERN),
                new ItemStack(Material.BELL),
                new ItemStack(Material.CAMPFIRE),
                new ItemStack(Material.SHROOMLIGHT),
                new ItemStack(Material.END_PORTAL_FRAME),
                new ItemStack(Material.DEEPSLATE_BRICKS),
                new ItemStack(Material.ANVIL),
                new ItemStack(Material.CONDUIT),
                new ItemStack(Material.DRAGON_EGG),
                new ItemStack(Material.GREEN_WOOL),
                new ItemStack(Material.RED_WOOL),
                new ItemStack(Material.BLUE_WOOL)
        );

        ItemStack close = new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        closeMeta.setDisplayName(ChatColor.RED + "Close");
        PersistentDataContainer closeContainer = closeMeta.getPersistentDataContainer();
        closeContainer.set(Keys.MENUOBJECT, PersistentDataType.STRING, "MenuObject");
        close.setItemMeta(closeMeta);

        //add a blackStained glass pane on 45 to 53 slot
        ItemStack blackStainedGlassPane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta blackStainedGlassPaneMeta = blackStainedGlassPane.getItemMeta();
        blackStainedGlassPaneMeta.setDisplayName(" ");
        PersistentDataContainer glassContainer = blackStainedGlassPaneMeta.getPersistentDataContainer();
        glassContainer.set(Keys.MENUOBJECT, PersistentDataType.STRING, "MenuObject");
        blackStainedGlassPane.setItemMeta(blackStainedGlassPaneMeta);
        for (int i = 45; i < 54; i++) {
            iconSelectWaystoneMenu.setItem(i, blackStainedGlassPane);
        }

        //add the current waystone icon
        ItemStack currentIcon = new ItemStack(waystone.getIcon());
        ItemMeta currentIconMeta = currentIcon.getItemMeta();
        PersistentDataContainer currentIconContainer = currentIconMeta.getPersistentDataContainer();
        currentIconContainer.set(Keys.MENUOBJECT, PersistentDataType.STRING, "MenuObject");
        currentIcon.setItemMeta(currentIconMeta);

        iconSelectWaystoneMenu.setItem(49, currentIcon);

        //add the items to the inventory
        itemStacks.forEach(iconSelectWaystoneMenu::addItem);

        //add the close button
        iconSelectWaystoneMenu.setItem(53, close);


        player.openInventory(iconSelectWaystoneMenu);

    }

    public static void makeAnvilGui(Player player, Waystone waystone) {

        AnvilGUI.Builder anvilGUI = new AnvilGUI.Builder();

        anvilGUI
                .plugin(QuickSurvival.getInstance())
                .itemLeft(waystone.getIcon())
                .itemRight(new ItemStack(Material.BARRIER))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return emptyList();
                    }
                    if (stateSnapshot.getText().equalsIgnoreCase(waystone.getName()) || stateSnapshot.getText().isEmpty()) {
                        //send a message to the player
                        stateSnapshot.getPlayer().sendMessage("The name is empty or the same as the waystone name :)");
                        return List.of(AnvilGUI.ResponseAction.close());

                    }

                    String newName = stateSnapshot.getText();
                    stateSnapshot.getPlayer().playSound(stateSnapshot.getPlayer().getLocation(), "block.anvil.use", 1, 1);
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> configureWaystone(newName, stateSnapshot.getPlayer(), waystone))
                    );

                })
                .preventClose()
                .title("Rename Waystone")
                .open(player)

        ;
    }

    //make a anvilGui just for rename items
    public static void makeAnvilGuiFirstRename(Player player) {

        //paper that says rename name
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(ChatColor.GREEN + "Rename Name");
        paper.setItemMeta(paperMeta);

        AnvilGUI.Builder anvilGUI = new AnvilGUI.Builder();

        anvilGUI
                .plugin(QuickSurvival.getInstance())
                .itemLeft(paper)
                .itemRight(new ItemStack(Material.BARRIER))
                .onClick((slot, stateSnapshot) -> {
                    if (slot != AnvilGUI.Slot.OUTPUT) {
                        return emptyList();
                    }
                    if (stateSnapshot.getText().isEmpty()) {
                        //send a message to the player
                        stateSnapshot.getPlayer().sendMessage("The name is empty or the same as the item name :)");
                    }

                    String newName = stateSnapshot.getText();
                    stateSnapshot.getPlayer().playSound(stateSnapshot.getPlayer().getLocation(), "block.anvil.use", 1, 1);
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(() -> WaystonePlacement.checkWaystoneSurroundings(stateSnapshot.getPlayer(), newName))
                    );

                })
                .preventClose()
                .title("Rename Item")
                .open(player)

        ;
    }


    public static void configureWaystone(String newName, Player player, Waystone waystone) {

        //set the new name to the waystone
        WaystonePlacement.removeWaystone(waystone.getName());
        Waystone newWaystone = new Waystone(waystone.getLocation(), newName, waystone.getPlayers(), waystone.getIcon());
        WaystonePlacement.saveWaystone(newWaystone, QuickSurvival.waystonesConfig, newName);

        WaystoneInteract.openWaystoneInventory(player);
        player.sendMessage("The new name is: " + newName);

        //upload the new name to the physical waystone

        Block b = player.getTargetBlockExact(5);

        if (b == null) return;

        if (!(b.getState() instanceof TileState tileState)) return;

        PersistentDataContainer container = tileState.getPersistentDataContainer();
        if (!container.has(Keys.WAYSTONE, PersistentDataType.STRING)) return;

        container.set(Keys.WAYSTONE, PersistentDataType.STRING, newName);
        tileState.update();
    }

    //set the new name to the item
    public static void changeItemName(String newName, Player player, ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(newName);
        item.setItemMeta(itemMeta);

        //give the item to the player if is full drop it
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
            player.sendMessage("The item has been dropped on the floor");
            return;
        } else {
            player.getInventory().addItem(item);
        }

        player.sendMessage("The new name is: " + newName);
    }


    //method that shows the waystone information of an item with persistent data container WAYSTONE(Location and Players with access)

    public static ItemStack showWaystoneInfo(ItemStack waystoneItem, Waystone waystone) {

        ItemMeta itemMeta = waystoneItem.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (!QuickSurvival.waystones.containsKey(waystone.getName())) return waystoneItem;
        itemMeta.setLore(Arrays.asList(
                ChatColor.GREEN + "Location: ",
                ChatColor.WHITE + "World: " + Objects.requireNonNull(waystone.getLocation().getWorld()).getName(),
                ChatColor.WHITE + "X: " + waystone.getLocation().getBlockX(),
                ChatColor.WHITE + "Y: " + waystone.getLocation().getBlockY(),
                ChatColor.WHITE + "Z: " + waystone.getLocation().getBlockZ(),
                ChatColor.GREEN + "Players: " + ChatColor.WHITE + waystone.getPlayers()

        ));

        waystoneItem.setItemMeta(itemMeta);


        //show the waystone information
        return waystoneItem;

    }

    //clear the lore of an item
    public static void clearLore(ItemStack item) {
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(null);
        item.setItemMeta(itemMeta);
    }


}
