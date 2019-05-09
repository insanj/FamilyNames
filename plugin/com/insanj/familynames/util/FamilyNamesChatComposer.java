package com.insanj.familynames.util;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

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

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.PlayerConnection;

import com.google.gson.Gson;

import com.insanj.familynames.util.FamilyNamesConfig;
import com.insanj.familynames.util.FamilyNamesPermissions;

public class FamilyNamesChatComposer {
    public void sendFamilyNamesMessage(FamilyNamesConfig.PlayerEntry sender, Player recipient, String message) {
        ArrayList messageJSON = new ArrayList();
        messageJSON.add("");

        // 0
        HashMap fullnameJSON = new HashMap();
        String fullnameString = String.format("<%s_%s>", sender.firstName, sender.surname);
        fullnameJSON.put("text", fullnameString);
        fullnameJSON.put("color", "white");
        messageJSON.add(fullnameJSON);

        // 1
        HashMap startBracketJSON = new HashMap();
        String startBracket = "[";
        startBracketJSON.put("text", startBracket);
        startBracketJSON.put("color", "gray");
        messageJSON.add(startBracketJSON);

        // 2
        HashMap playerNameJSON = new HashMap();
        String composedPlayerNameText = String.format("%s", sender.surname);
        playerNameJSON.put("text", composedPlayerNameText);
        playerNameJSON.put("color", "dark_gray");
        
        // hover over 2
        if (sender.tooltip != null) {
            HashMap hoverJSON = new HashMap();
            hoverJSON.put("action", "show_text");
            hoverJSON.put("value", sender.tooltip);
            playerNameJSON.put("hoverEvent", hoverJSON);
        }

        messageJSON.add(playerNameJSON);

        // 3
        String endBracket = "]";
        playerNameJSON.put("text", composedPlayerNameText);
        playerNameJSON.put("color", "gray");

        // 4
        HashMap messageBodyJSON = new HashMap();
        messageBodyJSON.put("text", message);

        HashMap clickJSON = new HashMap();
        clickJSON.put("action", "run_command");
        clickJSON.put("value", String.format("/tell %s", sender.name));
        messageBodyJSON.put("clickEvent", clickJSON);

        messageBodyJSON.put("color", "white");
        messageJSON.add(messageBodyJSON);

        // send!
        Gson gson = new Gson();
        String jsonString = gson.toJson(messageJSON);
        sendJsonMessage(recipient, jsonString);
    }

    public void sendMessage(String senderName, Player recipient, String message) {
        String jsonMessage = composeMessage(senderName, message);
        sendJsonMessage(recipient, jsonMessage);
    }

    public void sendMessage(String senderName, Player recipient, String message, String playerHoverText) {
        String jsonMessage = composeMessage(senderName, message, playerHoverText);
        sendJsonMessage(recipient, jsonMessage);
    }

    public void sendMessage(String senderName, Player recipient, String message, String playerHoverText, String clickEventCommandString) {
        String jsonMessage = composeMessage(senderName, message, playerHoverText, clickEventCommandString);
        sendJsonMessage(recipient, jsonMessage);
    }

    public void sendMessage(String senderName, Player recipient, String message, String playerHoverText, String clickEventCommandString, String color) {
        String jsonMessage = composeMessage(senderName, message, playerHoverText, clickEventCommandString, color);
        sendJsonMessage(recipient, jsonMessage);
    }

    private String composeMessage(String playerName, String message) {
        return composeMessage(playerName, message, null, null, null);
    }

    private String composeMessage(String playerName, String message, String playerHoverText) {
        return composeMessage(playerName, message, playerHoverText, null, null);
    }

    private String composeMessage(String playerName, String message, String playerHoverText, String clickEventCommandString) {
        return composeMessage(playerName, message, playerHoverText, clickEventCommandString, null);
    }

    private String composeMessage(String playerName, String message, String playerHoverText, String clickEventCommandString, String color) {
        ArrayList messageJSON = new ArrayList();
        messageJSON.add("");

        HashMap playerNameJSON = new HashMap();
        String composedPlayerNameText = String.format("%s", playerName);
        playerNameJSON.put("text", composedPlayerNameText);

        if (playerHoverText != null) {
            HashMap hoverJSON = new HashMap();
            hoverJSON.put("action", "show_text");
            hoverJSON.put("value", playerHoverText);
            playerNameJSON.put("hoverEvent", hoverJSON);
        }

        messageJSON.add(playerNameJSON);

        HashMap messageBodyJSON = new HashMap();
        messageBodyJSON.put("text", message);

        if (clickEventCommandString != null) {
            HashMap clickJSON = new HashMap();
            clickJSON.put("action", "run_command");
            clickJSON.put("value", clickEventCommandString);
            messageBodyJSON.put("clickEvent", clickJSON);
        }

        if (color != null) {
            messageBodyJSON .put("color", color);
        }

        messageJSON.add(messageBodyJSON);

        Gson gson = new Gson();
        String jsonString = gson.toJson(messageJSON);

        //String jsonString = "[\"\", {\"text\":\"" + playerName + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverText + "\"}}, {\"text\":\"" + message + "\"}]";
        return jsonString;
    }

    private static void sendJsonMessage(Player p, String s) {
        ( (CraftPlayer)p ).getHandle().playerConnection.sendPacket( createPacketPlayOutChat(s) );
    }

    private static PacketPlayOutChat createPacketPlayOutChat(String s) {
        IChatBaseComponent comp = ChatSerializer.a(s);
        return new PacketPlayOutChat(comp);
    }
}