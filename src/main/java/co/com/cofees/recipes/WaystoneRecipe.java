package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Map;

public class WaystoneRecipe extends RecipesCustom {

    public WaystoneRecipe(Map<Character, Material> ingredients, String[] shape) {
        this.key = Keys.WAYSTONE;
        this.result = createWaystoneItem();
        this.ingredients = ingredients;
        this.shape = shape;
    }

    private ItemStack createWaystoneItem() {
        ItemStack waystone = new ItemStack(Material.BLACK_BANNER);
        ItemMeta waystoneMeta = waystone.getItemMeta();

        if (waystoneMeta == null) return null;

        waystoneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        waystoneMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);

        waystoneMeta.setDisplayName(getName());

        waystoneMeta.getPersistentDataContainer().set(Keys.WAYSTONE, PersistentDataType.STRING, waystoneMeta.getDisplayName());
        waystoneMeta.setLore(getLore());
        waystone.setAmount(1);
        waystone.setItemMeta(waystoneMeta);
        return waystone;
    }

    private List<String> getLore() {
        return List.of("Place this to create a waystone");
    }

    private String getName() {
        return "Waystone";
    }
}