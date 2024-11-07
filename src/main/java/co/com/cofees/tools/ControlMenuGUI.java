package co.com.cofees.tools;

import co.com.cofees.QuickSurvival;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;

public class ControlMenuGUI {

    public void openMainMenu(Player player) {
        Inventory mainMenu = createMenu("Main Menu", 9);
        mainMenu.setItem(3, createMenuItem(Material.BARRIER, ChatColor.RED + "Close menu"));
        mainMenu.setItem(4, createMenuItem(Material.GRASS_BLOCK, ChatColor.GREEN + "Open general events menu"));
        mainMenu.setItem(5, createMenuItem(Material.GOLDEN_APPLE, ChatColor.GOLD + "Open UHC events menu"));
        fillDecorativePanes(mainMenu, 9, new int[]{3, 4, 5});
        player.openInventory(mainMenu);
        player.sendMessage("MainMenu was opened");
    }

    public void openGeneralMenu(Player player) {
        Inventory generalMenu = createMenu("General Menu", 36);
        generalMenu.setItem(31, createMenuItem(Material.BARRIER, ChatColor.RED + "Close menu"));
        generalMenu.setItem(4, createMenuItem(Material.GRASS_BLOCK, ChatColor.GREEN + "Back to the main menu"));
        generalMenu.setItem(10, createMenuItem(Material.RED_BED, ChatColor.GREEN + "Config % of players that need to sleep"));
        generalMenu.setItem(25, createToggleItem(Material.COW_SPAWN_EGG, ChatColor.RED + "Toggles Explosive cows", QuickSurvival.areCowsExplosive()));
        generalMenu.setItem(11, createToggleItem(Material.IRON_PICKAXE, ChatColor.GRAY + "Toggles veinMiner", QuickSurvival.isVeinMinerActive()));
        generalMenu.setItem(12, createToggleItem(Material.IRON_AXE, ChatColor.GRAY + "Toggles TreeCapitator", QuickSurvival.isTreeCapitatorActive()));
        fillDecorativePanes(generalMenu, 36, new int[]{4, 31, 10, 11, 12, 25});
        player.openInventory(generalMenu);
        player.sendMessage("GeneralMenu was opened");
    }

    public void openSleepEventMenu(Player player) {
        Inventory sleepMenu = createMenu("Sleep Menu", 27);
        sleepMenu.setItem(22, createMenuItem(Material.BARRIER, ChatColor.RED + "Close menu"));
        sleepMenu.setItem(4, createMenuItem(Material.GRASS_BLOCK, ChatColor.GREEN + "Back to the general menu"));
        int[] percentages = {1, 25, 33, 50, 66, 75, 100};
        for (int i = 0; i < percentages.length; i++) {
            sleepMenu.setItem(10 + i, createPercentageItem(percentages[i]));
        }
        fillDecorativePanes(sleepMenu, 27, new int[]{4, 22, 10, 11, 12, 13, 14, 15, 16});
        player.openInventory(sleepMenu);
        player.sendMessage("SleepMenu was opened");
    }

    private Inventory createMenu(String title, int size) {
        return Bukkit.createInventory(null, size, title);
    }

    private ItemStack createMenuItem(Material material, String displayName) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createToggleItem(Material material, String displayName, boolean isActive) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(displayName);
        meta.setLore(List.of(ChatColor.BLUE + "Currently " + (isActive ? "On" : "Off")));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPercentageItem(int percentage) {
        Material material = (percentage == QuickSurvival.getSleepingPercentage()) ? Material.GREEN_BED : Material.WHITE_BED;
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.BLUE + (percentage + "%"));
        meta.getPersistentDataContainer().set(Keys.PERCENTAGE, PersistentDataType.INTEGER, percentage);
        item.setItemMeta(meta);
        return item;
    }

    private void fillDecorativePanes(Inventory inventory, int size, int[] excludeSlots) {
        final ItemStack deco = createDecorativePane();
        for (int i = 0; i < size; i++) {
            final int index = i;
            if (Arrays.stream(excludeSlots).noneMatch(slot -> slot == index)) {
                inventory.setItem(index, deco);
            }
        }
    }

    public ItemStack createDecorativePane() {
        ItemStack deco = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta decoMeta = deco.getItemMeta();
        assert decoMeta != null;
        decoMeta.setDisplayName(" ");
        deco.setItemMeta(decoMeta);
        return deco;
    }
}
