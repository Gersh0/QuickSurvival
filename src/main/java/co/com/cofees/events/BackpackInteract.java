package co.com.cofees.events;

import co.com.cofees.QuickSurvival;
import co.com.cofees.tools.Keys;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BackpackInteract implements Listener {

    private static final HashMap<String, String> backpacks = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) throws IOException {
        //Guard Clause if the player is looking at a tilestate block and if is null we will open the backpack anyway
        Block b = event.getClickedBlock();
        if (b != null && b.getState() instanceof TileState) return;

        Player player = event.getPlayer();
        ItemStack handItem = player.getInventory().getItemInMainHand();

        if (handItem.getItemMeta() == null) return;

        //player.sendMessage("Item:" + Objects.requireNonNull(handItem.getItemMeta()).getPersistentDataContainer().getKeys());

        PersistentDataContainer container = handItem.getItemMeta().getPersistentDataContainer();

        // Check if the player is right-clicking with a backpack
        if (!event.getAction().name().contains("RIGHT")) return;

        for (int i = 1; i <= 5; i++) {
            if (!container.has(Keys.valueOf("BackpackLv" + i), PersistentDataType.STRING)) continue;
            backpacks.put(player.getUniqueId().toString(), container.get(Keys.BACKPACK_CODE, PersistentDataType.STRING));
            openBackpackInventory(player, i != 5 ? i : 6);
            event.setCancelled(true);
            break;
        }
    }

    private int roundInventorySize(int size) {
        if (size == 0) return 9;
        // Round up to the nearest multiple of 9
        return (int) Math.ceil((double) size / 9) * 9;
    }

    private void openBackpackInventory(Player player, int backpackLevel) throws IOException {
        int inventorySize = roundInventorySize(9 * backpackLevel);

        Inventory backpackInventory = Bukkit.createInventory(null, inventorySize, "Backpack");
        // Create a new backpack inventory if the player doesn't have one
        if (restoreInventory(player) != null) backpackInventory = restoreInventory(player);

        Inventory inventoryToOpen = backpackLevel > 1 ? Bukkit.createInventory(null, inventorySize, "Backpack") : backpackInventory;
        if (backpackLevel > 1) inventoryToOpen.setContents(backpackInventory.getContents());
        player.openInventory(inventoryToOpen);

        //player.sendMessage(org.bukkit.ChatColor.GREEN + "Backpack opened successfully!");//debug
    }

    //Evitar que ponga el backpack dentro de sí mismo
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase("Backpack")) return;

        if (event.getCurrentItem() == null) return;

        if (event.getCurrentItem().getItemMeta() == null) return;

        PersistentDataContainer container = event.getCurrentItem().getItemMeta().getPersistentDataContainer();

        //si(backpack clikeado == backpack abierto) entonces cancelar evento
        String UUIDbackpack = container.get(Keys.BACKPACK_CODE, PersistentDataType.STRING);
        String UUIDPlayer = event.getWhoClicked().getUniqueId().toString();

        //event.getWhoClicked().sendMessage(event.getAction().toString()); //debug
        // Check if the opened backpack is the same as the clicked backpack or applied other actions
        if (backpacks.get(UUIDPlayer).equals(UUIDbackpack)) {
            //event.getWhoClicked().sendMessage(event.getAction().toString());//debug
            event.setCancelled(true);
        }
    }

    //SECCION DE GUARDADO
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) throws IOException {
        if (event.getInventory().getHolder() != null)
            return; // poner otra condicion para limitar el inventario a solo backpacks
        //comprobar el persisten data container para guardar el inventario de la mochila

        //wareclouse if the inventory isn't a backpack inventory
        if (!event.getView().getTitle().equalsIgnoreCase("Backpack")) return;

        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        String UUIDbackpack = backpacks.get(player.getUniqueId().toString());

        if (UUIDbackpack == null) return;
        saveInventory(player, inventory);

    }

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

        String UUIDbackpack;
        if (!container.has(Keys.BACKPACK_CODE, PersistentDataType.STRING)) return null;

        UUIDbackpack = player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(Keys.BACKPACK_CODE, PersistentDataType.STRING);

        return UUIDbackpack;
    }

    public void saveInventory(Player p, Inventory inventory) throws IOException {
        //Guard clause if the player is looking at a tilestate block
        if (p.getTargetBlockExact(5) instanceof TileState) return;

        String UUIDbackpack = getBackpackUUID(p);
        File f = new File(getInventoryFolder(), p.getName() + UUIDbackpack + "BInventory.yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        List<Map<String, Object>> items = Stream.of(inventory.getContents())
                .map(item -> item != null ? item.serialize() : null)
                .collect(Collectors.toList());

        c.set("inventory.backpack", items);
        c.save(f);
    }

    public Inventory restoreInventory(Player p) throws IOException {
        String UUIDbackpack = getBackpackUUID(p);
        File f = new File(getInventoryFolder(), p.getName() + UUIDbackpack + "BInventory.yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);

        List<Map<String, Object>> items = (List<Map<String, Object>>) c.get("inventory.backpack");
        if (items == null) return null;

        int adjustedSize = roundInventorySize(items.size());
        Inventory inventory = Bukkit.createInventory(null, adjustedSize, "Backpack");

        IntStream.range(0, items.size())
                .filter(i -> items.get(i) != null)
                .forEach(i -> inventory.setItem(i, ItemStack.deserialize(items.get(i))));

        return inventory;
    }
}