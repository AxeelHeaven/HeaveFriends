package com.axeelheaven.friends.storage;

import java.util.*;

import com.axeelheaven.friends.Main;

import net.md_5.bungee.config.Configuration;

public class PlayerData {
	
	private String name;
	private List<String> friends;
	private List<String> friends_request;
	private Main plugin;
	
	public PlayerData(final Main main, final String name) {
		this.plugin = main;
		this.name = name;
		this.friends = new ArrayList<String>();
		this.friends_request = new ArrayList<String>();
		this.loadData();
	}

	public String getName() {
		return this.name;
	}
	
	public List<String> getFriends(){
		return this.friends;
	}
	
	public List<String> getRequest(){
		return this.friends_request;
	}
	
	private void loadData() {
		new Runnable() {
			@Override
			public void run() {
				Configuration config = plugin.getStorage();
				String path = "friends." + getName() + ".";
				if(config.getString(path + "friends") != null) {
					friends = config.getStringList(path + "friends");
					friends_request = config.getStringList(path + "request");
				}
			}
		}.run();
	}
}
