package co.com.cofees.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import co.com.cofees.tools.Regions;
import co.com.cofees.tools.Region;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public final class RegionListener implements Listener {
    //this is the event that will be triggered when a player breaks a block
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Regions regions = Regions.getInstance();

        Region standingIn = regions.findRegion(event.getBlock().getLocation());

        if (standingIn == null) return;

        List<String> players = standingIn.getPlayers();

        if (!players.contains(player.getName())
        ) {
            //say the player is in a region
            player.sendMessage("You are in a region: " + standingIn.getName() + "only this players can be here:" + players);

            event.setCancelled(true);
            return;
        }
        player.sendMessage("you are allowed to break blocks in this region: " + standingIn.getName());

    }
    //this is the event that will be triggered when a player places a block
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Regions regions = Regions.getInstance();

        Region standingIn = regions.findRegion(event.getBlock().getLocation());

        if (standingIn == null) return;

        List<String> players = standingIn.getPlayers();



        if (!players.contains(player.getName())) {
           //say the player is in a region
            player.sendMessage("You are in a region" + standingIn.getName());

            event.setCancelled(true);
            return;
        }
        player.sendMessage("you are allowed to put blocks in this region: " + standingIn.getName());

    }
    //this is the event that will be triggered when a player interacts with a block
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {

        Regions regions = Regions.getInstance();

        Region standingIn = regions.findRegion(event.getBlock().getLocation());

        if (standingIn == null) return;

        event.setCancelled(true);

    }

}