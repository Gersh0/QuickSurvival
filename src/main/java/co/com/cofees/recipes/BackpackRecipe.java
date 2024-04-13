package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class BackpackRecipe extends RecipesCustom {

    public BackpackRecipe(int lvl, Map<Character, Material> ingredients, String[] shape) {
        this.key = getKey(lvl);
        this.result = createBackpackItem(lvl, getLore(lvl));
        this.ingredients = ingredients;
        this.shape = shape;

    }

    private ItemStack createBackpackItem(int lvl, List<String> lore) {
        ItemStack backpack = new ItemStack(Material.CLOCK);
        ItemMeta backpackMeta = backpack.getItemMeta();
        if (backpackMeta == null) return null;

        if (lvl >= 3 && lvl <= 5) {
            backpackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            backpackMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        }

        backpackMeta.setDisplayName(getName(lvl));

        if (lvl == 1)
            backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, "true");
        NamespacedKey key = getKey(lvl);
        if (key == null) return null;
        backpackMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, "true");
        backpackMeta.setLore(lore);
        backpack.setAmount(1);
        backpackMeta.setCustomModelData(lvl);
        backpack.setItemMeta(backpackMeta);
        return backpack;
    }

    private List<String> getLore(int lvl) {
        return switch (lvl) {
            case 1 -> List.of("A simple backpack");
            case 2 -> List.of("A backpack with a little more space");
            case 3 -> List.of("A backpack with a lot of space");
            case 4 -> List.of("A backpack with a lot of space and a little more");
            case 5 -> List.of("A backpack with a lot of space and a little more", "and a little more");
            default -> null;
        };
    }

    private String getName(int lvl) {
        return switch (lvl) {
            case 1 -> (ChatColor.BOLD + "Backpack lv1");
            case 2 -> (ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Backpack lv2");
            case 3 -> (ChatColor.GOLD + "" + ChatColor.BOLD + "Backpack lv3");
            case 4 -> (ChatColor.BLUE + "" + ChatColor.BOLD + "Backpack lv4");
            case 5 -> (ChatColor.BLUE + "" + ChatColor.MAGIC + "Backpack lv5");
            default -> null;
        };
    }

    private NamespacedKey getKey(int lvl) {
        return switch (lvl) {
            case 1 -> Keys.BACKPACKLV1;
            case 2 -> Keys.BACKPACKLV2;
            case 3 -> Keys.BACKPACKLV3;
            case 4 -> Keys.BACKPACKLV4;
            case 5 -> Keys.BACKPACKLV5;
            default -> null;
        };
    }

}
