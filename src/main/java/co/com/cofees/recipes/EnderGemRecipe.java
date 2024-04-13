package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class EnderGemRecipe extends RecipesCustom {

    public EnderGemRecipe(Map<Character, Material> ingredients, String[] shape) {
        this.key = Keys.ENDER_GEM;
        this.result = createEnderGemItem();
        this.ingredients = ingredients;
        this.shape = shape;
    }

    private ItemStack createEnderGemItem() {
        ItemStack enderGem = new ItemStack(Material.ENDER_PEARL);
        ItemMeta enderGemMeta = enderGem.getItemMeta();
        if (enderGemMeta == null) return null;

        enderGemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        enderGemMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);

        enderGemMeta.setDisplayName(getName());

        enderGemMeta.getPersistentDataContainer().set(Keys.ENDER_GEM, PersistentDataType.STRING, enderGemMeta.getDisplayName());
        enderGemMeta.setLore(getLore());
        enderGem.setAmount(1);
        enderGem.setItemMeta(enderGemMeta);
        return enderGem;
    }

    private List<String> getLore() {
        return List.of("A special ender pearl for waystones");
    }

    private String getName() {
        return ChatColor.DARK_PURPLE + "Ender Gem";
    }
}