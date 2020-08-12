package me.newt.enchantmentlock.feature;

import me.newt.enchantmentlock.EnchantmentLock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Updated to support minecraft 1.16.1.
 * @author Newt
 */
public class BlockEnchantingTable implements Listener {

    private final EnchantmentLock enchantmentLock;
    private final List<UUID> cancelDoubleMessage;

    /**
     * Constructor for the BlockEnchantingTable Listener.
     * @param enchantmentLock Instance of the main class.
     */
    public BlockEnchantingTable(EnchantmentLock enchantmentLock) {
        this.enchantmentLock = enchantmentLock;
        this.cancelDoubleMessage = new ArrayList<>();
    }

    /**
     * Enchanting Table event.
     * @param event The respective event listened to.
     */
    @EventHandler
    public void onEnchantmentTable(PrepareItemEnchantEvent event) {
        ItemStack item = event.getItem();
        if (enchantmentLock.itemManager.isLockedItem(item)) {
            event.setCancelled(true);
        }

        Player player = event.getEnchanter();
        UUID uuid = player.getUniqueId();

        // For some reason, this event is called multiple times for the same enchantment.
        // This prevents the chat of players from being spammed in that case.
        if (!cancelDoubleMessage.contains(uuid)) {
            player.sendMessage(enchantmentLock.messageManager.cannot_enchant);
            cancelDoubleMessage.add(uuid);
            removeInTwoTicks(uuid);
        }
    }

    /**
     * Schedules the removal from a UUID from the cancelDoubleMessage list.
     * @param uuid The UUID to remove.
     */
    private void removeInTwoTicks(UUID uuid) {
        BukkitScheduler scheduler = enchantmentLock.getServer().getScheduler();
        scheduler.runTaskLater(enchantmentLock, () -> cancelDoubleMessage.remove(uuid), 2L);
    }
}
