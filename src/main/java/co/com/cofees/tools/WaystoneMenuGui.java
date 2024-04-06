package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

        ItemStack nameChange = new ItemStack(Material.NAME_TAG);
        ItemMeta nameChangeMeta = nameChange.getItemMeta();
        nameChangeMeta.setDisplayName(ChatColor.GREEN + "Change Name");
        nameChange.setItemMeta(nameChangeMeta);
        waystoneOptionMenu.setItem(4, nameChange);

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

    public static void openAnvilRenameWaystoneMenu(Player player, Waystone waystone) {
        player.sendMessage("name change menu was opened");
        //create an anvil inventory to rename the waystone
        Inventory anvil = Bukkit.createInventory(player, InventoryType.ANVIL, "Rename Waystone");
        anvil.setItem(0, waystone.getIcon());
        player.openInventory(anvil);
    }

    public static void makeAnvilGui(Player player, Waystone waystone) {
        AnvilGUI.Builder builder =

                new AnvilGUI.Builder()
                        .onClose(stateSnapshot -> {
                            stateSnapshot.getPlayer().sendMessage("You closed the inventory.");
                        })
                        .onClick((slot, stateSnapshot) -> { // Either use sync or async variant, not both
                            if(slot != AnvilGUI.Slot.OUTPUT) {
                                return Collections.emptyList();
                            }

                            if(stateSnapshot.getText().equalsIgnoreCase("you")) {
                                stateSnapshot.getPlayer().sendMessage("You have magical powers!");
                                return Arrays.asList(AnvilGUI.ResponseAction.close());
                            } else {
                                return Arrays.asList(AnvilGUI.ResponseAction.replaceInputText("Try again"));
                            }
                        })
                        .preventClose()                                                    //prevents the inventory from being closed
                        .text("What is the meaning of life?")                              //sets the text the GUI should start with
                        .title("Enter your answer.")                                       //set the title of the GUI (only works in 1.14+)
                        .plugin(QuickSurvival.getInstance());                              //opens the GUI for the player provided

        builder.open(player);

    }

    private static void myCode(Player player) {
        // Aquí puedes definir las acciones que deseas realizar
        // cuando el jugador ingresa "you" en el inventario del yunque.
        // Por ejemplo:
        player.sendMessage("You have magical powers!");
        // Realiza otras acciones según tus necesidades.
    }


    //method that shows the waystone information of an item with persistent data container WAYSTONE(Location and Players with access)
    public static ItemStack showWaystoneInfo(ItemStack waystoneItem, Waystone waystone){

        ItemMeta itemMeta = waystoneItem.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if (!QuickSurvival.waystones.containsKey(waystone.getName())) return waystoneItem;
        itemMeta.setLore(Arrays.asList(
                ChatColor.GREEN + "Location: ",
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
    public static ItemStack clearLore(ItemStack item){
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setLore(null);
        item.setItemMeta(itemMeta);
        return item;


}





}
