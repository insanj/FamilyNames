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
    private final String ERROR_NO_PERM = ChatColor.RED + "You do not have the required permission to run this FamilyNames command.";

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
            default:
            case ALL:
            case UNKNOWN:
                return false;
        }
    }

    private boolean onFamilyRemoveCommand(CommandSender sender, String[] args) {
        return true;
    }

    private boolean onFamilySetCommand(CommandSender sender, String[] args) {
        if (args < 3) {
            return false;
        }

        String playerName = args[1];
        String[] familyName = args[2].split("_");
        if (familyName == null || familyName.length != 2) {
            return false;
        }

        String firstName = familyName[0];
        String surname = familyName[1];

        FamilyNamesConfig.PlayerEntry entry = new PlayerEntry(sender.getName(), "", firstName, surname, "");
        plugin.config.addPlayerEntry(entry);
        return true;
    }

    private boolean onFamilyAddCommand(CommandSender sender, String[] args) {
        return true;
    }

    private boolean onFamilyRemovePCommand(CommandSender sender, String[] args) {
        return true;
    }
}
