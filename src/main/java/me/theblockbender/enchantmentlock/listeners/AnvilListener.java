package me.theblockbender.enchantmentlock.listeners;

import me.theblockbender.enchantmentlock.EnchantmentLock;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.util.List;

public class AnvilListener implements Listener {

    private EnchantmentLock main;

    /**
     * Constructor
     *
     * @param main Instance of the main class.
     */
    public AnvilListener(EnchantmentLock main) {
        this.main = main;
    }

    /**
     * Anvil event.
     *
     * @param event Event called.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (!(human instanceof Player)) return;

        Player player = (Player) human;
        Inventory inventory = event.getClickedInventory();
        if (!(inventory instanceof AnvilInventory)) return;

        InventoryView inventoryView = event.getView();
        int clickedSlot = event.getRawSlot();
        if (clickedSlot != inventoryView.convertSlot(clickedSlot)) return;
        if (clickedSlot != 2) return;

        ItemStack item = inventory.getItem(0);
        ItemStack mergedWith = inventory.getItem(1);
        ItemStack result = inventory.getItem(2);

        if (item == null) return;
        if (mergedWith == null) return;
        if (result == null) return;

        //check if the the anvil action is an enchantment and if this is allowed by config settings
        if (mergedWith.getType() == Material.ENCHANTED_BOOK && !enchantmentBookMergesDisabled())
            return;

        //check if one of th BOTH merged items is a locked item
        if (isLockedItem(item) || isLockedItem(mergedWith)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Message")));
            event.setCancelled(true);
        }
    }

    private boolean enchantmentBookMergesDisabled() {
        return main.getConfig().getBoolean("BlockMergeEnchants");
    }

    private boolean isLockedItem(ItemStack item) {

        if (!item.hasItemMeta())
            return false;

        ItemMeta meta = item.getItemMeta();

        if (!meta.hasLore())
            return false;

        List<String> lore = meta.getLore();

        for (String loreLine : lore) {
            String line = ChatColor.stripColor(loreLine);

            for(String id : main.identifiers) {
                if (line.contains(id))
                    return true;
            }
        }
        return true;
    }
}
