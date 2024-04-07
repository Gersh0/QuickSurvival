package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerEvents implements Listener {
    private final NamespacedKey key = new NamespacedKey(QuickSurvival.getInstance(), "spawner");

    @EventHandler
    public void onBlockExp(BlockExpEvent event) {
        if (event.getBlock().getType() == Material.SPAWNER) {
            event.setExpToDrop(0);
        }
    }

    @EventHandler
    public void onSpawnerBreak(BlockBreakEvent event) {
        if (!hasSilkTouchPickaxe(event.getPlayer()) || event.getBlock().getType() != Material.SPAWNER) return;

        CreatureSpawner spawner = (CreatureSpawner) event.getBlock().getState();
        ItemStack item = new ItemStack(Material.SPAWNER);
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        if (spawner.getSpawnedType() == null) return;

        meta.setDisplayName(spawner.getSpawnedType().name());
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, spawner.getSpawnedType().name());
        item.setItemMeta(meta);
        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), item);
        event.setDropItems(false);
    }

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.SPAWNER) return;

        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if (!container.has(key, PersistentDataType.STRING)) return;

        CreatureSpawner spawner = (CreatureSpawner) event.getBlockPlaced().getState();
        spawner.setSpawnedType(EntityType.valueOf(container.get(key, PersistentDataType.STRING)));
        spawner.update();
    }

    private boolean hasSilkTouchPickaxe(Player player) {
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        return mainHandItem != null && mainHandItem.getItemMeta() != null && mainHandItem.getItemMeta().hasEnchant(Enchantment.SILK_TOUCH);
    }
}