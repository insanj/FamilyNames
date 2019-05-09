package com.insanj.familynames.command;

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

import com.insanj.familynames.FamilyNamesPlugin;
import com.insanj.familynames.util.FamilyNamesPermissions;
import com.insanj.familynames.util.FamilyNamesConfig;

public class FamilyNamesCommandExecutor implements CommandExecutor {
    private final FamilyNamesPlugin plugin;
    private final String ERROR_NO_PERM = ChatColor.RED + "You do not have the required permission to run this FamilyNames command.";

    public FamilyNamesCommandExecutor(FamilyNamesPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (plugin.config.getEnabled() == false) {
            plugin.info("Ignoring command because we are disabled in config file!");
            return false; // disabled in config
        }

        if (args.length < 1) {
            plugin.info("Unable to process /family because no arguments given (/family does not do anything by itself).");
            return false;
        }

        String familyCommand = args[0];

        if (FamilyNamesPermissions.senderHasPermission(sender, familyCommand) == false) {
            sender.sendMessage(ERROR_NO_PERM);
            return true;
        }

        Player player = (Player)sender;
        FamilyNamesPermissions.PermissionType type = FamilyNamesPermissions.permissionTypeFromString(familyCommand);
        switch (type) {
            case SET:
                return onFamilySetCommand(player, args);
            case REMOVE:
                return onFamilyRemoveCommand(player, args);
            case ADD:
                return onFamilyAddCommand(player, args);
            case REMOVEP:
                return onFamilyRemovePCommand(player, args);
            default:
            case ALL:
            case UNKNOWN:
                return false;
        }
    }

    private boolean onFamilySetCommand(Player sender, String[] args) {
        if (args.length < 3) {
            plugin.info("Unable to run /family set because fewer than 3 arguments were given.");
            return false;
        }

        String playerName = args[1];
        String[] familyName = args[2].split("_");
        if (familyName == null || familyName.length != 2) {
            plugin.info("Unable to set Family Name of player which does not follow the format 'first_last'!");
            return false;
        }

        String firstName = familyName[0];
        String surname = familyName[1];

        FamilyNamesConfig.PlayerEntry entry = new FamilyNamesConfig.PlayerEntry(sender.getName(), "", firstName, surname, "");
        plugin.config.addPlayerEntry(entry);

        plugin.composer.sendMessage("[FamilyNames]", sender, String.format("Welcome, %s_%s!", entry.firstName, entry.surname));

        return true;
    }

    private boolean onFamilyRemovePCommand(Player sender, String[] args) {
        if (args.length < 2) {
            plugin.info("Unable to run /family removep because fewer than 2 arguments were given.");
            return false;
        }

        String playerName = args[1];
        boolean result = plugin.config.removePlayerEntry(playerName);

        if (result == true) {
            plugin.composer.sendMessage("[FamilyNames]", sender, String.format("%s removed from Family Names config!", playerName));
        } else {
            plugin.composer.sendMessage("[FamilyNames]", sender, String.format("Error: unable to remove %s from Family Names config", playerName));
        }

        return true;
    }

    private boolean onFamilyAddCommand(Player sender, String[] args) {
        if (args.length < 3) {
            plugin.info("Unable to run /family add because fewer than 3 arguments were given.");
            return false;
        }

        String typeString = args[1];
        String string = args[2];
        boolean result = plugin.config.addFamilyName(typeString, string);

        if (result == true) {
            plugin.composer.sendMessage("[FamilyNames]", sender, String.format("%s added to Family Names config for %s!", string, typeString));
        } else {
            plugin.composer.sendMessage("[FamilyNames]", sender, String.format("Error: unable to add %s to Family Names config for %s", string, typeString));
        }

        return true;
    }

    private boolean onFamilyRemoveCommand(Player sender, String[] args) {
        if (args.length < 3) {
            plugin.info("Unable to run /family remove because fewer than 3 arguments were given.");
            return false;
        }

        String typeString = args[1];
        String string = args[2];
        boolean result = plugin.config.removeFamilyName(typeString, string);

        if (result == true) {
            plugin.composer.sendMessage("[FamilyNames]", sender, String.format("%s removed from Family Names config for %s!", string, typeString));
        } else {
            plugin.composer.sendMessage("[FamilyNames]", sender, String.format("Error: unable to remove %s to Family Names config for %s", string, typeString));
        }

        return true;
    }
}
