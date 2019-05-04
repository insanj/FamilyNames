package me.insanj.familynames;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class FamilyNamesPlugin extends JavaPlugin {
	@Override
	public void onEnable() {
		getCommand("family").setExecutor(new FamilyNamesCommandExecutor());
    
    Bukkit.getServer().getPluginManager().registerEvents(new FamilyNamesChatListener(this), this);
  }
}
