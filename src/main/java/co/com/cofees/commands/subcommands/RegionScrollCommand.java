package co.com.cofees.commands.subcommands;

import co.com.cofees.recipes.RegionScrollRecipe;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RegionScrollCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (args.length != 0) {
            sender.sendMessage("§8[§6🛑§8] §7Usage: /region scroll");
            return true;
        }

        Player player = (Player) sender;

        //give the player a scroll
        ItemStack RegionScroll = RegionScrollRecipe.createRegionScrollItem();

        //if the player's inventory is full, drop the item
        if (player.getInventory().firstEmpty() == -1) {
            player.getWorld().dropItem(player.getLocation(), RegionScroll);
        } else {
            player.getInventory().addItem(RegionScroll);
        }

        return true;
    }

}
