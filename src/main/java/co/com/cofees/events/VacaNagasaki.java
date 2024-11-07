package co.com.cofees.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class VacaNagasaki implements Listener {

    private boolean isActive;

    @EventHandler
    public void onCowRightClick(PlayerInteractEntityEvent event){

        if(event.getRightClicked().getType() == EntityType.COW && isActive){

            Cow cow = (Cow)event.getRightClicked();

            cow.getWorld().createExplosion(cow.getLocation(), 20F);
        }
    }

    public void toggleExplosiveCows(){
        isActive = !isActive;
        if(isActive){
            Bukkit.broadcastMessage(ChatColor.RED+"Cows are now dangerous!!!");
        } else{
            Bukkit.broadcastMessage("Cows aren't dangerous anymore");
        }
    }

    public boolean areCowsExplosive(){
        return isActive;
    }



}
