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
    private final FamilyNamesPlugin plugin;
    private final String ERROR_NO_PERM = ChatColor.RED + "You do not have the required permission to run this Hover command.";

    public FamilyNamesCommandExecutor(Hover plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (plugin.config.getEnabled() == false) {
            return false; // disabled in config
        }

        if (args.length != 1) {
            return false;
        }

        String familyCommand = args[0];

        else if (FamilyNamesPermissions.senderHasPermission(sender, familyCommand) == false) {
            sender.sendMessage(ERROR_NO_PERM);
            return true;
        }

        switch (FamilyNamesPermissions.permissionTypeFromString(familyCommand)) {
            case SET:
                return onFamilySetCommand(sender, args);
            case REMOVE:
                return onFamilyRemoveCommand(sender, args);
            case ADD:
                return onFamilyAddCommand(sender, args);
            case REMOVEP:
                return onFamilyRemovePCommand(sender, args);
            case FSET:
                return onFamilyFSetCommand(sender, args);
            default:
            case ALL:
            case UNKNOWN:
                return false;
        }
    }

    private boolean onFamilyRemoveCommand(CommandSender sender, String[] args) {
        //   family.remove:
        //             /family remove <family_name>
    }

    private boolean onFamilySetCommand(CommandSender sender, String[] args) {
        if (args < 3) {
            return false;
        }

        String playerName = args[1];
        String familyName = args[2];

        

    }


    private boolean onFamilyAddCommand(CommandSender sender, String[] args) {
        //   family.add:

        //             /family add <family_name>

        Player player = (Player)sender;

        HashMap defaultContents = new HashMap();
        defaultContents.put("Name", player.getName());

        plugin.getConfig().createSection(player.getName(), defaultContents);
        plugin.saveConfig();

        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Wrote default config for " + player.getName() + "!");
        return true;
    }


    private boolean onFamilyRemovePCommand(CommandSender sender, String[] args) {
        //  family.removep:
        //             /family removep <player>

    }


    private boolean onFamilyFSetCommand(CommandSender sender, String[] args) {
        //   family.fset:
        //             /family fset <player> <first_name> <surname>
    }

    private boolean onFamilyReloadCommand(CommandSender sender, String[] args) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.LIGHT_PURPLE + "Hover reloaded!");
        return true;
    }

}
