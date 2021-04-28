package com.axeelheaven.friends.settings;

import java.util.HashMap;

import com.axeelheaven.friends.Main;
import com.axeelheaven.friends.storage.PlayerData;

public class Settings {
	
	private Main plugin;
	private HashMap<String, PlayerData> data;
	private HashMap<String, Integer> permission;
	
	public Settings(Main main) {
		this.plugin = main;
		this.data = new HashMap<String, PlayerData>();
		this.permission = new HashMap<String, Integer>();
		for(String permission : this.plugin.getConfig().getSection("settings.permissions").getKeys()) {
			int maxFriends = this.plugin.getConfig().getInt("settings.permissions." + permission);
			this.getPermissions().put(permission, maxFriends);
		}
	}
	
	
	public HashMap<String, PlayerData> getData() {
		return this.data;
	}
	
	public PlayerData getData(String name) {
		if(!this.data.containsKey(name)) {
			this.data.put(name, new PlayerData(this.plugin, name));
		}
		return this.data.get(name);
	}
	
	public HashMap<String, Integer> getPermissions() {
		return this.permission;
	}
	
}
