package com.coffee.cTT;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CTT extends JavaPlugin implements Listener {

    private DiverArmor diverArmor;
    private OceanCompass oceanCompass;

    @Override
    public void onEnable() {
        this.diverArmor = new DiverArmor(this);
        this.oceanCompass = new OceanCompass(this);

        getServer().getPluginManager().registerEvents(diverArmor, this);
        getServer().getPluginManager().registerEvents(oceanCompass, this);

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("CTT plugin enabled!");
    }
}