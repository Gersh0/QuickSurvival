package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import jdk.jfr.Percentage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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

        // Items para decorar el inventario:
        ItemStack deco =  createDecorativePane();
        for(int i=0; i<9;i++){
            if(i<3 || i>5){
                mainMenu.setItem(i, deco);
            }
        }


        player.openInventory(mainMenu);
        //player.sendMessage("MainMenu was opened");//debug

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

        //Item para configurar cuantos jugadores deben de dormir
        ItemStack sleep =  new ItemStack(Material.RED_BED);
        ItemMeta sleepMeta = sleep.getItemMeta();
        assert sleepMeta != null;
        sleepMeta.setCustomModelData(10);
        sleepMeta.setDisplayName(ChatColor.GREEN + "Config % of players that need to sleep");
        sleep.setItemMeta(sleepMeta);
        generalMenu.setItem(10, sleep);

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
        veinMinerMeta.setDisplayName(ChatColor.GRAY+"Toggles veinMiner");
        veinMinerMeta.setLore(Arrays.asList(ChatColor.BLUE+"Currently " + ((QuickSurvival.isVeinMinerActive())? "On": "Off")
                , ChatColor.WHITE+"When breaking an ore while crouching,"
                , ChatColor.WHITE+"all of the surrounding ore of the same type will break."));
        veinMiner.setItemMeta(veinMinerMeta);
        generalMenu.setItem(11, veinMiner);

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
        generalMenu.setItem(12, treeCapitator);

        // item para activar/desactivar poder romper spawners

        // Para decorar el inventario:
        ItemStack deco =  createDecorativePane();
        for(int i=0; i<36;i++){

            if((i!=4 && i!=31) && !(i<17 && i>9) && !(i<26 && i>18)){
                generalMenu.setItem(i, deco);
            }
        }



        player.openInventory(generalMenu);
        //player.sendMessage("GeneralMenu was opened");//debug
    }

    public void openSleepEventMenu(Player player){
        Inventory sleepMenu = Bukkit.createInventory(null, 27, "Sleep Menu");

        // item para cerrar el inventario
        ItemStack close =  new ItemStack(Material.BARRIER);
        ItemMeta closeMeta = close.getItemMeta();
        assert closeMeta != null;
        closeMeta.setCustomModelData(10);
        closeMeta.setDisplayName(ChatColor.RED + "Close menu");
        close.setItemMeta(closeMeta);
        sleepMenu.setItem(22, close);

        // item para volver al menu general:
        ItemStack back =  new ItemStack(Material.GRASS_BLOCK);
        ItemMeta backMeta = back.getItemMeta();
        assert backMeta != null;
        backMeta.setCustomModelData(10);
        backMeta.setDisplayName(ChatColor.GREEN + "Back to the general menu");
        back.setItemMeta(backMeta);
        sleepMenu.setItem(4, back);

        // items para setear el porcentaje:
        int[] porcentajes = {1,25,33,50,66,75,100};
        for(int i=0;i<7;i++){
            ItemStack percentage =  new ItemStack(Material.WHITE_BED);
            if(porcentajes[i] == QuickSurvival.getSleepingPercentage()){
                percentage =  new ItemStack(Material.GREEN_BED);
            }
            ItemMeta percentageMeta = percentage.getItemMeta();
            assert percentageMeta != null;
            percentageMeta.setCustomModelData(10);
            percentageMeta.getPersistentDataContainer().set(Keys.PERCENTAGE, PersistentDataType.INTEGER, porcentajes[i]);
            percentageMeta.setDisplayName(ChatColor.BLUE+(porcentajes[i]+"%"));
            percentage.setItemMeta(percentageMeta);
            sleepMenu.setItem(10+i, percentage);
        }

        //Items para decorar el menú:
        ItemStack deco =  createDecorativePane();
        for(int i=0; i<27;i++){
            if((i!=4 && i!=22) && !(i<17 && i>9)){
                sleepMenu.setItem(i, deco);
            }
        }

        player.openInventory(sleepMenu);
        //player.sendMessage("SleepMenu was opened");//debug
    }

    public ItemStack createDecorativePane(){
        ItemStack deco =  new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta decoMeta = deco.getItemMeta();
        assert decoMeta != null;
        decoMeta.setCustomModelData(10);
        decoMeta.setDisplayName(" ");
        deco.setItemMeta(decoMeta);
        return deco;
    }

}
