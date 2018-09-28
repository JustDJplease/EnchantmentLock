package me.theblockbender.enchantmentlock.listeners;

import me.theblockbender.enchantmentlock.EnchantmentLock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class EnchantmentTableListener implements Listener {

    private EnchantmentLock main;
//    private List<UUID> cancelDoubleSending = new ArrayList<>();

    /**
     * Constructor
     *
     * @param main Instance of the main class.
     */
    public EnchantmentTableListener(EnchantmentLock main) {
        this.main = main;
    }

    /**
     * Enchanting Table event.
     *
     * @param event Event called.
     */
    @EventHandler
    public void onEnchant(PrepareItemEnchantEvent event) {
        ItemStack item = event.getItem();
        Player player = event.getEnchanter();
        final UUID uuid = player.getUniqueId();

        if (!item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return;
        List<String> lore = meta.getLore();

        for (String loreline : lore) {
            if (!main.identifiers.contains(loreline)) continue;
//            if (!cancelDoubleSending.contains(uuid)) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Message")));
//                cancelDoubleSending.add(uuid);
//                main.getServer().getScheduler().runTaskLater(main, (Runnable) () -> cancelDoubleSending.remove(uuid), 2L);
//            }
            event.setCancelled(true);
        }
    }
}
