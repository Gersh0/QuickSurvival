package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ControlMenuGUI {

    public void openMainMenu(Player player){

        Inventory mainMenu = Bukkit.createInventory(null, 9, "Main Menu");

        //      opciones en el menu principal:

        // Item para cerrar el inventario:
        ItemStack close =  new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        assert closeMeta != null;
        closeMeta.setCustomModelData(10);
        closeMeta.setDisplayName(ChatColor.RED + "Close menu");
        close.setItemMeta(closeMeta);
        mainMenu.setItem(3, close);

        // Item para abrir el menu de eventos general:
        ItemStack generalMenu =  new ItemStack(Material.GRASS_BLOCK);
        ItemMeta generalMenuMeta = generalMenu.getItemMeta();
        assert generalMenuMeta != null;
        generalMenuMeta.setCustomModelData(10);
        generalMenuMeta.setDisplayName(ChatColor.GREEN + "Open general events menu");
        generalMenu.setItemMeta(generalMenuMeta);
        mainMenu.setItem(4, generalMenu);

        // Item para abrir el menu de eventos UHC:
        ItemStack uhcMenu =  new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta uhcMenuMeta = uhcMenu.getItemMeta();
        assert uhcMenuMeta != null;
        uhcMenuMeta.setCustomModelData(10);
        uhcMenuMeta.setDisplayName(ChatColor.GOLD + "Open UHC events menu");
        uhcMenu.setItemMeta(uhcMenuMeta);
        mainMenu.setItem(5, uhcMenu);


        player.openInventory(mainMenu);
        player.sendMessage("MainMenu was opened");

    }

    public void openGeneralMenu(Player player){
        Inventory generalMenu = Bukkit.createInventory(null, 36, "General Menu");

        // item para cerrar el inventario
        ItemStack close =  new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        assert closeMeta != null;
        closeMeta.setCustomModelData(10);
        closeMeta.setDisplayName(ChatColor.RED + "Close menu");
        close.setItemMeta(closeMeta);
        generalMenu.setItem(31, close);

        // item para volver al menu principal:
        ItemStack back =  new ItemStack(Material.GRASS_BLOCK);
        ItemMeta backMeta = back.getItemMeta();
        assert backMeta != null;
        backMeta.setCustomModelData(10);
        backMeta.setDisplayName(ChatColor.GREEN + "Back to the main menu");
        back.setItemMeta(backMeta);
        generalMenu.setItem(4, back);

        // item para activar/desactivar las vacas explosivas
        ItemStack cows = new ItemStack(Material.COW_SPAWN_EGG);
        ItemMeta cowMeta = cows.getItemMeta();
        assert cowMeta != null;
        cowMeta.setCustomModelData(10);
        cowMeta.setDisplayName(ChatColor.RED + "Toggles Explosive cows");
        cowMeta.setLore(Arrays.asList(ChatColor.BLUE+"Currently " + ((QuickSurvival.areCowsExplosive())? "On": "Off")
                , ChatColor.WHITE+"Cows will explode when right clicked!"));
        cows.setItemMeta(cowMeta);
        generalMenu.setItem(25, cows);

        // item para activar/desactivar veinMiner
        ItemStack veinMiner = new ItemStack(Material.IRON_PICKAXE);
        ItemMeta veinMinerMeta = veinMiner.getItemMeta();
        assert veinMinerMeta != null;
        veinMinerMeta.setCustomModelData(10);
        if(QuickSurvival.isVeinMinerActive()){

        }
        veinMinerMeta.setDisplayName(ChatColor.GRAY+"Toggles veinMiner");
        veinMinerMeta.setLore(Arrays.asList(ChatColor.BLUE+"Currently " + ((QuickSurvival.isVeinMinerActive())? "On": "Off")
                , ChatColor.WHITE+"When breaking an ore while crouching,"
                , ChatColor.WHITE+"all of the surrounding ore of the same type will break."));
        veinMiner.setItemMeta(veinMinerMeta);
        generalMenu.setItem(10, veinMiner);

        // item para activar/desactivar treeCapitator
        ItemStack treeCapitator = new ItemStack(Material.IRON_AXE);
        ItemMeta treeCapitatorMeta = treeCapitator.getItemMeta();
        assert treeCapitatorMeta != null;
        treeCapitatorMeta.setCustomModelData(10);
        treeCapitatorMeta.setDisplayName(ChatColor.GRAY+"Toggles TreeCapitator");
        treeCapitatorMeta.setLore(Arrays.asList(ChatColor.BLUE+"Currently " + ((QuickSurvival.isTreeCapitatorActive())? "On": "Off")
                , ChatColor.WHITE+"When breaking a tree while crouching,"
                , ChatColor.WHITE+"all of the logs in the tree will break."
                , ChatColor.WHITE+"Only works if it still has leaves touching the log!"));
        treeCapitator.setItemMeta(treeCapitatorMeta);
        generalMenu.setItem(11, treeCapitator);



        player.openInventory(generalMenu);
        player.sendMessage("MainMenu was opened");
    }

}
