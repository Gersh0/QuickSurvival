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

public class RegionScrollRecipe extends RecipesCustom {

    RegionScrollRecipe(Map<Character, Material> ingredients, String[] shape) {
        this.key = Keys.REGION_SCROLL;
        this.result = createRegionScrollItem();
        this.ingredients = ingredients;
        this.shape = shape;
    }

    public static ItemStack createRegionScrollItem() {
        ItemStack regionScroll = new ItemStack(Material.PAPER);
        ItemMeta regionScrollMeta = regionScroll.getItemMeta();
        if (regionScrollMeta == null) return null;

        regionScrollMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        regionScrollMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);

        regionScrollMeta.setDisplayName(getName());

        regionScrollMeta.getPersistentDataContainer().set(Keys.REGION_SCROLL, PersistentDataType.STRING, regionScrollMeta.getDisplayName());
        regionScrollMeta.setLore(getLore());
        regionScroll.setAmount(1);
        regionScroll.setItemMeta(regionScrollMeta);
        return regionScroll;
    }

    //the lore will explain what the item does
    private static List<String> getLore() {
        return List.of("A scroll to Make region",
                "Right click to instance a corner",
                "Shift right click to reset the region corners");
    }

    private static String getName() {
        return ChatColor.DARK_PURPLE + "Region Scroll";
    }

}
