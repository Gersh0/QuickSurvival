package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BackpackRecipe extends RecipesCustom {
    public BackpackRecipe(int lvl, Map<Character, Material> ingredients, String[] shape) {
        this.key = getKey(lvl);
        this.result = getItem(lvl);
        this.ingredients = ingredients;
        this.shape = shape;
        registerRecipe();
    }

    public BackpackRecipe(int lvl, Map<Character, Material> ingredients, String[] shape, char key, RecipeChoice.MaterialChoice materialChoice) {
        this.key = getKey(lvl);
        this.result = getItem(lvl);
        this.ingredients = ingredients;
        this.shape = shape;
        registerRecipe(key, materialChoice);
    }

    public RecipeChoice.MaterialChoice createChoice(Material material, NamespacedKey key, PersistentDataType<String, String> type) {
        return new RecipeChoice.MaterialChoice(material) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                if (itemStack == null) {
                    return false;
                }
                PersistentDataContainer container = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
                return container.has(key, type);
            }
        };
    }

    private ItemStack getItem(int lvl) {
        return switch (lvl) {
            case 1 -> new ItemStack(Material.LEATHER);
            case 2 -> new ItemStack(Material.LEATHER);
            case 3 -> new ItemStack(Material.LEATHER);
            case 4 -> new ItemStack(Material.LEATHER);
            case 5 -> new ItemStack(Material.LEATHER);
            default -> null;
        };
    }

    private ItemStack createBackpackItem(int lvl, String lore) {
        ItemStack backpack = new ItemStack(Material.CLOCK);
        ItemMeta backpackMeta = backpack.getItemMeta();
        if (backpackMeta == null) return null;

        if (lvl >= 3 && lvl <= 5) {
            backpackMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            backpackMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        }

        backpackMeta.setDisplayName(getName(lvl));

        if (lvl == 1) backpackMeta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, "true");

        backpackMeta.getPersistentDataContainer().set(getKey(lvl), PersistentDataType.STRING, "true");
        backpackMeta.setLore(List.of(lore));
        backpack.setAmount(1);
        backpackMeta.setCustomModelData(lvl);
        backpack.setItemMeta(backpackMeta);
        return backpack;
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
