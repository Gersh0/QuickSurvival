package co.com.cofees.events;

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
        Block block = e.getBlock();
        Player player = e.getPlayer();
        if ((player.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH))) {
            CreatureSpawner x = (CreatureSpawner) block.getState();
            ItemStack spawner = new ItemStack(block.getType());
            BlockStateMeta blockMeta = (BlockStateMeta) spawner.getItemMeta();
            blockMeta.setBlockState(x);
            spawner.setItemMeta(blockMeta);
            block.getWorld().dropItem(e.getBlock().getLocation(), spawner);
        }
    }
}

