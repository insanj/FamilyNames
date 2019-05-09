


package com.insanj.familynames.listener;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.insanj.familynames.FamilyNamesPlugin;
import com.insanj.familynames.util.FamilyNamesConfig;

public class FamilyNamesChatListener implements Listener {
    private final FamilyNamesPlugin plugin;

    public FamilyNamesChatListener(FamilyNamesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (plugin.config.getEnabled() == false) {
            plugin.info("Disabled in config, so not overriding or doing anything with chat message.");
            return; // disabled in config
        }

        Player player = event.getPlayer();
        FamilyNamesConfig.PlayerEntry entry = plugin.config.getPlayerEntry(player.getName());
        if (entry == null) {
            plugin.info("Nothing configured for user sending message: " + player.toString());
            return; // nothing configured
        }

        String msg = event.getMessage();
        for (Player recipient : event.getRecipients​()) {
            plugin.composer.sendFamilyNamesMessage(entry, recipient, msg);
        }

        event.setCancelled(true);
        plugin.info("Finished sending Family Names chat message with PlayerEntry: " + entry.toString());
    }

}