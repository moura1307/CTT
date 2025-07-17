package com.coffee.cTT;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class SpaceHelmet {

    private final NamespacedKey spaceHelmetKey;
    private final NamespacedKey uniqueIdKey;

    public SpaceHelmet(JavaPlugin plugin) {
        this.spaceHelmetKey = new NamespacedKey(plugin, "space_helmet");
        this.uniqueIdKey = new NamespacedKey(plugin, "unique_Id");
    }
    @EventHandler
    public void makeUnstackable(CraftItemEvent e) {

        ItemStack result = e.getInventory().getResult();
        if(result.getType() != Material.GLASS) return;

        ItemMeta meta = result.getItemMeta();
        if (meta == null) return;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        if (!pdc.has(uniqueIdKey, PersistentDataType.STRING)) {
            pdc.set(uniqueIdKey, PersistentDataType.STRING, UUID.randomUUID().toString());
            result.setItemMeta(meta);
        }
    }

    private boolean isDesiredItem(ItemStack item, Player player) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta())
            return false;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(spaceHelmetKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(spaceHelmetKey, PersistentDataType.BYTE);
    }

    @EventHandler
    public void preventPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isDesiredItem(item, player)) {
            e.setCancelled(true);
        }
    }
}
