package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.ControlMenuGUI;
import co.com.cofees.tools.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ControlMenuHandler implements Listener {

    ControlMenuGUI guiManager = new ControlMenuGUI();

    //Menu titles
    final String MAIN_MENU = "Main Menu";
    final String GENERAL_MENU = "General Menu";
    final String SLEEP_MENU = "Sleep Menu";

    @EventHandler
    public void onMainMenuClick(InventoryClickEvent event){

        Player player = (Player)event.getWhoClicked();

        if(event.getView().getTitle().equals(MAIN_MENU)){

            player.sendMessage("You clicked on the main menu, on slot #"+event.getSlot());

            switch (Objects.requireNonNull(event.getCurrentItem()).getType()){
                case BARRIER:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //Close the main menu
                        player.closeInventory();
                        player.sendMessage("main control menu was closed");
                    }
                    break;

                case GRASS_BLOCK:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //Change to the general menu
                        reopenGeneralMenu(player);
                    }
                    break;

                case GOLDEN_APPLE:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        // Se cambia al menu de eventos de uhc
                        player.closeInventory();

                    }
                    break;

            }
        }
    }

    @EventHandler
    public void onGeneralMenuClick(InventoryClickEvent event){
        if(event.getView().getTitle().equals(GENERAL_MENU)){

            Player player = (Player)event.getWhoClicked();

            switch (Objects.requireNonNull(event.getCurrentItem()).getType()){
                case BARRIER:
                    //Close the general menu
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        player.closeInventory();
                        player.sendMessage("general control menu was closed");
                    }
                    break;

                case GRASS_BLOCK:
                    //Change to the main menu
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        player.closeInventory();
                        player.sendMessage("switched to the main menu");
                        guiManager.openMainMenu(player);
                    }
                    break;

                case COW_SPAWN_EGG:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //Enable or disable the explosive cows
                        QuickSurvival.toggleExplosiveCows();
                        reopenGeneralMenu(player);
                    }
                    break;

                case IRON_PICKAXE:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //Enable or disable the veinMiner
                        QuickSurvival.toggleVeinMiner();
                        reopenGeneralMenu(player);
                    }
                    break;

                case IRON_AXE:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //Enable or disable the treeCapitator
                        QuickSurvival.toggleTreeCapitator();
                        reopenGeneralMenu(player);
                    }
                    break;
                case RED_BED:
                    if(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10){
                        //Change to the sleep menu
                        player.closeInventory();
                        guiManager.openSleepEventMenu(player);
                    }
                    break;

            }
        }
    }

    @EventHandler
    public void onSleepMenuClick(InventoryClickEvent event){

        Player player = (Player)event.getWhoClicked();

        if(event.getView().getTitle().equals(SLEEP_MENU)){
            switch (Objects.requireNonNull(event.getCurrentItem()).getType()) {
                case BARRIER:
                    //Close the general menu
                    if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10) {
                        player.closeInventory();
                        player.sendMessage("general control menu was closed");
                    }
                    break;

                case GRASS_BLOCK:
                    //Change to the main menu
                    if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10) {
                        reopenGeneralMenu(player);
                    }
                    break;
                case WHITE_BED, GREEN_BED:
                    //Change the sleeping percentage
                    if (Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getCustomModelData() == 10) {
                        Integer percentage = event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(Keys.PERCENTAGE, PersistentDataType.INTEGER);
                        if (percentage != null) {
                            QuickSurvival.setSleepingPercentage(percentage);
                            reopenSleepMenu(player);
                        }
                    }
                    break;
            }
        }
    }

    public void reopenGeneralMenu(Player player){
        player.closeInventory();
        guiManager.openGeneralMenu(player);
    }

    public void reopenSleepMenu(Player player){
        player.closeInventory();
        guiManager.openSleepEventMenu(player);
    }
}
