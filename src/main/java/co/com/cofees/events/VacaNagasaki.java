package co.com.cofees.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class VacaNagasaki implements Listener {

    private boolean areCowsExplosive;

    @EventHandler
    public void onCowRightClick(PlayerInteractEntityEvent event){

        if(event.getRightClicked().getType() == EntityType.COW && areCowsExplosive){

            Cow cow = (Cow)event.getRightClicked();

            cow.getWorld().createExplosion(cow.getLocation(), 20F);
        }
    }

    public boolean toggleExplosiveCows(){
        areCowsExplosive = !areCowsExplosive;
        if(areCowsExplosive){
            Bukkit.broadcastMessage(ChatColor.RED+"Cows are now dangerous!!!");
        } else{
            Bukkit.broadcastMessage("Cows aren't dangerous anymore");
        }
        return areCowsExplosive;
    }

    public boolean areCowsExplosive(){
        return areCowsExplosive;
    }



}
