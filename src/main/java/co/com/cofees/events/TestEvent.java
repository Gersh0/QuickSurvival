package co.com.cofees.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class TestEvent implements Listener {
    @EventHandler
    public void onSpawner(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        // Guard clause to check for the SILK_TOUCH enchantment and SPAWNER block
        if (!hasSilkTouchPickaxe(player, block) ) {
            return;
        }

        CreatureSpawner spawner = (CreatureSpawner) block.getState();

        //Create block and metadata for new Spawner
        ItemStack newSpawner = new ItemStack(block.getType());
        BlockStateMeta blockMeta = (BlockStateMeta) newSpawner.getItemMeta();

        assert blockMeta != null; //Metadata exists

        //Add data to new spawner
        blockMeta.setBlockState(spawner);
        newSpawner.setItemMeta(blockMeta);

        //Drop item
        block.getWorld().dropItem(e.getBlock().getLocation(), newSpawner);

    }

    private boolean hasSilkTouchPickaxe(Player player, Block block) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        /*Testing
        player.sendMessage(String.valueOf(mainHandItem.getType()));
        player.sendMessage(String.valueOf(block.getType().compareTo(Material.SPAWNER)));
         */
        if(mainHandItem.getItemMeta() == null)return false;
        if(mainHandItem.getType().compareTo(Material.AIR)==0)return false;
        return mainHandItem.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH) && block.getType().compareTo(Material.SPAWNER)==0;
    }
}

