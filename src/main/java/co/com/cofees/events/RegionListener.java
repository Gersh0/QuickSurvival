package co.com.cofees.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import co.com.cofees.tools.Regions;
import co.com.cofees.tools.Region;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.List;

public final class RegionListener implements Listener {

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
        player.sendMessage("You are allowed to break blocks in this region: " + standingIn.getName());

    }

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
        player.sendMessage("You are allowed to put blocks in this region: " + standingIn.getName());

    }
}