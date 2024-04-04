package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BackpackInteract implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        //Guard Clause if the player is looking at a tilestate block and if is null we will open the backpack anyway
        Block b = event.getClickedBlock();
        if (b != null && b.getState() instanceof TileState) return;


        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem.getItemMeta() == null) return;

        player.sendMessage("Objeto:" + Objects.requireNonNull(handItem.getItemMeta()).getPersistentDataContainer().getKeys());


        PersistentDataContainer container = handItem.getItemMeta().getPersistentDataContainer();

        // Verificar si el jugador interactuó con un "Backpack lv1"
        if (event.getAction().name().contains("RIGHT")) {
            if (container.has(Keys.BACKPACKLV1, PersistentDataType.STRING)) {
                // Abrir el inventario de la mochila
                openBackpackInventory(player, 1);
                event.setCancelled(true);
            } else if (container.has(Keys.BACKPACKLV2, PersistentDataType.STRING)) {
                openBackpackInventory(player, 2);
                event.setCancelled(true);
            } else if (container.has(Keys.BACKPACKLV3, PersistentDataType.STRING)) {
                openBackpackInventory(player, 3);
                event.setCancelled(true);
            } else if (container.has(Keys.BACKPACKLV4, PersistentDataType.STRING)) {
                // Abrir el inventario de la mochila
                openBackpackInventory(player, 4);
                event.setCancelled(true);
            } else if (container.has(Keys.BACKPACKLV5, PersistentDataType.STRING)) {
                // Abrir el inventario de la mochila
                openBackpackInventory(player, 6);
                event.setCancelled(true);
            }
        }
    }

    private int roundInventorySize(int size) {
        if (size == 0) return 9;
        // Redondear hacia arriba al múltiplo de 9 más cercano
        return (int) Math.ceil((double) size / 9) * 9;
    }

    private void openBackpackInventory(Player player, int backpackLevel) throws IOException {
        int inventorySize = roundInventorySize(9 * backpackLevel);



        Inventory backpackInventory = Bukkit.createInventory(null, inventorySize,"Backpack");
        // Crear el inventario de la mochila
        if (restoreInventory(player) != null) {
            backpackInventory = restoreInventory(player);
        }


        // Verificar si el jugador tiene un inventario guardado para esta mochila


        // Duplicar el inventario si el nivel es mayor que 1
        if (backpackLevel > 1) {
            Inventory duplicateInventory = Bukkit.createInventory(null, inventorySize, "Backpack");
            duplicateInventory.setContents(backpackInventory.getContents());
            player.openInventory(duplicateInventory);
        } else {
            // Abrir el inventario para el jugador
            player.openInventory(backpackInventory);
        }

        player.sendMessage(org.bukkit.ChatColor.GREEN + "Mochila abierta");
    }

    //SECCION DE GUARDADO


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) throws IOException {
        if (event.getInventory().getHolder() != null) return; // poner otra condicion para limitar el inventario a solo backpacks
        //comprobar el persisten data container para guardar el inventario de la mochila

        //wareclouse if the inventory isn't a backpack inventory
        if (!event.getView().getTitle().equalsIgnoreCase("Backpack")) return;


        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem.getItemMeta() == null)return;

        PersistentDataContainer container = Objects.requireNonNull(handItem.getItemMeta()).getPersistentDataContainer();

        if (container.has(Keys.BACKPACK_CODE, PersistentDataType.STRING)) {

            saveInventory(player, inventory);

            player.sendMessage("tipo de inventario cerrado: " + event.getInventory().getType().name());

            player.sendMessage("nombre de la clase: " + inventory.getClass().getName());

            player.sendMessage("holder: " + inventory.getHolder());

        }


    }

  /*  @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) throws IOException {

        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();

        ItemStack handItem = player.getInventory().getItemInMainHand();

        PersistentDataContainer container = Objects.requireNonNull(handItem.getItemMeta()).getPersistentDataContainer();
        player.sendMessage("inventario abierto");
        // Cargar el inventario al abrir la mochila
        if (container.has(Keys.BACKPACK_CODE, PersistentDataType.STRING)) {
            restoreInventory(player);
        }
    }*/


    private File getInventoryFolder() {
        File pluginFolder = QuickSurvival.getInstance().getDataFolder();
        File inventoryFolder = new File(pluginFolder, "inventory");

        if (!inventoryFolder.exists()) {
            inventoryFolder.mkdir();
        }

        return inventoryFolder;
    }


    //OBTENER UUID DEL BACKPACK
    public String getBackpackUUID(Player player) {
        PersistentDataContainer container = Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).getPersistentDataContainer();

        String UUIDbackpack = null;
        if (container.has(Keys.BACKPACK_CODE, PersistentDataType.STRING)) {
            UUIDbackpack = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(Keys.BACKPACK_CODE, PersistentDataType.STRING);
        } else {
            return null;
        }
        return UUIDbackpack;
    }

    public void saveInventory(Player p, Inventory inventory) throws IOException {

        //Wareclouse if the player is looking at a tilestate block
        Block b = p.getTargetBlockExact(5);
        if (b != null && b.getState() instanceof TileState) return;



        String UUIDbackpack = getBackpackUUID(p);

        File f = new File(getInventoryFolder(), p.getName() + UUIDbackpack + "BInventory" + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        List<Map<String, Object>> items = new ArrayList<>();
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                items.add(item.serialize());
            }
        }

        c.set("inventory.backpack", items);
        c.save(f);

        p.sendMessage("Inventario guardado exitosamente en: " + f.getAbsolutePath());
    }


    public Inventory restoreInventory(Player p) throws IOException {

        String UUIDbackpack = getBackpackUUID(p);

        File f = new File(getInventoryFolder(), p.getName() + UUIDbackpack + "BInventory" + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        List<Map<String, Object>> items = (List<Map<String, Object>>) c.get("inventory.backpack");
        if (items != null) {
            // Ajustar el tamaño para que sea un múltiplo de 9
            int adjustedSize = roundInventorySize(items.size());
            Inventory inventory = Bukkit.createInventory(null, adjustedSize,"Backpack");

            for (int i = 0; i < items.size(); i++) {
                ItemStack itemStack = ItemStack.deserialize(items.get(i));
                inventory.setItem(i, itemStack);
            }

            p.sendMessage("Inventario cargado exitosamente desde: " + f.getAbsolutePath());
            return inventory;
        }

        return null; // O manejar de otra manera si no se pudo cargar el inventario
    }


/*    public void saveInventory(Player p, Inventory inventory) throws IOException {

        File f = new File(getInventoryFolder(), p.getName() + "BInventory" + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("inventory.backpack", inventory);
        c.save(f);

        p.sendMessage("inventario guardado exitosamente en:" + f.getAbsolutePath());
    }

    @SuppressWarnings("unchecked")
    public void restoreInventory(Player p, Inventory inventory) throws IOException {
        File f = new File(getInventoryFolder(), p.getName() + "BInventory" + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        ItemStack[] content = ((List<ItemStack>) Objects.requireNonNull(c.get("inventory.backpack"))).toArray(new ItemStack[0]);
        inventory.setContents(content);
        p.sendMessage("inventario Cargado exitosamente desde:" + f.getAbsolutePath());
    }*/

}