package me.insanj.familynames;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.Sound;

public class FamilyNamesCommandExecutor implements CommandExecutor {

  	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length <= 0) {
            return false;
        }

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player =  (Player)sender;

        if (player.isOp() == false) {
            return false;
        }


        String playerName = args[0];
        try {
            player = Bukkit.getPlayer(playerName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
