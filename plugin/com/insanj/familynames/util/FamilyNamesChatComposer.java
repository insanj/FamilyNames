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

public class FamilyNamesChatComposer {
    public void sendMessage(String senderName, Player recipient, String message, String hoverText) {
        String jsonMessage = composeMessage(senderName, message, hoverText);
        sendJsonMessage(recipient, jsonMessage);
    }

    public String composeMessage(String playerName, String message, String hoverText) {
        String jsonString = "[\"\", {\"text\":\"" + playerName + "\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + hoverText + "\"}}, {\"text\":\"" + message + "\"}]";
        return jsonString;
    }

    public static void sendJsonMessage(Player p, String s) {
        ( (CraftPlayer)p ).getHandle().playerConnection.sendPacket( createPacketPlayOutChat(s) );
    }

    public static PacketPlayOutChat createPacketPlayOutChat(String s) {
        IChatBaseComponent comp = ChatSerializer.a(s);
        return new PacketPlayOutChat(comp);
    }
}