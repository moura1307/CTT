package com.coffee.cTT;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StructureSearchResult;

import java.util.ArrayList;
import java.util.List;

public class OceanCompass implements Listener {

    private final JavaPlugin plugin;
    private final NamespacedKey compassKey;

    public OceanCompass(JavaPlugin plugin) {
        this.plugin = plugin;
        this.compassKey = new NamespacedKey(plugin, "ocean_compass");
    }

    @EventHandler
    public void onCompassActive(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;

        Player player = e.getPlayer();
        ItemStack item = e.getItem();

        // Validate item
        if (item == null || item.getType() != Material.COMPASS) return;

        ItemMeta oldOceanCompassMeta = item.getItemMeta();
        if (oldOceanCompassMeta == null) return;

        NamespacedKey itemKey = new NamespacedKey(plugin, "ocean_compass");
        oldOceanCompassMeta.getPersistentDataContainer().set(
                itemKey,
                PersistentDataType.STRING,
                "active"
        );
        item.setItemMeta(oldOceanCompassMeta);

        e.setCancelled(true);

        ItemStack activatedCompass = locateStructure(player, StructureType.OCEAN_MONUMENT, 10000);

        if (activatedCompass == null) {
            player.sendMessage(ChatColor.RED + "No Ocean Monument found nearby!");
            return;
        }

        EquipmentSlot hand = e.getHand();
        if (hand == EquipmentSlot.HAND) {
            player.getInventory().setItemInMainHand(activatedCompass);
        } else if (hand == EquipmentSlot.OFF_HAND) {
            player.getInventory().setItemInOffHand(activatedCompass);
        }

        player.sendMessage(ChatColor.GREEN + "Compass activated! Now tracking the nearest Ocean Monument.");
    }

    public static ItemStack locateStructure(Player player, StructureType structureType, int searchRadius) {
        World world = player.getWorld();
        StructureSearchResult result = world.locateNearestStructure(player.getLocation(), structureType, searchRadius, false);

        if (result == null) return null;

        Location location = result.getLocation();
        return createCompass(location);
    }

    public static ItemStack createCompass(Location location) {
        ItemStack compass = new ItemStack(Material.COMPASS);
        CompassMeta oceanCompassMeta = (CompassMeta) compass.getItemMeta();

        if (oceanCompassMeta != null) {
            oceanCompassMeta.setLodestone(location);
            oceanCompassMeta.setLodestoneTracked(false);

            oceanCompassMeta.setDisplayName(ChatColor.AQUA + "Ocean Monument Tracker");
            List<String> lore = new ArrayList<>();
            lore.add("This compass shall bring you to the proximity of the temple of a deadly foe");
            lore.add("");
            lore.add(ChatColor.DARK_GRAY + "ctt.oceancompass");
            oceanCompassMeta.setLore(lore);

            compass.setItemMeta(oceanCompassMeta);
        }
        return compass;
    }
}
