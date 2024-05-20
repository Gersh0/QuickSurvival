package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.type.Switch;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class ProtectionBlockRecipe extends RecipesCustom {

    public ProtectionBlockRecipe(int type, Map<Character, Material> ingredients, String[] shape) {
        this.result =createProtectionBlock(type);
        this.ingredients = ingredients;
        this.shape = shape;
        switch (type){
            case 1:
                this.key = Keys.IRON_PROTECTION_BLOCK;
                break;
            case 2:
                this.key = Keys.DIAMOND_PROTECTION_BLOCK;
                break;
            case 3:
                this.key = Keys.NETHERITE_PROTECTION_BLOCK;
                break;
        }
    }

    public static ItemStack createProtectionBlock(int type) {
        ItemStack protectionBlock = switch (type) {
            case 1 -> new ItemStack(Material.BARREL);
            case 2 -> new ItemStack(Material.FURNACE);
            case 3 -> new ItemStack(Material.DROPPER);
            default -> null;
        };
        assert protectionBlock != null;
        ItemMeta protectionBlockMeta = protectionBlock.getItemMeta();
        assert protectionBlockMeta != null;
        protectionBlockMeta.setDisplayName(getName(type));
        protectionBlockMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        protectionBlockMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        protectionBlockMeta.setCustomModelData(10);
        protectionBlockMeta.setLore(getLore(type));
        protectionBlock.setAmount(1);

        switch (type){
            case 1:
                protectionBlockMeta.getPersistentDataContainer().set(Keys.IRON_PROTECTION_BLOCK, PersistentDataType.STRING, protectionBlockMeta.getDisplayName());
                break;
            case 2:
                protectionBlockMeta.getPersistentDataContainer().set(Keys.DIAMOND_PROTECTION_BLOCK, PersistentDataType.STRING, protectionBlockMeta.getDisplayName());
                break;
            case 3:
                protectionBlockMeta.getPersistentDataContainer().set(Keys.NETHERITE_PROTECTION_BLOCK, PersistentDataType.STRING, protectionBlockMeta.getDisplayName());
                break;

        }

        protectionBlock.setItemMeta(protectionBlockMeta);
        return protectionBlock;
    }

    public static String getName(int type) {
        return switch (type) {
            case 1 -> (ChatColor.WHITE + "Level 1 protection block");
            case 2 -> (ChatColor.DARK_GRAY + "" + ChatColor.BLUE + "Level 2 protection block");
            case 3 -> (ChatColor.GOLD + "" + ChatColor.BLACK + "Level 3 protection block");
            default -> null;
        };
    }

    private static List<String> getLore(int type) {
        return switch (type) {
            case 1 -> List.of("Protects in a radius of 10 blocks, with a height of 50.");
            case 2 -> List.of("Protects in a radius of 20 blocks, with a height of 100.");
            case 3 -> List.of("Protects in a radius of 40 blocks, with a height of 200.");
            default -> null;
        };
    }
}

