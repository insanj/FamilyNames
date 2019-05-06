package com.insanj.familynames.command;

import com.insanj.familynames.FamilyNamesPlugin;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class FamilyNamesCommandExecutor implements CommandExecutor {
    public final String HOVER_PERMISSION_ADD_CMD = "add";
    public final String HOVER_PERMISSION_START_CMD = "start";
    public final String HOVER_PERMISSION_STOP_CMD = "stop";
    public final String HOVER_PERMISSION_RELOAD_CMD = "reload";

    private final String ERROR_NO_PERM = ChatColor.RED + "You do not have the required permission to run this Hover command.";

    public final Hover plugin;

    public HoverCommandExecutor(Hover plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (plugin.config.getEnabled() == false) {
            return false; // disabled in config
        }

        if (args.length != 1) {
            return false;
        } else if (senderHasPermission(sender, args[0]) == false) {
            sender.sendMessage(ERROR_NO_PERMISSIONS);
            return true;
        }
        
        if (args[0].equalsIgnoreCase(HOVER_PERMISSION_ADD_CMD)) {
            Player player = (Player)sender;
            
            HashMap defaultContents = new HashMap();
            defaultContents.put("Name", player.getName());
            plugin.getConfig().createSection(player.getName(), defaultContents);
            plugin.saveConfig();

            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Wrote default config for " + player.getName() + "!");
            return true;
        } else if (args[0].equalsIgnoreCase(HOVER_PERMISSION_RELOAD_CMD)) {
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover reloaded!");
            return true;
        } else if (args[0].equalsIgnoreCase(HOVER_PERMISSION_START_CMD)) {
            if (plugin.listener.disabled == false) {
                sender.sendMessage(ChatColor.RED + "Hover is already enabled.");
                return true;
            }

            plugin.listener.disabled = false;
            Bukkit.getServer().getPluginManager().registerEvents(plugin.listener, plugin); 
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover re-enabled!");
            return true;
        } else if (args[0].equalsIgnoreCase(HOVER_PERMISSION_STOP_CMD)) {
            if (plugin.listener.disabled == true) {
                sender.sendMessage(ChatColor.RED + "Hover is already disabled.");
                return true;
            }

            plugin.listener.disabled = true;
            HandlerList.unregisterAll(plugin.listener);
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover disabled!");
            return true;
        }

        return false;
    }

//   family.all:

    private boolean onFamilyRemoveCommand() {
        //   family.remove:
        //             /family remove <family_name>
    }

    private boolean onFamilySetCommand() {
        //  family.set:
        //              /family set <player> <family_name>
    }


    private boolean onFamilyAddCommand() {
        //   family.add:

        //             /family add <family_name>
    }


    private boolean onFamilyRemovePCommand() {
        //  family.removep:
        //             /family removep <player>

    }


    private boolean onFamilyFSetCommand() {
        //   family.fset:
        //             /family fset <player> <first_name> <surname>
    }

}
