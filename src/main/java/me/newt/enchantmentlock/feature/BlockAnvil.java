package me.newt.enchantmentlock.feature;

import me.newt.enchantmentlock.EnchantmentLock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

import java.util.logging.Logger;

/**
 * Updated to support minecraft 1.16.1.
 *
 * @author Newt
 */
public class BlockAnvil implements Listener {

    private final EnchantmentLock enchantmentLock;
    private Logger logger;

    /**
     * Constructor for the BlockEnchantingTable Listener.
     *
     * @param enchantmentLock Instance of the main class.
     */
    public BlockAnvil(EnchantmentLock enchantmentLock) {
        logger = Bukkit.getLogger();
        this.enchantmentLock = enchantmentLock;
    }

    /**
     * Returns whether or not the item has been renamed
     * @param item ItemStack original item put in anvil
     * @param result ItemStack result from using the anvil
     * @return Whether or not the item has been renamed
     */
    private boolean isChangeInName(ItemStack item, ItemStack result) {
        String itemName;
        String resultName;

        if (item.hasItemMeta()) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta.hasDisplayName()) {
                itemName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            } else {
                itemName = ChatColor.stripColor(item.getType().name());
            }
        } else {
            itemName = ChatColor.stripColor(item.getType().name());
        }

        if (result.hasItemMeta()) {
            ItemMeta resultMeta = result.getItemMeta();
            if (resultMeta.hasDisplayName()) {
                resultName = ChatColor.stripColor(result.getItemMeta().getDisplayName());
            } else {
                resultName = ChatColor.stripColor(result.getType().name());
            }
        } else {
            resultName = ChatColor.stripColor(result.getType().name());
        }

//        logger.info("itemName   = " + itemName);
//        logger.info("resultName = " + resultName);
        
        if (!itemName.equals(resultName)) {
            return true;
        }
        return false;
    }

    /**
     * Anvil event.
     *
     * @param event The respective event listened to.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) return;

        Player player = (Player) human;
        Inventory inventory = event.getClickedInventory();
        if (!(inventory instanceof AnvilInventory)) return;
        
        InventoryView inventoryView = event.getView();
        int slot = event.getRawSlot();
        if (slot != inventoryView.convertSlot(slot)) return;
        if (slot != 2) return;

        ItemStack item = inventory.getItem(0);
        ItemStack mergedWith = inventory.getItem(1);
        ItemStack result = inventory.getItem(2);

        boolean involvesLockedItem = false;
        boolean isRepair = false;
        boolean isChangeInEnchantments = false;
        boolean isChangeInName = false;

        if (item == null) return;
        if (result == null) return;
        if (enchantmentLock.itemManager.isLockedItem(item)) involvesLockedItem = true;
        if (enchantmentLock.itemManager.isLockedItem(result)) involvesLockedItem = true;

        isChangeInName = isChangeInName(item, result);

        if (mergedWith != null) {
            if (enchantmentLock.itemManager.isLockedItem(mergedWith)) involvesLockedItem = true;
            if (mergedWith.getType() != Material.ENCHANTED_BOOK) {
                isRepair = true;
            }
            if (item.getEnchantments() != result.getEnchantments()) {
                isRepair = false;
                isChangeInEnchantments = true;
            }
        }
        
        if (!involvesLockedItem) return;

        if (enchantmentLock.block_anvil_enchanting && isChangeInEnchantments) {
            event.setCancelled(true);
            player.sendMessage(enchantmentLock.messageManager.cannot_enchant);
            return;
        }

        if (enchantmentLock.block_name_change && isChangeInName) {
            event.setCancelled(true);
            player.sendMessage(enchantmentLock.messageManager.cannot_rename);
            return;
        }

        if (enchantmentLock.block_anvil_repair && isRepair) {
            event.setCancelled(true);
            player.sendMessage(enchantmentLock.messageManager.cannot_repair);
        }


    }
}
