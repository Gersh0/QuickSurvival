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

public class TreeCapitator implements Listener {

    private boolean isActive;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        if (isActive) {
            Player player = event.getPlayer();
            ItemStack tool = player.getInventory().getItemInMainHand();
            Block block = event.getBlock();
            String type = block.getType().name();

            if (!player.getGameMode().equals(GameMode.CREATIVE) && tool.getType().name().endsWith("_AXE") && type.endsWith("_LOG") && (player.isSneaking())) {

                //player.sendMessage("Entered the if statement of the treeCapitator");

                // if the block type is stripped, only the part of the name after "Stripped_" will be used
                if (type.startsWith("STRIPPED_")) {
                    type = type.substring(9);
                    player.sendMessage("The wood of the first block is stripped");
                }

                List<Block> logs = getLogs(block.getLocation(), type);
                logs.remove(block);

                if (hasLeaves(logs, type)) {
                    player.sendMessage("The tree has leaves and the treecapitator has started");

                    Damageable meta = (Damageable) tool.getItemMeta();
                    assert meta != null;
                    int dur = tool.getType().getMaxDurability() - 1 - meta.getDamage();

                    // Here it is changed if config will be applied to ignoreDurability
                    if (logs.size() > dur) {
                        logs = logs.subList(0, dur);
                    }

                    meta.setDamage(meta.getDamage() + logs.size());
                    tool.setItemMeta(meta);

                    for (Block log : logs) {
                        log.breakNaturally();
                    }
                }

            }
        }

    }

    public List<Block> getSurrounding(Location origin, String type) {
        List<Block> blocks = new ArrayList<>();

        for (int x = -1; x < 2; x++) {
            for (int y = 0; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Location loc = origin.clone().add(x, y, z);
                    Block block = loc.getBlock();
                    if (!origin.equals(loc) && block.getType().name().endsWith(type)) {
                        blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }

    public List<Block> getLogs(Location origin, String type) {
        List<Block> logs = new ArrayList<>();
        List<Block> next = getSurrounding(origin, type);

        while (!next.isEmpty()) {
            List<Block> nextNext = new ArrayList<>();
            for (Block log : next) {
                if (!logs.contains(log)) {
                    logs.add(log);
                    nextNext.addAll(getSurrounding(log.getLocation(), type));
                }
            }

            next = nextNext;

        }

        return logs;
    }

    public boolean hasLeaves(List<Block> logs, String type) {
        String leaveType = type.substring(0, type.length() - 4) + "_LEAVES";

        for (Block log : logs) {
            if (!getSurrounding(log.getLocation(), leaveType).isEmpty()) {
                return true;
            }
        }

        return false;

    }

    public void toggleTreeCapitator() {
        isActive = !isActive;
        if (isActive) {
            Bukkit.broadcastMessage("TreeCapitator is now active!");
        } else {
            Bukkit.broadcastMessage("TreeCapitator was turned off!");
        }
    }

    public boolean isActive() {
        return isActive;
    }

}