package com.insanj.familynames.util;

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

import javax.json.JSONObject;
import javax.json.JSONArray;

public class FamilyNamesChatComposer {
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
        JSONArray messageJSON = new JSONArray();
        messageJSON.add("");

        JSONObject playerNameJSON = new JSONObject();
        String composedPlayerNameText = String.format("%s", playerName);
        playerNameJSON.put("text", composedPlayerNameText);

        if (playerHoverText != null) {
            JSONObject hoverJSON = new JSONObject();
            hoverJSON.put("action", "show_text");
            hoverJSON.put("value", playerHoverText);
            playerNameJSON.put("hoverEvent", hoverJSON);
        }

        messageJSON.add(playerNameJSON);

        JSONObject messageBodyJSON = new JSONObject();
        messageBodyJSON.put("text", message);

        if (clickEventCommandString != null) {
            JSONObject clickJSON = new JSONObject();
            clickJSON.put("action", "run_command");
            clickJSON.put("value", clickEventCommandString);
            messageBodyJSON.put("clickEvent", clickJSON);
        }

        if (color != null) {
            messageBodyJSON .put("color", color);
        }

        messageJSON.add(messageBodyJSON);

        String jsonString = messageJSON.toString();

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