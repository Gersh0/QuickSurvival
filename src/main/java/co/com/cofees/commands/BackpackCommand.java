package co.com.cofees.commands;

import co.com.cofees.QuickSurvival;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BackpackCommand implements CommandExecutor, Listener {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Este comando solo puede ser ejecutado por jugadores.");
            return true;
        }

        Player player = (Player) sender;

        // Crear y abrir el inventario de la mochila
        openBackpackInventory(player);

        return true;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', "&6Backpack"))) {
            event.setCancelled(true); // Evitar que los jugadores puedan mover los elementos en la mochila
        }
    }

    private void openBackpackInventory(Player player) {
        // Crear el inventario de la mochila
        Inventory backpackInventory = JavaPlugin.getPlugin(QuickSurvival.class).getServer().createInventory(null, 56, ChatColor.translateAlternateColorCodes('&', "&6Backpack"));

        // Agregar elementos o configurar el inventario según tus necesidades
        ItemStack exampleItem = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = exampleItem.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&bItem de Ejemplo"));
        exampleItem.setItemMeta(meta);

        // Colocar el item de ejemplo en la posición 13 (puedes ajustar esto según tus necesidades)
        backpackInventory.setItem(13, exampleItem);

        // Abrir el inventario para el jugador
        player.openInventory(backpackInventory);
    }
}
