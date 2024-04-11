package co.com.cofees.events;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import co.com.cofees.tools.Regions;
import co.com.cofees.tools.Region;

public final class RegionListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Regions regions = Regions.getInstance();

        Region standingIn = regions.findRegion(event.getBlock().getLocation());

        if (standingIn != null) {
           //say the player is in a region
            player.sendMessage("You are in a region" + standingIn.getName());

            event.setCancelled(true);
        }
    }
}