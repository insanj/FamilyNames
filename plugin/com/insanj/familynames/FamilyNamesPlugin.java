package me.insanj.familynames;

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
    private FamilyNamesChatListener listener;
    private FamilyNamesCommandExecutor executor;
    private FamilyNamesMessageComposer composer;

    @Override
    public void onEnable() {
        config = new FamilyNamesConfig(this);

        executor = new FamilyNamesCommandExecutor(this);
        getCommand("family").setExecutor(executor);

        composer = new FamilyNamesMessageComposer(this);

        listener = new FamilyNamesChatListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(listener, this); 
    }
}