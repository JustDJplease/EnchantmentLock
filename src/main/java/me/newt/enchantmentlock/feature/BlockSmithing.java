package me.newt.enchantmentlock.feature;

import me.newt.enchantmentlock.EnchantmentLock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.SmithingInventory;

/**
 * Updated to support minecraft 1.16.1.
 * @author Newt
 */
public class BlockSmithing implements Listener {

    private final EnchantmentLock enchantmentLock;

    /**
     * Constructor for the BlockSmithing Listener.
     * @param enchantmentLock Instance of the main class.
     */
    public BlockSmithing(EnchantmentLock enchantmentLock) {
        this.enchantmentLock = enchantmentLock;
    }

    /**
     * Smithing event.
     * @param event The respective event listened to.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) return;

        Player player = (Player) human;
        Inventory inventory = event.getClickedInventory();
        if (!(inventory instanceof SmithingInventory)) return;

        InventoryView inventoryView = event.getView();
        int slot = event.getRawSlot();
        if (slot != inventoryView.convertSlot(slot)) return;
        if (slot != 2) return;

        ItemStack item = inventory.getItem(0);
        ItemStack mergedWith = inventory.getItem(1);
        ItemStack result = inventory.getItem(2);

        boolean involvesLockedItem = false;

        if (item == null) return;
        if (result == null) return;
        if (mergedWith == null) return;

        if (enchantmentLock.itemManager.isLockedItem(item)) involvesLockedItem = true;
        if (enchantmentLock.itemManager.isLockedItem(result)) involvesLockedItem = true;
        if (enchantmentLock.itemManager.isLockedItem(mergedWith)) involvesLockedItem = true;

        if (!involvesLockedItem) return;

        event.setCancelled(true);
        player.sendMessage(enchantmentLock.messageManager.cannot_smith);
    }
}
