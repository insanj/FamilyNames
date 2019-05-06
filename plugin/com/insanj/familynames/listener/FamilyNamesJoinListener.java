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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class FamilyNamesJoinListener implements Listener {
    private final FamilyNamesPlugin plugin;

    public FamilyNamesLoginListener(FamilyNamesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (plugin.config.getEnabled() == false) {
            return; // disabled in config
        }

        Player player = event.getPlayer();
        Map entry = plugin.config.getPlayerEntry(player.getName());
        if (entry != null) {
            return; // player already setup
        }

        plugin.composer.sendMessage("[FamilyNames]", player, "Welcome! Set up your name by clicking: [Male] or [Female]", "TODO");

        //String msg = event.getMessage();
      //  for (Player recipient : event.getRecipients​()) {
      //      plugin.composer.sendMessage(player, recipient, msg);
        //}

        // ask for male / female
    }
}