package com.axeelheaven.friends;

import java.io.File;
import java.io.IOException;

import com.axeelheaven.friends.commands.*;
import com.axeelheaven.friends.listeners.*;
import com.axeelheaven.friends.settings.*;
import com.axeelheaven.friends.storage.PlayerData;
import com.axeelheaven.friends.util.*;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin {
	
	private Settings settings;
	private Configuration configuration;
	private Configuration storage;
	private Configuration messages;

	@Override
	public void onEnable(){
		this.configuration = this.loadDefaultConfig("config");
		this.storage = this.loadDefaultConfig("database");
		this.messages = this.loadDefaultConfig("messages_" + this.getConfig().getString("language").toLowerCase());
		this.sender("&7HeavenFriends: Your language has been uploaded to the file: " + ("messages_" + this.getConfig().getString("language").toLowerCase() + ".yml"));
		
		this.settings = new Settings(this);
		this.loadHandlers();

		this.sender(" ");
		this.sender("§bHeavenFriends: §fPlugin enabled correctly &7(License PREMIUM)");
		this.sender("§bHeavenFriends: §fThis plugin was developed by: @AxeelHeaven");
		this.sender(" ");
	}
	
	@Override
	public void onDisable() {
		for(PlayerData pd : this.getSettings().getData().values()) {
			String path = "friends." + pd.getName() + ".";
			this.getStorage().set(path + "friends", pd.getFriends());
			this.getStorage().set(path + "request", pd.getRequest());
		}
		this.saveStorage();
	}
	
	public void sender(String string) {
		ProxyServer.getInstance().getLogger().info(c(string));
	}

	private void loadHandlers() {
	    PluginManager pm = ProxyServer.getInstance().getPluginManager();
	    /** Eventos listener */
		pm.registerListener(this, new JoinHandler(this));

	    /** Comandos */
		pm.registerCommand(this, new FriendCommand(this));
		
	}

	private String c(String c) {
		return ChatColor.translateAlternateColorCodes('&', c);
	}
	
	public Settings getSettings() {
		return this.settings;
	}

	public Configuration getConfig() {
		return this.configuration;
	}

	public Configuration getLang() {
		return this.messages;
	}

	public Configuration getStorage() {
		return this.storage;
	}
	
	private Configuration loadDefaultConfig(final String name) {
		try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new FileConfig(this).loadConfiguration(name + ".yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public void saveStorage(){
		File localFile = new File(this.getDataFolder(), "database.yml");
		try{
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.storage, localFile);
		}catch (Exception localException){
			localException.printStackTrace();
		}
	}
	
}
