package com.coffee.cTT;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.generator.structure.StructureType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class CTT extends JavaPlugin implements Listener { // Implement Listener

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(
                new OceanCompass(this),
                this
        );
    }
}