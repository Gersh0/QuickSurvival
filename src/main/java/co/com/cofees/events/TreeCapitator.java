package co.com.cofees.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TreeCapitator implements Listener {

    private boolean isActive;
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if(isActive){
            Player player = event.getPlayer();
            ItemStack tool = player.getInventory().getItemInMainHand();
            Block block = event.getBlock();
            String type = block.getType().name();

            if(!player.getGameMode().equals(GameMode.CREATIVE) && tool.getType().name().endsWith("_AXE") && type.endsWith("_LOG") && (player.isSneaking())){

                //player.sendMessage("Se entro al if del treeCapitator");

                // si el tipo de bloque es stripped, se utilizará solo la parte del nombre después del "Stripped_"
                if(type.startsWith("STRIPPED_")){
                    type = type.substring(9);
                    player.sendMessage("La madera del primer bloque es stripped");
                }

                List<Block> logs = getLogs(block.getLocation(), type);
                logs.remove(block);

                if(hasLeaves(logs, type)){
                    player.sendMessage("el arbol tiene hojas y se inicio el treecapitator");

                    Damageable meta = (Damageable)tool.getItemMeta();
                    assert meta != null;
                    int dur = tool.getType().getMaxDurability() - 1 - meta.getDamage();

                    //Aqui se cambia si se le va a aplicar config al ignoreDurability
                    if(logs.size()>dur){
                        logs = logs.subList(0,dur);
                    }

                    meta.setDamage(meta.getDamage() + logs.size());
                    tool.setItemMeta((ItemMeta) meta);

                    for(Block log: logs){
                        log.breakNaturally();
                    }
                }

            }
        }

    }

    public List<Block> getSurrounding(Location origin, String type){
        List<Block> blocks = new ArrayList<>();

        for (int x = -1; x<2; x++){
            for(int y=0; y<2; y++){
               for(int z=-1;z<2;z++){
                   Location loc = origin.clone().add(x,y,z);
                   Block block = loc.getBlock();
                   if(!origin.equals(loc) && block.getType().name().endsWith(type)){
                       blocks.add(block);
                   }
               }
            }
        }

        return blocks;
    }

    public List<Block> getLogs(Location origin, String type){
        List<Block> logs = new ArrayList<>();
        List<Block> next = getSurrounding(origin,type);

        while(!next.isEmpty()){
            List<Block> nextNext = new ArrayList<>();
            for(Block log : next){
                if(!logs.contains(log)){
                    logs.add(log);
                    nextNext.addAll(getSurrounding(log.getLocation(), type));
                }
            }

            next = nextNext;

        }

        return logs;
    }

    public boolean hasLeaves(List<Block> logs, String type){
        String leaveType = type.substring(0,type.length()-4)+"_LEAVES";

        for(Block log : logs){
            if(!getSurrounding(log.getLocation(), leaveType).isEmpty()){
                return true;
            }
        }

        return false;

    }

    public boolean toggleTreeCapitator(){
        isActive = !isActive;
        if(isActive){
            Bukkit.broadcastMessage("TreeCapitator is now active!");
        } else{
            Bukkit.broadcastMessage("TreeCapitator was turned off!");
        }
        return isActive;
    }

    public boolean isActive(){
        return isActive;
    }

}
