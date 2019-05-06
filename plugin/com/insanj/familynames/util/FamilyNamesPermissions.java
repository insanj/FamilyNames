package com.insanj.familynames.util;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.bukkit.World;
import org.bukkit.Location;

public class FamilyNamesPermissions {
    public static final String FAMILY_SET_KEY = "family.set";
    public static final String FAMILY_REMOVE_KEY = "family.remove";
    public static final String FAMILY_ADD_KEY = "family.add";
    public static final String FAMILY_REMOVEP_KEY = "family.removep";
    public static final String FAMILY_ALL_KEY = "family.all";
    public static final String FAMILY_FSET_KEY = "family.fset";

    public enum PermissionType {
        SET,
        REMOVE,
        ADD,
        REMOVEP,
        ALL,
        FSET,
        UNKNOWN
    }

    public static PermissionType permissionTypeFromString(String arg) {
        if (arg.equalsIgnoreCase(FAMILY_SET_KEY)) {
            return PermissionType.SET;
        } else if (arg.equalsIgnoreCase(FAMILY_REMOVE_KEY)) {
            return PermissionType.REMOVE;
        } else if (arg.equalsIgnoreCase(FAMILY_ADD_KEY)) {
            return PermissionType.ADD;
        } else if (arg.equalsIgnoreCase(FAMILY_REMOVEP_KEY)) {
            return PermissionType.REMOVEP;
        } else if (arg.equalsIgnoreCase(FAMILY_ALL_KEY)) {
            return PermissionType.ALL;
        } else if (arg.equalsIgnoreCase(FAMILY_FSET_KEY)) {
            return PermissionType.FSET;
        } else {
            return PermissionType.UNKNOWN;
        }
    }

    public static boolean senderHasPermission(CommandSender sender, String arg) {
        if (!(sender instanceof Player)) {
            return false;
        }

        return playerHasPermission((Player)sender, arg);
    }

    public static boolean playerHasPermission(Player sender, String arg) {
        if (sender.isOp() == true) {
            return true;
        }
        
        PermissionType type = permissionTypeFromString(arg);
        switch (type) {
            case SET:
                return sender.hasPermission(FAMILY_SET_KEY) == true;
            case REMOVE:
                return sender.hasPermission(FAMILY_REMOVE_KEY) == true;
            case ADD:
                return sender.hasPermission(FAMILY_ADD_KEY) == true;
            case REMOVEP:
                return sender.hasPermission(FAMILY_REMOVEP_KEY) == true;
            case FSET:
                return sender.hasPermission(FAMILY_FSET_KEY) == true;
            case ALL:
                return true;
            default:
            case UNKNOWN:
                return false;
        }
    }