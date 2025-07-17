package com.coffee.cTT;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;
import java.util.logging.Logger;

public class DiverArmor implements Listener  {
    private final NamespacedKey diverArmorKey;
    private final NamespacedKey uniqueIdKey;
    private final Logger logger;
    public DiverArmor(JavaPlugin plugin) {
        this.diverArmorKey = new NamespacedKey(plugin, "diver_armor");
        this.uniqueIdKey = new NamespacedKey(plugin, "unique_Id");
        this.logger = plugin.getLogger();
    }

    public ItemStack createDiverHelmet() {
        return createArmorPiece(Material.TINTED_GLASS);
    }

    public ItemStack createDiverChestplate() {
        return createArmorPiece(Material.LEATHER_CHESTPLATE);
    }

    public ItemStack createDiverLeggings() {
        return createArmorPiece(Material.LEATHER_LEGGINGS);
    }

    public ItemStack createDiverBoots() {
        return createArmorPiece(Material.LEATHER_BOOTS);
    }

    private ItemStack createArmorPiece(Material material) {
        ItemStack piece = new ItemStack(material);
        ItemMeta meta = piece.getItemMeta();
        if (meta == null) return piece;

        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.getPersistentDataContainer().set(diverArmorKey, PersistentDataType.BYTE, (byte) 1);

        piece.setItemMeta(meta);
        return piece;
    }

    private void logItemFlags(String context, ItemStack item) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            logger.info(context + " " + item.getType() + " | Flags: " + meta.getItemFlags());
            logger.info("PDC contains key: " +
                    meta.getPersistentDataContainer().has(diverArmorKey, PersistentDataType.BYTE));
        }
    }

    private boolean isDesiredItem(ItemStack item, Player player) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta())
            return false;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(diverArmorKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(diverArmorKey, PersistentDataType.BYTE);
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

    @EventHandler
    public void preventPlacement(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isDesiredItem(item, player)) {
            e.setCancelled(true);
        }
    }

}