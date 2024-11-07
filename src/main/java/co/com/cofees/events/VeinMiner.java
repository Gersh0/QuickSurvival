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

import java.util.ArrayList;
import java.util.List;

public class VeinMiner implements Listener {

    private boolean isActive;
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if(isActive){
            Player player = event.getPlayer();
            ItemStack tool = player.getInventory().getItemInMainHand();
            Block block = event.getBlock();
            String type = block.getType().name();

            if(!player.getGameMode().equals(GameMode.CREATIVE) && tool.getType().name().endsWith("_PICKAXE") && type.endsWith("_ORE") && (player.isSneaking())){

                //player.sendMessage("You mined: "+type);

                List<Block> ores = getOres(block.getLocation(), type);
                ores.remove(block);

                Damageable meta = (Damageable)tool.getItemMeta();
                assert meta != null;
                int dur = tool.getType().getMaxDurability() - 1 - meta.getDamage();

                //Here you can change if you want to apply config to ignoreDurability
                if(ores.size()>dur){
                    ores = ores.subList(0,dur);
                }

                meta.setDamage(meta.getDamage() + ores.size());
                tool.setItemMeta(meta);

                for(Block ore: ores){
                    ore.breakNaturally(tool);
                }

            }
        }

    }

    public List<Block> getSurrounding(Location origin, String type){
        List<Block> blocks = new ArrayList<>();

        for (int x = -1; x<2; x++){
            for(int y=-1; y<2; y++){
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

    public List<Block> getOres(Location origin, String type){
        List<Block> ores = new ArrayList<>();
        List<Block> next = getSurrounding(origin,type);

        while(!next.isEmpty()){
            List<Block> nextNext = new ArrayList<>();
            for(Block ore : next){
                if(!ores.contains(ore)){
                    ores.add(ore);
                    nextNext.addAll(getSurrounding(ore.getLocation(), type));
                }
            }

            next = nextNext;

        }

        return ores;
    }

    public void toggleVeinMiner(){
        isActive = !isActive;
        if(isActive){
            Bukkit.broadcastMessage("VeinMiner is now active!");
        } else{
            Bukkit.broadcastMessage("VeinMiner was turned off!");
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
