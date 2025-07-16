package com.coffee.cTT;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
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

public class DiverArmor implements Listener  {
    private final NamespacedKey diverArmorKey;
    private final NamespacedKey uniqueIdKey;
    public DiverArmor(JavaPlugin plugin) {
        this.diverArmorKey = new NamespacedKey(plugin, "diver_armor");
        this.uniqueIdKey = new NamespacedKey(plugin, "unique_Id");
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

        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DYE);
        meta.setUnbreakable(true);
        meta.getPersistentDataContainer().set(diverArmorKey, PersistentDataType.BYTE, (byte) 1);

        piece.setItemMeta(meta);
        return piece;
    }

    private boolean isDiverArmor(ItemStack item, Player player) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta())
            return false;

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(diverArmorKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);

        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(diverArmorKey, PersistentDataType.BYTE);
    }

    @EventHandler
    public void onCraft(CraftItemEvent e) {

        ItemStack result = e.getInventory().getResult();
        if(result.getType() != Material.TINTED_GLASS) return;

        ItemMeta meta = result.getItemMeta();
        if (meta == null) return; // Skip if no meta (unlikely for craftable items)

        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        // Only assign UUID if it doesn't have one yet
        if (!pdc.has(uniqueIdKey, PersistentDataType.STRING)) {
            pdc.set(uniqueIdKey, PersistentDataType.STRING, UUID.randomUUID().toString());
            result.setItemMeta(meta); // Apply the updated meta
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isDiverArmor(item, player)) {
            e.setCancelled(true);
        }
    }

}