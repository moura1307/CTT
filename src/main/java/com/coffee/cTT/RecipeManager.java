package com.coffee.cTT;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class RecipeManager {
    public static void registerRecipes(JavaPlugin plugin) {
        oceanCompassRecipe(plugin);
    }

    public static void oceanCompassRecipe(JavaPlugin plugin) {

        ItemStack placeholder = new ItemStack(Material.COMPASS);
        placeholder.addUnsafeEnchantment(Enchantment.AQUA_AFFINITY, 1);
        ItemMeta placeHolderMeta = placeholder.getItemMeta();

        placeHolderMeta.setDisplayName(ChatColor.AQUA + "Sea Compass");
        List<String> lore = new ArrayList<>();
        lore.add("Press right-click to locate a Ocean Monument");
        placeHolderMeta.setLore(lore);

        NamespacedKey itemKey = new NamespacedKey(plugin, "sea_compass");
        placeHolderMeta.getPersistentDataContainer().set(
                itemKey,
                PersistentDataType.STRING,
                "custom_compass"
        );

        placeholder.setItemMeta(placeHolderMeta);

        ShapedRecipe oceanCompass = new ShapedRecipe(
                new NamespacedKey(plugin, "ocean_compass"),
                placeholder
        );

        oceanCompass.shape(
                "LSL",
                "SCS",
                "LSL"
        );

        oceanCompass.setIngredient('C', Material.COMPASS);
        oceanCompass.setIngredient('L', Material.LILY_PAD);
        oceanCompass.setIngredient('S', Material.SEA_PICKLE);

        plugin.getServer().addRecipe(oceanCompass);
    }
}
