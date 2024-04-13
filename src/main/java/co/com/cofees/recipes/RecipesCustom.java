package co.com.cofees.recipes;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.recipe.CraftingBookCategory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public abstract class RecipesCustom implements CustomRecipe {
    protected NamespacedKey key;
    protected ItemStack result;
    protected Map<Character, Material> ingredients;
    protected String[] shape;

    @Override
    public void registerRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(shape);
        ingredients.forEach(recipe::setIngredient);
        Bukkit.addRecipe(recipe);
    }

    @Override
    public void registerRecipe(char c, RecipeChoice.MaterialChoice materialChoice) {
        ShapedRecipe recipe = new ShapedRecipe(key, result);
        recipe.shape(shape);
        ingredients.forEach(recipe::setIngredient);
        recipe.setIngredient(c, materialChoice);
        recipe.setGroup("backpack");
        recipe.setCategory(CraftingBookCategory.EQUIPMENT);
        Bukkit.addRecipe(recipe);
    }

    public static RecipeChoice.MaterialChoice createChoice(Material material, NamespacedKey key) {
        return new RecipeChoice.MaterialChoice(material) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                if (itemStack == null || itemStack.getItemMeta() == null) {
                    return false;
                }
                PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
                return container.has(key, PersistentDataType.STRING);
            }
        };
    }
}
