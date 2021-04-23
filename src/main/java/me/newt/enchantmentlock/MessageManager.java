package me.newt.enchantmentlock;

import org.bukkit.ChatColor;

/**
 * Updated to support minecraft 1.16.1.
 * @author Newt
 */
public class MessageManager {

    public String cannot_enchant;
    public String cannot_disenchant;
    public String cannot_repair;
    public String cannot_smith;
    public String cannot_rename;

    /**
     * Constructor for the MessageManager.
     * @param enchantmentLock Instance of the main class.
     */
    public MessageManager(EnchantmentLock enchantmentLock) {
        this.cannot_enchant = enchantmentLock.getConfig().getString("cannot_enchant");
        this.cannot_disenchant = enchantmentLock.getConfig().getString("cannot_disenchant");
        this.cannot_repair = enchantmentLock.getConfig().getString("cannot_repair");
        this.cannot_smith = enchantmentLock.getConfig().getString("cannot_smith");
        this.cannot_rename = enchantmentLock.getConfig().getString("cannot_rename");

        assert cannot_enchant != null : "Invalid configuration. Could not load message.";
        assert cannot_disenchant != null : "Invalid configuration. Could not load message.";
        assert cannot_repair != null : "Invalid configuration. Could not load message.";
        assert cannot_smith != null : "Invalid configuration. Could not load message.";
        assert cannot_rename != null : "Invalid configuration. Could not load message.";

        cannot_enchant = ChatColor.translateAlternateColorCodes('&', cannot_enchant);
        cannot_disenchant = ChatColor.translateAlternateColorCodes('&', cannot_disenchant);
        cannot_repair = ChatColor.translateAlternateColorCodes('&', cannot_repair);
        cannot_smith = ChatColor.translateAlternateColorCodes('&', cannot_smith);
        cannot_rename = ChatColor.translateAlternateColorCodes('&', cannot_rename);
    }
}
