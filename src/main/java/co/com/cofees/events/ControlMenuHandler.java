package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.ControlMenuGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.net.http.WebSocket;
import java.util.Objects;

public class ControlMenuHandler implements Listener {



    ControlMenuGUI guiManager = new ControlMenuGUI();

    @EventHandler
    public void onMenuClick(InventoryClickEvent event){

        Player player = (Player)event.getWhoClicked();

        //lista de los posibles menus
        final String MAIN_MENU = "Main Menu";
        final String UHC_MENU = "UHC Menu";
        final String GENERAL_MENU = "General Menu";

        if(event.getView().getTitle().equals(MAIN_MENU)){

            player.sendMessage("You clicked on the main menu, on slot #"+event.getSlot());

            switch (Objects.requireNonNull(event.getCurrentItem()).getType()){
                case BARRIER:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //Se cierra el menu principal
                        player.closeInventory();
                        player.sendMessage("main control menu was closed");
                    }
                    break;

                case GRASS_BLOCK:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        // Se cambia al menu de eventos general
                        player.closeInventory();
                        guiManager.openGeneralMenu(player);
                        player.sendMessage("switched to general menu");
                    }
                    break;

                case GOLDEN_APPLE:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        // Se cambia al menu de eventos de uhc
                        player.closeInventory();
                        //abrir menu de UHC
                        player.sendMessage("switched to UHC menu");
                    }
                    break;

            }
        }

        else if(event.getView().getTitle().equals(GENERAL_MENU)){
            //guard clause para evitar errores

            if (event.getCurrentItem() == null) return;

            switch (event.getCurrentItem().getType()){
                case BARRIER:
                    //Se cierra el menu general
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        player.closeInventory();
                        player.sendMessage("general control menu was closed");
                    }
                    break;

                case GRASS_BLOCK:
                    // se cambia al menu principal
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        player.closeInventory();
                        player.sendMessage("switched to the main menu");
                        guiManager.openMainMenu(player);
                    }
                    break;

                case COW_SPAWN_EGG:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //se activan las vacas explosivas
                        QuickSurvival.toggleExplosiveCows();
                        reopenGeneralMenu(player);
                    }
                    break;

                case IRON_PICKAXE:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //se activa el veinMiner
                        QuickSurvival.toggleVeinMiner();
                        reopenGeneralMenu(player);
                    }
                    break;

                case IRON_AXE:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //se activa el treeCapitator
                        QuickSurvival.toggleTreeCapitator();
                        reopenGeneralMenu(player);
                    }
                    break;

            }
        }


    }

    public void reopenGeneralMenu(Player player){
        player.closeInventory();
        guiManager.openGeneralMenu(player);
    }
}
