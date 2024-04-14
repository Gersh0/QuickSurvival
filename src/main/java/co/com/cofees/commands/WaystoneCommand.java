package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.TextTools;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class WaystoneCommand implements CommandExecutor, Listener, TabCompleter {

    //todo: check usability of this command
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        //Issued from console
        if (!(sender instanceof Player)) {
            sender.sendMessage(text("&4Can't use this command from console."));
            return true;
        }

        //Player issued command
        Player player = (Player) sender;
        if (args.length <= 0) {
            player.sendMessage(text("&bTest Successfull."));
            return true;
        }

        switch (args[0]) {
            case "waystone":
                //crea inventario
                Inventory inv = Bukkit.createInventory(null, 45, ChatColor.translateAlternateColorCodes('&', "&2Inventario Prueba"));
                //crea un bloque que va a ser usado como opcion
                Material materiale = Material.REDSTONE_BLOCK;
                ItemStack item = new ItemStack(materiale, 1);
                ItemMeta meta = item.getItemMeta();

                // Crear un botón para abrir otro inventario
                Material botonMaterial = Material.DIAMOND;
                ItemStack botonItem = new ItemStack(botonMaterial, 1);
                ItemMeta botonMeta = botonItem.getItemMeta();
                botonMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bAbrir otro inventario"));
                botonItem.setItemMeta(botonMeta);
                inv.setItem(22, botonItem);

                // Botón "Crear Waypoint"
                Material crearWaypointMaterial = Material.RED_WOOL;
                ItemStack crearWaypointItem = new ItemStack(crearWaypointMaterial, 1);
                ItemMeta crearWaypointMeta = crearWaypointItem.getItemMeta();
                crearWaypointMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&cCrear Waypoint"));
                crearWaypointItem.setItemMeta(crearWaypointMeta);
                inv.setItem(24, crearWaypointItem);

                //titulo del inventario
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&4&lPrueba"));
                item.setItemMeta(meta);
                //agegar bloque al inventario
                inv.setItem(20, item);

                //seccion de decoracion
                Material deco = Material.BLACK_STAINED_GLASS_PANE;
                ItemStack decor = new ItemStack(deco, 1);
                for (int i = 0; i < 9; i++) {
                    inv.setItem(i, decor);
                }
                for (int i = 36; i < 45; i++) {
                    inv.setItem(i, decor);
                }
                inv.setItem(9, decor);
                inv.setItem(18, decor);
                inv.setItem(27, decor);
                inv.setItem(36, decor);
                inv.setItem(17, decor);
                inv.setItem(26, decor);
                inv.setItem(35, decor);

                //abrir inventario
                player.openInventory(inv);

                player.sendMessage(text("&b&Waypoint Entregado&b"));
                break;

            default:
                noArgs(player);
                break;
        }

        return true;
    }
    //COMPLETAR COMANDO
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("inventory") && args.length == 1) {
            // Si el comando es /inventory y estamos en el primer argumento, sugerir /waystone
            List<String> completions = new ArrayList<>();
            completions.add("waystone");
            return completions;
        }

        return Collections.emptyList();
    }

    public void noArgs(CommandSender sender) {
        sender.sendMessage(text("&4&lUse &r/test help &4&lfor usage of command"));
    }

    public String text(String text) {
        return TextTools.coloredText(text);
    }

    //Seccion del evento para bloquear interacciones



}