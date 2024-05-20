package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;

public class RegionScrollGui {



    public static void makeAnvilGuiFirstRename(Player player, Location location1, Location location2) {

        Regions regions = Regions.getInstance();

        //paper that says rename name
        ItemStack paper = new ItemStack(Material.PAPER);
        ItemMeta paperMeta = paper.getItemMeta();
        paperMeta.setDisplayName(ChatColor.GREEN + "Set A Name");
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
                        return emptyList();
                    }

                    String newName = stateSnapshot.getText();
                    stateSnapshot.getPlayer().playSound(stateSnapshot.getPlayer().getLocation(), "block.anvil.use", 1, 1);
                    return Arrays.asList(
                            AnvilGUI.ResponseAction.close(),
                            AnvilGUI.ResponseAction.run(()->constructRegion(location1,location2,newName,stateSnapshot.getPlayer())

                    ));

                })
                .preventClose()
                .title("Set Region Name")
                .open(player)

        ;

    }

    public static void constructRegion(Location location1,Location location2,String name,Player player){
        Regions regions = Regions.getInstance();

        List<String> players = new java.util.ArrayList<>();
        players.add(player.getName());


        regions.saveRegion(new Region(name, location1, location2, players), QuickSurvival.regionsConfig, name);



        player.sendMessage("Region created");
    }





}
