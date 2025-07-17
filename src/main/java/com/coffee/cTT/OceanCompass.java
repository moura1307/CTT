package com.coffee.cTT;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
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
        e.getPlayer().sendMessage(plugin.toString());
        if (!e.getAction().toString().contains("RIGHT_CLICK")) return;
        ItemStack compass = e.getPlayer().getInventory().getItemInHand();

        if (compass.getItemMeta() == null || compass.getType() != Material.COMPASS) return;
        if (compass.getItemMeta().getPersistentDataContainer().has(compassKey, PersistentDataType.BYTE)) {
            NamespacedKey activationKey = new NamespacedKey(plugin, "ocean_compass_activated");
            if (compass.getItemMeta().getPersistentDataContainer().has(activationKey, PersistentDataType.BYTE)) {
                e.getPlayer().sendMessage("compass.getItemMeta().getPersistentDataContainer().has(activationKey, PersistentDataType.BYTE = false");
                e.setCancelled(true);
            } else {
                ItemStack activatedCompass = locateStructure(e.getPlayer(), StructureType.OCEAN_MONUMENT, 10000);
                if (activatedCompass == null) {
                    e.getPlayer().sendMessage(ChatColor.RED + "No Ocean Monument found nearby!");
                    return;
                }
                CompassMeta activatedMeta = (CompassMeta) activatedCompass.getItemMeta();
                compass.getItemMeta().getPersistentDataContainer().set(activationKey, PersistentDataType.BYTE, (byte) 1);
                activatedCompass.setItemMeta(activatedMeta);

                EquipmentSlot hand = e.getHand();
                if (hand == EquipmentSlot.HAND) {
                    e.getPlayer().getInventory().setItemInMainHand(activatedCompass);
                } else if (hand == EquipmentSlot.OFF_HAND) {
                    e.getPlayer().getInventory().setItemInOffHand(activatedCompass);
                }

                e.getPlayer().sendMessage(ChatColor.GREEN + "Compass activated! Now tracking the nearest Ocean Monument.");
            }
        } else {
            e.getPlayer().sendMessage(compass.getPersistentDataContainer().toString());
            e.setCancelled(true);
        }
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
            lore.add("This compass shall bring you to the");
            lore.add("proximity of the temple of a deadly foe");
            oceanCompassMeta.setLore(lore);

            compass.setItemMeta(oceanCompassMeta);
        }
        return compass;
    }
}
