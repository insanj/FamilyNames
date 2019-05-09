package com.insanj.familynames;

import com.insanj.familynames.command.*;
import com.insanj.familynames.listener.*;
import com.insanj.familynames.util.*;

import java.util.Set;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

public class FamilyNamesPlugin extends JavaPlugin {
    public FamilyNamesConfig config;
    public FamilyNamesChatComposer composer;

    private FamilyNamesChatListener chatListener;
    private FamilyNamesJoinListener joinListener;
    private FamilyNamesCommandExecutor executor;

    @Override
    public void onEnable() {
        // utils
        config = new FamilyNamesConfig(this);
        config.reload();

        composer = new FamilyNamesChatComposer();

        // commands
        executor = new FamilyNamesCommandExecutor(this);
        getCommand("family").setExecutor(executor);

        // listeners (for player connections, chat messages)
        joinListener = new FamilyNamesJoinListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(joinListener, this); 

        chatListener = new FamilyNamesChatListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(chatListener, this); 
    }

    public void info(String s) {
        if (config.getDebug() == true) {
            Bukkit.getLogger().info(s);
        }
    }

    public static void warning(String s) {
        Bukkit.getLogger().warning(s);
    }
}