package me.theblockbender.enchantmentlock.listeners;

import me.theblockbender.enchantmentlock.EnchantmentLock;
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
        int slot = event.getRawSlot();
        if (slot != inventoryView.convertSlot(slot)) return;
        if (slot != 2) return;

        ItemStack item = inventory.getItem(0);
        ItemStack mergedWith = inventory.getItem(1);
        ItemStack result = inventory.getItem(2);
        boolean isRepair = false;

        if (item == null) return;
        if (result == null) return;
        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return;

        if (mergedWith != null) {
            if (mergedWith.getType() != Material.ENCHANTED_BOOK) isRepair = true;
            if (main.getConfig().getBoolean("BlockMergeEnchants") && (item.getEnchantments() != result.getEnchantments()))
                isRepair = false;
        }

        if (!isRepair) {
            List<String> lore = meta.getLore();
            for (String loreline : lore) {
                String line = ChatColor.stripColor(loreline);
                for(String id : main.identifiers){
                    if(line.contains(id)){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Message")));
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }
}
