package co.com.cofees.events;

import co.com.cofees.commands.RegionCommand;
import co.com.cofees.tools.*;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionScrollInteract implements Listener {

    //cooldown section
    private static final long COOLDOWN_IN_MS = 200; // 200 milliseconds
    private final Map<UUID, Long> lastInteractTimes = new HashMap<>();

    //event to interact with the region scroll
    //this event will be used to set the position of the region
    //the first position will be set with the first right click
    //the second position will be set with the second right click
    //the region will be created with the third right click


    //TODO see if the persistent data type can be used to store the location properly

    public static final Map<UUID, Tuple<Location, Location>> selections = new HashMap<>();

    @EventHandler
    public void onRegionScrollInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        //cooldown section
        if (lastInteractTimes.containsKey(playerUUID)) {
            long timeSinceLastInteract = System.currentTimeMillis() - lastInteractTimes.get(playerUUID);
            if (timeSinceLastInteract < COOLDOWN_IN_MS) {
                // If the player is still in cooldown, don't process the event
                return;
            }
        }



        //get the block the player is clicking
        Block block = event.getClickedBlock();
        if (block == null) return;
        Location location = block.getLocation();
        //check if the player is holding the region scroll
        ItemStack RegionScroll = player.getInventory().getItemInMainHand();
        ItemMeta RegionScrollMeta = RegionScroll.getItemMeta();
        if (RegionScrollMeta == null) return;
        PersistentDataContainer container = RegionScrollMeta.getPersistentDataContainer();
        //check if the player is right clicking
        Tuple<Location, Location> selection = selections.getOrDefault(playerUUID, new Tuple<>(null, null));

        if (event.getAction().toString().contains("RIGHT") && !player.isSneaking()&& container.has(Keys.REGION_SCROLL, PersistentDataType.STRING)) {
                //check if the player is holding the region scroll
                if (selection.getFirst() == null && selection.getSecond() == null) {
                    //set the first position


                    selection.setFirst(location);
                    player.sendMessage("First position set");
                    RegionScroll.setItemMeta(RegionScrollMeta);

                } else if (selection.getFirst() != null && selection.getSecond() == null) {
                    //set the second position

                    player.sendMessage("Second position set");
                    selection.setSecond(location);
                    RegionScroll.setItemMeta(RegionScrollMeta);

                } else {


                    Location pos1= selection.getFirst();
                    Location pos2= selection.getSecond();
                    //create the region
                    RegionScrollGui.makeAnvilGuiFirstRename(player, pos1, pos2);
                    //consume the region scroll just if is not in the creative mode
                    if (!player.getGameMode().toString().contains("CREATIVE")){
                    RegionScroll.setAmount(RegionScroll.getAmount() - 1);
                    return;
                    }

                    //delete the selections after saving
                    selection.setFirst(null);
                    selection.setSecond(null);

                }

            }

            //if the player performs a Right click with the main hand the selection will be reset
            if (event.getAction().toString().contains("RIGHT") && player.isSneaking()){

                RegionScroll.setItemMeta(RegionScrollMeta);
                selection.setFirst(null);
                selection.setSecond(null);
                player.sendMessage("Selection reset");


            }

            selections.put(player.getUniqueId(), selection);
            lastInteractTimes.put(playerUUID, System.currentTimeMillis());
        }



    }

