package me.newt.enchantmentlock;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Updated to support minecraft 1.16.1.
 * @author Newt
 */
public class ItemManager {

    private final List<String> identifiers;

    /**
     * Constructor for the ItemManager.
     * @param enchantmentLock Instance of the main class.
     */
    public ItemManager(EnchantmentLock enchantmentLock) {
        this.identifiers = enchantmentLock.getConfig().getStringList("identifiers");
    }

    /**
     * Test if the given {@link ItemStack} should be locked from enchantments.
     * @param item The ItemStack to test.
     * @return If the ItemStack is locked.
     */
    public boolean isLockedItem(ItemStack item) {
        if (item == null) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        // no Name or Lore
        if ( (!meta.hasDisplayName()) && (!meta.hasLore()) ) return false;
        
        if (meta.hasDisplayName()) { // has name
            String name = ChatColor.stripColor(meta.getDisplayName());
            for (String identifier : identifiers) {
                if (name.contains(identifier)) return true;
                }
            }
            
        // passed name check, now check lore
        List<String> lore = meta.getLore();
        if (lore == null) return false;

        for (String line : lore) {
            line = ChatColor.stripColor(line);

            for (String identifier : identifiers) {
                if (line.contains(identifier)) return true;
            }
        }
        return false;
    }

}
