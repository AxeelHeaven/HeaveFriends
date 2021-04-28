package com.axeelheaven.friends.listeners;

import com.axeelheaven.friends.Main;
import com.axeelheaven.friends.storage.PlayerData;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class JoinHandler implements Listener {

	private Main plugin;
	
	public JoinHandler(final Main main) {
		this.plugin = main;
	}
	
	@EventHandler
	public void JoinEvent(PostLoginEvent event) {
		final ProxiedPlayer player = event.getPlayer();
		if(!this.plugin.getSettings().getData().containsKey(player.getName())) {
			this.plugin.getSettings().getData().put(player.getName(), new PlayerData(this.plugin, player.getName()));
			ProxyServer.getInstance().getLogger().info(c("&7The configuration of the '" + player.getName() + "' user was loaded, '" + this.plugin.getSettings().getData(player.getName()).getFriends().size() + "' friends in total."));
		}
		final PlayerData pd = (PlayerData)this.plugin.getSettings().getData().get(player.getName());
		if(!pd.getFriends().isEmpty()) {
			for (ProxiedPlayer friends : BungeeCord.getInstance().getPlayers()) {
				boolean cancelar = false;
				if(cancelar) {
					break;
				}
				for (String name : pd.getFriends()) {
					if(friends.getName().equalsIgnoreCase(name)){
						friends.sendMessage(new TextComponent(c(this.plugin.getLang().getString("alert_join_friend").replace("<name>", friends.getName()))));
						cancelar = true;
						break;
					}
				}
			}
		}
		if(!pd.getRequest().isEmpty()) {
			player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("join_request_alert"))));
		}
	}
	
	@EventHandler
	public void DisconnectEvent(PlayerDisconnectEvent event) {
		final ProxiedPlayer player = event.getPlayer();
		final PlayerData pd = (PlayerData)this.plugin.getSettings().getData().get(player.getName());

		for (ProxiedPlayer friends : BungeeCord.getInstance().getPlayers()) {
			boolean cancelar = false;
			if(cancelar) {
				break;
			}
			for (String name : pd.getFriends()) {
				if(friends.getName().equalsIgnoreCase(name)){
					friends.sendMessage(new TextComponent(c(this.plugin.getLang().getString("alert_leave_friend").replace("<name>", friends.getName()))));
					cancelar = true;
					break;
				}
			}
		}
	}

	private String c(String c) {
		return ChatColor.translateAlternateColorCodes('&', c);
	}
	
}
