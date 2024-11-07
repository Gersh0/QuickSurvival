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

public class WarpScrollRecipe extends RecipesCustom {

    public WarpScrollRecipe(Map<Character, Material> ingredients, String[] shape) {
        this.key = Keys.WARP_SCROLL;
        this.result = createWarpScrollItem();
        this.ingredients = ingredients;
        this.shape = shape;
    }

    private ItemStack createWarpScrollItem() {
        ItemStack warpScroll = new ItemStack(Material.PAPER);
        ItemMeta warpScrollMeta = warpScroll.getItemMeta();
        if (warpScrollMeta == null) return null;

        warpScrollMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        warpScrollMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);

        warpScrollMeta.setDisplayName(getName());

        warpScrollMeta.getPersistentDataContainer().set(Keys.WARP_SCROLL, PersistentDataType.STRING, warpScrollMeta.getDisplayName());
        warpScrollMeta.setLore(getLore());
        warpScroll.setAmount(1);
        warpScroll.setItemMeta(warpScrollMeta);
        return warpScroll;
    }

    private List<String> getLore() {
        return List.of("A scroll to warp to a waystone");
    }

    private String getName() {
        return ChatColor.DARK_PURPLE + "Warp Scroll";
    }
}