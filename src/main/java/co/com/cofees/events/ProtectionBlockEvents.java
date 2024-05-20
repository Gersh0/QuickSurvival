package co.com.cofees.events;

import co.com.cofees.recipes.ProtectionBlockRecipe;
import co.com.cofees.tools.Keys;
import co.com.cofees.tools.RegionScrollGui;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProtectionBlockEvents implements Listener {

    @EventHandler
    public void onPlayerProtectionBlockPlacement(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getItemMeta() == null || !isProtectionBlock(item)) return;
        player.sendMessage("block is a protection block");
        PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();

        Location protectionLocation = event.getBlockPlaced().getLocation();
        List<String> players = new ArrayList<>();
        players.add(player.getName());
        switch (event.getBlockPlaced().getType()) {
            case BARREL:
                setDataContainer(1, event.getBlockPlaced());
                Location location1 = new Location(event.getBlock().getWorld(), protectionLocation.getX() - 10, protectionLocation.getY(), protectionLocation.getZ() - 10);
                Location location2 = new Location(event.getBlock().getWorld(), protectionLocation.getX() + 10, protectionLocation.getY() + 50, protectionLocation.getZ() + 10);
                player.sendMessage("Location 1: " + location1 + " Location 2: " + location2);
                RegionScrollGui.makeAnvilGuiFirstRename(player, location1, location2);
                break;
            case FURNACE:
                break;
            case DROPPER:
                break;
        }

    }

    public void setDataContainer(int type, Block block) {
        TileState state = (TileState) (block.getState());
        String name = ProtectionBlockRecipe.getName(type);
        switch (type) {
            case 1:
                state.getPersistentDataContainer().set(Keys.IRON_PROTECTION_BLOCK, PersistentDataType.STRING, name);
                break;
            case 2:
                state.getPersistentDataContainer().set(Keys.DIAMOND_PROTECTION_BLOCK, PersistentDataType.STRING, name);
                break;
            case 3:
                state.getPersistentDataContainer().set(Keys.NETHERITE_PROTECTION_BLOCK, PersistentDataType.STRING, name);
                break;
        }
        state.update();
    }

    @EventHandler
    public void onPlayerProtectionBlockBreak(BlockBreakEvent event) {

        Block block = event.getBlock();
        if (!isProtectionBlock(block)) return;
        event.getPlayer().sendMessage("block broken is a protection block");
        event.setDropItems(false);

        ItemStack protectionBlock = switch (block.getType()) {
            case BARREL -> ProtectionBlockRecipe.createProtectionBlock(1);
            case FURNACE -> ProtectionBlockRecipe.createProtectionBlock(2);
            case DROPPER -> ProtectionBlockRecipe.createProtectionBlock(3);
            default -> null;
        };
        if (protectionBlock == null) return;


        event.getPlayer().sendMessage("Protection Block Broken");
        block.getWorld().dropItemNaturally(block.getLocation(), protectionBlock);
        event.getPlayer().sendMessage(Objects.requireNonNull(Objects.requireNonNull(protectionBlock.getItemMeta()).getLore()).get(1));


    }

    @EventHandler
    public void onPlayerProtectionBlockPickup(EntityPickupItemEvent event) {

    }

    private boolean isProtectionBlock(ItemStack item) {
        PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta()).getPersistentDataContainer();
        return container.has(Keys.IRON_PROTECTION_BLOCK, PersistentDataType.STRING) ||
                container.has(Keys.DIAMOND_PROTECTION_BLOCK, PersistentDataType.STRING) ||
                container.has(Keys.NETHERITE_PROTECTION_BLOCK, PersistentDataType.STRING);
    }

    private boolean isProtectionBlock(Block block) {
        if (block.getState() instanceof TileState tileState) {
            PersistentDataContainer container = tileState.getPersistentDataContainer();
            return container.has(Keys.IRON_PROTECTION_BLOCK, PersistentDataType.STRING) ||
                    container.has(Keys.DIAMOND_PROTECTION_BLOCK, PersistentDataType.STRING) ||
                    container.has(Keys.NETHERITE_PROTECTION_BLOCK, PersistentDataType.STRING);
        } else {
            return false;
        }
    }

    private void consumeItemInHand(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }

    private boolean isRightClick(PlayerInteractEvent event) {
        return event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR;
    }


}
