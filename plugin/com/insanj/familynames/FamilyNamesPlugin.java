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
    private FamilyNamesLoginListener loginListener;
    private FamilyNamesCommandExecutor executor;

    @Override
    public void onEnable() {
        // utils
        config = new FamilyNamesConfig(this);
        config.reload();

        composer = new FamilyNamesChatComposer(this);

        // commands
        executor = new FamilyNamesCommandExecutor(this);
        getCommand("family").setExecutor(executor);

        // listeners (for player connections, chat messages)
        chatListener = new FamilyNamesChatListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(chatListener, this); 

        loginListener = new FamilyNamesLoginListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(loginListener, this); 
    }
}