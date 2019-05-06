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

// tentative imports...
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PlayerConnection;

public class FamilyNamesChatListener implements Listener {
    private final FamilyNamesPlugin plugin;

    public FamilyNamesChatListener(FamilyNamesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (plugin.config.getEnabled() == false) {
            return; // disabled in config
        }

        Player player = event.getPlayer();
        Map entry = plugin.config.getPlayerEntry(player);
        if (entry == null) {
            return; // nothing configured
        }
        
        String msg = event.getMessage();
        for (Player recipient : event.getRecipients​()) {
            plugin.composer.sendMessage(String.format("%s_%s", entry.get("first_name"), entry.get("surname")), recipient, msg, entry.get("tooltip"));
        }

        event.setCancelled(true);
    }

}