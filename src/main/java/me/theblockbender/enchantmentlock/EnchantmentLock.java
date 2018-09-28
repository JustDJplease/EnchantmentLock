package me.theblockbender.enchantmentlock;

import me.theblockbender.enchantmentlock.listeners.AnvilListener;
import me.theblockbender.enchantmentlock.listeners.EnchantmentTableListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentLock extends JavaPlugin {

    public List<String> identifiers = new ArrayList<>();

    /**
     * Called upon loading this plugin.
     */
    public void onEnable() {
        saveDefaultConfig();
        registerEvents();
        loadIdentifiers();
    }

    /**
     * Registers all event-classes as such.
     */
    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AnvilListener(this), this);
        pluginManager.registerEvents(new EnchantmentTableListener(this), this);
    }

    /**
     * Loads all identifiers from the config into this {@link List}. And translates '&' for colour coding.
     */
    private void loadIdentifiers() {
        for (String id : getConfig().getStringList("Identifiers")) {
            identifiers.add(ChatColor.translateAlternateColorCodes('&', id));
        }
    }
}
