package co.com.cofees.commands;

import co.com.cofees.tools.Keys;
import co.com.cofees.tools.TextTools;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WaystoneBannerInteract implements CommandExecutor, TabCompleter {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) throws CommandException {
        //Issued from console
        if (!(sender instanceof Player player)) {
            sender.sendMessage(text("&4Can't use this command from console."));
            return true;
        }

        //Player issued command
        if (args.length <= 0) {
            player.sendMessage(text("&bTest Successfull."));
            return true;
        }

        switch (args[0]) {
            case "open":
                Block b = player.getTargetBlockExact(5);
                if (b == null) {
                    player.sendMessage("Apunta a un bloque");
                    return true;
                }

                if (!(b.getState() instanceof TileState tileState)) {
                    player.sendMessage("Este bloque no es una Waystone");
                    return true;
                }

                player.sendMessage("este bloque si es un tilestate");
                PersistentDataContainer container = tileState.getPersistentDataContainer();

                if (!container.has(Keys.WAYSTONE, PersistentDataType.STRING)) {
                    player.sendMessage("Este bloque no tiene Key: WAYSTONE");
                    return true;
                }

                openWaystoneInventory(player);
                break;

            case "set":
                Block block = player.getTargetBlockExact(5);
                if (block == null) {
                    player.sendMessage("Apunta a un bloque");
                    return true;
                }

                if (!(block.getState() instanceof TileState tileStateSet)) {
                    player.sendMessage("Este bloque no tiene Tilestate");
                    return true;
                }

                player.sendMessage("este bloque si es un tilestate");
                tileStateSet.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, "true");
                tileStateSet.update();
                player.sendMessage("Bloque asignado como Waystone");
                break;

            default:
                noArgs(player);
                break;
        }

        return true;
    }

    //Abrir Menu de la waystone
    private void openWaystoneInventory(Player player) {
        // Crear el inventario del "Waystone"
        Inventory waystoneInventory = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&4WaystoneMenu"));

        // agregar elementos

        // Abrir el inventario para el jugador
        player.openInventory(waystoneInventory);
    }


    public void noArgs(CommandSender sender) {
        sender.sendMessage(text("&4&lUse &r/test help &4&lfor usage of command"));
    }

    public String text(String text) {
        return TextTools.coloredText(text);
    }

    //COMPLETAR COMANDO
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, @NotNull String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("waystone") && args.length == 1) {
            List<String> completions = new ArrayList<>();
            completions.add("open");
            completions.add("set");
            return completions;
        }

        return Collections.emptyList();
    }
}