package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class CustomRecipes {

    public static void registerCustomCrafting() {
        backpacks();
    }

    public static void backpacks() {
        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv1 = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta backpackLv1Meta = backpackLv1.getItemMeta();
        backpackLv1Meta.setDisplayName(ChatColor.BOLD + "Backpack lv1");

        backpackLv1Meta.getPersistentDataContainer().set(Keys.BACKPACKLV1, PersistentDataType.STRING, "true");
        backpackLv1Meta.setLore(List.of("UNA MOCHILA EN MAICRA?"));
        backpackLv1.setAmount(1);
        //setear meta
        backpackLv1.setItemMeta(backpackLv1Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl1 = new ShapedRecipe(Keys.BACKPACKLV1Recipe, backpackLv1);
        bl1.shape("CCC", "SHS", "CCC");
        bl1.setIngredient('H', Material.CHEST);
        bl1.setIngredient('C', Material.LEATHER);
        bl1.setIngredient('S', Material.STRING);
        // Registrar el crafteo
        Bukkit.addRecipe(bl1);

        //backpack lv2

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv2 = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta backpackLv2Meta = backpackLv2.getItemMeta();
        backpackLv2Meta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Backpack lv2");

        backpackLv2Meta.getPersistentDataContainer().set(Keys.BACKPACKLV2, PersistentDataType.STRING, "true");
        backpackLv2Meta.setLore(List.of("UNA MOCHILA MAS GRANDE"));
        backpackLv2.setAmount(1);
        //setear meta
        backpackLv2.setItemMeta(backpackLv2Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl2 = new ShapedRecipe(Keys.BACKPACKLV2Recipe, backpackLv2);
        bl2.shape("HHH", "HMH", "HHH");
        bl2.setIngredient('H', Material.IRON_INGOT);
        bl2.setIngredient('M', new RecipeChoice.ExactChoice(backpackLv1));

        // Registrar el crafteo
        Bukkit.addRecipe(bl2);

        //backpack lv3

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv3 = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta backpackLv3Meta = backpackLv3.getItemMeta();
        backpackLv3Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv3Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv3Meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Backpack lv3");

        backpackLv3Meta.getPersistentDataContainer().set(Keys.BACKPACKLV3, PersistentDataType.STRING, "true");
        backpackLv3Meta.setLore(List.of("UNA MOCHILA AUN MAS GRANDE"));
        backpackLv3.setAmount(1);
        //setear meta
        backpackLv3.setItemMeta(backpackLv3Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl3 = new ShapedRecipe(Keys.BACKPACKLV3Recipe, backpackLv3);
        bl3.shape("OOO", "OMO", "OOO");
        bl3.setIngredient('O', Material.GOLD_INGOT);
        bl3.setIngredient('M', new RecipeChoice.ExactChoice(backpackLv2));

        // Registrar el crafteo
        Bukkit.addRecipe(bl3);

        //backpack lv4

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv4 = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta backpackLv4Meta = backpackLv4.getItemMeta();
        backpackLv4Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv4Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv4Meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Backpack lv4");

        backpackLv4Meta.getPersistentDataContainer().set(Keys.BACKPACKLV4, PersistentDataType.STRING, "true");
        backpackLv4Meta.setLore(List.of("UNA MOCHILA INCLUSO MAS GRANDE? CUANDO TERMINA?"));
        backpackLv4.setAmount(1);
        //setear meta
        backpackLv4.setItemMeta(backpackLv4Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl4 = new ShapedRecipe(Keys.BACKPACKLV4Recipe, backpackLv4);
        bl4.shape("DDD", "DMD", "DDD");
        bl4.setIngredient('D', Material.DIAMOND);
        bl4.setIngredient('M', new RecipeChoice.ExactChoice(backpackLv3));

        // Registrar el crafteo
        Bukkit.addRecipe(bl4);

        //backpack lv4

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv5 = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta backpackLv5Meta = backpackLv5.getItemMeta();
        backpackLv5Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv5Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv5Meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.MAGIC + "Backpack lv5");

        backpackLv5Meta.getPersistentDataContainer().set(Keys.BACKPACKLV5, PersistentDataType.STRING, "true");
        backpackLv5Meta.setLore(List.of("LA MOCHILA DEFINITIVA"));
        backpackLv5.setAmount(1);
        //setear meta
        backpackLv5.setItemMeta(backpackLv5Meta);

        // Crear el crafteo personalizado
        ShapedRecipe bl5 = new ShapedRecipe(Keys.BACKPACKLV5Recipe, backpackLv5);
        bl5.shape("GMN");
        bl5.setIngredient('N', Material.NETHERITE_INGOT);
        bl5.setIngredient('M', new RecipeChoice.ExactChoice(backpackLv4));
        bl5.setIngredient('G', Material.GOLD_INGOT);

        // Registrar el crafteo
        Bukkit.addRecipe(bl5);
    }
}
