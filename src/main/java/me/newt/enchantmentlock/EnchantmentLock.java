package me.newt.enchantmentlock;

import me.newt.enchantmentlock.feature.BlockAnvil;
import me.newt.enchantmentlock.feature.BlockEnchantingTable;
import me.newt.enchantmentlock.feature.BlockGrindstone;
import me.newt.enchantmentlock.feature.BlockSmithing;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Updated to support minecraft 1.16.1.
 * @author Newt
 */
public class EnchantmentLock extends JavaPlugin {

    public boolean block_enchanting_table;
    public boolean block_anvil_enchanting;
    public boolean block_anvil_repair;
    public boolean block_grindstone;
    public boolean block_smithing;
    public boolean block_name_change;
    public ItemManager itemManager;
    public MessageManager messageManager;

    /**
     * Enabling the plugin.
     */
    public void onEnable() {
        saveDefaultConfig();

        FileConfiguration configuration = getConfig();
        if (configuration.getInt("config_version") != 1) {
            Logger logger = getLogger();
            logger.severe("Invalid config.yml detected! (Is it outdated?)");
            logger.severe("Please delete the current file and restart the server.");
        }

        block_enchanting_table = configuration.getBoolean("block_enchanting_table");
        block_anvil_enchanting = configuration.getBoolean("block_anvil_enchanting");
        block_name_change = configuration.getBoolean("block_name_change");
        block_anvil_repair = configuration.getBoolean("block_anvil_repair");
        block_grindstone = configuration.getBoolean("block_grindstone");
        block_smithing = configuration.getBoolean("block_smithing");

        itemManager = new ItemManager(this);
        messageManager = new MessageManager(this);
        registerEvents();
    }

    /**
     * Registers all (enabled) events.
     */
    private void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        // Prevent players from enchanting locked items with an Enchanting Table.
        if (block_enchanting_table) pluginManager.registerEvents(new BlockEnchantingTable(this), this);

        // Prevent players from enchanting and/or repairing locked items with an Anvil.
        if (block_anvil_enchanting || block_anvil_repair) pluginManager.registerEvents(new BlockAnvil(this), this);

        // Prevent players from disenchanting locked items with a Grindstone.
        if (block_grindstone) pluginManager.registerEvents(new BlockGrindstone(this), this);

        // Prevent players from upgrading locked items to netherite with a smithing table.
        if (block_smithing) pluginManager.registerEvents(new BlockSmithing(this), this);
    }
}
