package me.newt.enchantmentlock.feature;

import me.newt.enchantmentlock.EnchantmentLock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * Updated to support minecraft 1.16.1.
 * @author Newt
 */
public class BlockGrindstone implements Listener {

	private final EnchantmentLock enchantmentLock;

	/**
	 * Constructor for the BlockGrindstone Listener.
	 * @param enchantmentLock Instance of the main class.
	 */
	public BlockGrindstone(EnchantmentLock enchantmentLock) {
		this.enchantmentLock = enchantmentLock;
	}

	/**
	 * Grindstone event.
	 * @param event The respective event listened to.
	 */
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity human = event.getWhoClicked();
		if (!(human instanceof Player))	return;

		Player player = (Player) human;
		Inventory inventory = event.getClickedInventory();
		if (!(inventory instanceof GrindstoneInventory)) return;

		InventoryView inventoryView = event.getView();
		int slot = event.getRawSlot();
		if (slot != inventoryView.convertSlot(slot)) return;
		if (slot != 2) return;

		ItemStack upperItem = inventory.getItem(0);
		ItemStack lowerItem = inventory.getItem(1);
		ItemStack resultItem = inventory.getItem(2);

		boolean involvesLockedItem = false;

		if (upperItem != null) {
			if (enchantmentLock.itemManager.isLockedItem(upperItem)) involvesLockedItem = true;
		}

		if (lowerItem != null) {
			if (enchantmentLock.itemManager.isLockedItem(lowerItem)) involvesLockedItem = true;
		}

		if (resultItem != null) {
			if (enchantmentLock.itemManager.isLockedItem(lowerItem)) involvesLockedItem = true;
		}

		if (!involvesLockedItem) return;

		event.setCancelled(true);
		player.sendMessage(enchantmentLock.messageManager.cannot_disenchant);
	}
}
