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

import com.insanj.familynames.FamilyNamesPlugin;
import com.insanj.familynames.util.FamilyNamesConfig;

public class FamilyNamesJoinListener implements Listener {
    private final FamilyNamesPlugin plugin;

    public FamilyNamesJoinListener(FamilyNamesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (plugin.config.getEnabled() == false) {
            plugin.info("Detected player join, but we are disabled so suppressing any action.");
            return; // disabled in config
        }

        Player player = event.getPlayer();
        FamilyNamesConfig.PlayerEntry entry = plugin.config.getPlayerEntry(player.getName());
        if (entry != null) {
            plugin.info("Player with existing Family Names entry detected: " + player.toString());
            return; // player already setup
        }

        plugin.composer.sendMessage("[FamilyNames]", player, "Welcome! Please click a gender:");

        String randomMaleFirstName = plugin.config.getRandomMaleFirstName();
        String randomFemaleFirstName = plugin.config.getRandomFemaleFirstName();
        String randomSurname = plugin.config.getRandomSurname();

        String maleClickResultCmd = String.format("family set %s %s_%s", player.getName(), randomMaleFirstName, randomSurname);
        plugin.composer.sendMessage("[FamilyNames]", player, "[MALE]", null, maleClickResultCmd, "aqua");

        String femaleClickResultCmd = String.format("family set %s %s_%s", player.getName(), randomFemaleFirstName, randomSurname);
        plugin.composer.sendMessage("[FamilyNames]", player, "[FEMALE]", null, femaleClickResultCmd, "light_purple");

        plugin.info("Finished querying new player for Family Names gender, will have to wait for click...");
    }
}