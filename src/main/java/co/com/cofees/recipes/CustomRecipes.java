package co.com.cofees.recipes;

import co.com.cofees.tools.Keys;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomRecipes {

    public static void registerCustomCrafting() {
        backpacks();
    }

    public static ItemStack createCustomItem(Material material, String displayName, String lore, NamespacedKey key, String keyValue, int amount) {
        // Crear el ítem
        ItemStack item = new ItemStack(material, amount);
        ItemMeta itemMeta = item.getItemMeta();

        // Configurar el nombre visible del ítem

        Objects.requireNonNull(itemMeta).setDisplayName(ChatColor.BOLD + displayName);

        // Configurar la persistencia de datos
        if (key != null && keyValue != null) {
            itemMeta.getPersistentDataContainer().set(key, PersistentDataType.STRING, keyValue);
        }

        // Configurar el lore del ítem
        if (lore != null && !lore.isEmpty()) {
            itemMeta.setLore(List.of(lore));
        }

        // Establecer la meta del ítem
        item.setItemMeta(itemMeta);

        return item;
    }


    // NO ESTA TERMINADO
    public static void addCustomRecipe(ItemStack itemStack, NamespacedKey key, String shape1, String shape2, String shape3, HashMap<Character, Material> ingredientMap) {
        ShapedRecipe backpack = new ShapedRecipe(key, itemStack);
        backpack.shape(shape1, shape2, shape3); //Controlar casos que no se puedan
        backpack.setIngredient('H', Material.CHEST);//1
        backpack.setIngredient('C', Material.LEATHER);//2
        backpack.setIngredient('S', Material.STRING);//3  esos 3 se pueden recibir en un hashmap
        // Registrar el crafteo
        Bukkit.addRecipe(backpack);
    }


    public static void backpacks() {
        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv1 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv1Meta = backpackLv1.getItemMeta();
        backpackLv1Meta.setDisplayName(ChatColor.BOLD + "Backpack lv1");

        backpackLv1Meta.getPersistentDataContainer().set(Keys.BACKPACKLV1, PersistentDataType.STRING, "true");
        backpackLv1Meta.getPersistentDataContainer().set(Keys.BACKPACK_CODE, PersistentDataType.STRING, "true");
        backpackLv1Meta.setLore(List.of("UNA MOCHILA EN MAICRA?"));
        backpackLv1.setAmount(1);
        //setear meta
        backpackLv1Meta.setCustomModelData(1);
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
        ItemStack backpackLv2 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv2Meta = backpackLv2.getItemMeta();
        backpackLv2Meta.setDisplayName(ChatColor.DARK_GRAY + "" + ChatColor.BOLD + "Backpack lv2");

        backpackLv2Meta.getPersistentDataContainer().set(Keys.BACKPACKLV2, PersistentDataType.STRING, "true");

        backpackLv2Meta.setLore(List.of("UNA MOCHILA MAS GRANDE"));
        backpackLv2.setAmount(1);
        //setear meta
        backpackLv2Meta.setCustomModelData(2);
        backpackLv2.setItemMeta(backpackLv2Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl2 = new ShapedRecipe(Keys.BACKPACKLV2Recipe, backpackLv2);
        bl2.shape("HHH", "HMH", "HHH");
        bl2.setIngredient('H', Material.IRON_INGOT);
        bl2.setIngredient('M', new RecipeChoice.MaterialChoice(Material.CLOCK) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                return canCraftLv2(itemStack);
            }
        });


        // Registrar el crafteo
        Bukkit.addRecipe(bl2);

        //backpack lv3

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv3 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv3Meta = backpackLv3.getItemMeta();
        backpackLv3Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv3Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv3Meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Backpack lv3");

        backpackLv3Meta.getPersistentDataContainer().set(Keys.BACKPACKLV3, PersistentDataType.STRING, "true");
        backpackLv3Meta.setLore(List.of("UNA MOCHILA AUN MAS GRANDE"));
        backpackLv3.setAmount(1);
        //setear meta
        backpackLv3Meta.setCustomModelData(3);
        backpackLv3.setItemMeta(backpackLv3Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl3 = new ShapedRecipe(Keys.BACKPACKLV3Recipe, backpackLv3);
        bl3.shape("OOO", "OMO", "OOO");
        bl3.setIngredient('O', Material.GOLD_INGOT);
        bl3.setIngredient('M', new RecipeChoice.MaterialChoice(backpackLv2.getType()) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                if (itemStack == null) {
                    return false;
                }
                PersistentDataContainer container = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
                return container.has(Keys.BACKPACKLV2, PersistentDataType.STRING);
            }
        });
        // Registrar el crafteo
        Bukkit.addRecipe(bl3);

        //backpack lv4

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv4 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv4Meta = backpackLv4.getItemMeta();
        backpackLv4Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv4Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv4Meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Backpack lv4");

        backpackLv4Meta.getPersistentDataContainer().set(Keys.BACKPACKLV4, PersistentDataType.STRING, "true");
        backpackLv4Meta.setLore(List.of("UNA MOCHILA INCLUSO MAS GRANDE? CUANDO TERMINA?"));
        backpackLv4.setAmount(1);
        //setear meta
        backpackLv4Meta.setCustomModelData(4);
        backpackLv4.setItemMeta(backpackLv4Meta);


        // Crear el crafteo personalizado
        ShapedRecipe bl4 = new ShapedRecipe(Keys.BACKPACKLV4Recipe, backpackLv4);
        bl4.shape("DDD", "DMD", "DDD");
        bl4.setIngredient('D', Material.DIAMOND);
        bl4.setIngredient('M', new RecipeChoice.MaterialChoice(Material.CLOCK) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                if (itemStack == null) {
                    return false;
                }
                PersistentDataContainer container = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
                return container.has(Keys.BACKPACKLV3, PersistentDataType.STRING);
            }
        });
        // Registrar el crafteo
        Bukkit.addRecipe(bl4);

        //backpack lv4

        // Crear el ítem que se obtendrá al realizar el crafteo
        ItemStack backpackLv5 = new ItemStack(Material.CLOCK);
        ItemMeta backpackLv5Meta = backpackLv5.getItemMeta();
        backpackLv5Meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        backpackLv5Meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
        backpackLv5Meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.MAGIC + "Backpack lv5");

        backpackLv5Meta.getPersistentDataContainer().set(Keys.BACKPACKLV5, PersistentDataType.STRING, "true");
        backpackLv5Meta.setLore(List.of("LA MOCHILA DEFINITIVA"));
        backpackLv5.setAmount(1);
        //setear meta
        backpackLv5Meta.setCustomModelData(5);
        backpackLv5.setItemMeta(backpackLv5Meta);

        // Crear el crafteo personalizado
        ShapedRecipe bl5 = new ShapedRecipe(Keys.BACKPACKLV5Recipe, backpackLv5);
        bl5.shape("NM ");
        bl5.setIngredient('N', Material.NETHERITE_INGOT);
        bl5.setIngredient('M', new RecipeChoice.MaterialChoice(Material.CLOCK) {
            @Override
            public boolean test(@Nullable ItemStack itemStack) {
                if (itemStack == null) {
                    return false;
                }
                PersistentDataContainer container = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
                return container.has(Keys.BACKPACKLV4, PersistentDataType.STRING);
            }
        });

        // Registrar el crafteo
        Bukkit.addRecipe(bl5);
    }

    // Métodos para verificar las condiciones de crafteo de cada mochila
    private static boolean canCraftLv2(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(Keys.BACKPACKLV1, PersistentDataType.STRING);
    }

    private static boolean canCraftLv3(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(Keys.BACKPACKLV2, PersistentDataType.STRING);
    }

    private static boolean canCraftLv4(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(Keys.BACKPACKLV3, PersistentDataType.STRING);
    }

    private static boolean canCraftLv5(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        return container.has(Keys.BACKPACKLV4, PersistentDataType.STRING);
    }




}