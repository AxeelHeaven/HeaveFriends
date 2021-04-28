package com.axeelheaven.friends.commands;

import com.axeelheaven.friends.Main;
import com.axeelheaven.friends.storage.PlayerData;
import com.axeelheaven.friends.util.Util;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class FriendCommand extends Command{

	private Main plugin;
	
	public FriendCommand(Main main) {
		super("friend", "", "f", "friends", "amigo", "amigos");
		this.plugin = main;
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof ProxiedPlayer)) {
			sender.sendMessage(new TextComponent(c("&cOnly users can use this command.")));
			return;
		}
		
		ProxiedPlayer player = (ProxiedPlayer) sender;
		PlayerData pd = this.plugin.getSettings().getData(player.getName());
		
		if(this.plugin.getConfig().getStringList("disable_servers").contains(player.getServer().getInfo().getName())) {
			player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("server_blocked"))));
			return;
		}
		
		if(args.length == 0) {
			for(String c : this.plugin.getLang().getStringList("command_friend_help")) {
				player.sendMessage(new TextComponent(c(c)));
			}
			return;
		}
		

		if(args[0].equalsIgnoreCase("tp")) {
			if(BungeeCord.getInstance().getPlayer(args[1]) == null) {
				sender.sendMessage(new TextComponent(c(this.plugin.getLang().getString("offline_player"))));
				return;
			}
			if(player.getServer().getInfo().getName().equals(BungeeCord.getInstance().getPlayer(args[1]).getServer().getInfo().getName())) {
				sender.sendMessage(new TextComponent(c(this.plugin.getLang().getString("server_connected"))));
			}else {
				player.connect(BungeeCord.getInstance().getPlayer(args[1]).getServer().getInfo());
			}
			return;
		}
		
		if(args[0].equalsIgnoreCase("list")) {
			sender.sendMessage(new TextComponent(c(this.plugin.getLang().getString("friends_list"))));
			if(pd.getFriends().isEmpty()) {
				sender.sendMessage(new TextComponent(c(this.plugin.getLang().getString("friends_list_empty"))));
				return;
			}
			int page = 1;
			if(args.length == 2) {
				try {
					page = Integer.parseInt(args[1]);
				}catch(Exception e) {
					page = 1;
				}
			}
			int entriesPerPage = 10;
			int startIndex = (page - 1) * entriesPerPage;
			int endIndex = startIndex + entriesPerPage;
			if (endIndex > pd.getFriends().size()) {
				   endIndex = pd.getFriends().size();
			}
			
			try {
				for(String friends : pd.getFriends().subList(startIndex, endIndex)) {
					Util.EjecutarComando(player, this.plugin.getLang().getString("friends_list_username").replace("<username>", friends)
							.replace("<status>", (BungeeCord.getInstance().getPlayer(friends) != null ? "Online" : "Offline")).replace("<username>", friends), "/friends tp " + friends);

				}
			} catch (Exception e) {
				sender.sendMessage(new TextComponent(c(this.plugin.getLang().getString("page_error"))));
			}
			
			if(pd.getFriends().size() >= (entriesPerPage * page)) {
				Util.EjecutarComando(player, c(this.plugin.getLang().getString("friends_list_next_page")), "/friends list " + (page + 1));
			}
			return;
		}

		if(args[0].equalsIgnoreCase("accept")) {
			if(args.length != 2) {
				player.sendMessage(new TextComponent(c("&cCorrect usage: '/friends accept <username>'")));
				return;
			}

			if(pd.getFriends().contains(args[1])) {
				pd.getFriends().remove(args[1]);
				player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("is_your_friend"))));
				return;
			}

			if(!pd.getRequest().contains(args[1])) {
				player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("error_username_no_requesting").replace("<name>", args[1]))));
				return;
			}
			
			if(!player.hasPermission("heaven.maxfriends.*")) {
				for(String permissions : this.plugin.getSettings().getPermissions().keySet()) {
					boolean cancelar = false;
					if(cancelar) {
						break;
					}
					if(!player.hasPermission(permissions)) {
						player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("no_permission_add_friends"))));
						cancelar = true;
						return;
					}
				}
				
				for(String permissions : this.plugin.getSettings().getPermissions().keySet()) {
					boolean cancelar = false;
					if(cancelar) {
						break;
					}
					if(player.hasPermission(permissions) && (this.plugin.getSettings().getPermissions().get(permissions) >= pd.getFriends().size())) {
						player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("friends_limit"))));
						cancelar = true;
						return;
					}	
				}
			}

			PlayerData targetData = this.plugin.getSettings().getData(args[1]);
			
			pd.getFriends().add(args[1]);
			targetData.getFriends().add(sender.getName());
			pd.getRequest().remove(args[1]);
			
			player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("accept_new_friend").replace("<name>", args[1]))));
			if(BungeeCord.getInstance().getPlayer(args[1]) != null && BungeeCord.getInstance().getPlayer(args[1]).isConnected()) {
				ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[1]);
				target.sendMessage(new TextComponent(c(this.plugin.getLang().getString("accept_your_request").replace("<name>", sender.getName()))));
			}
			return;
		}
		
		if(args[0].equalsIgnoreCase("request")) {
			if(args.length == 3 && args[1].equalsIgnoreCase("remove")) {
				if(args[2].equalsIgnoreCase("all")) {
					pd.getRequest().clear();
					player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("request_clear"))));
				}else {
					if(!pd.getRequest().contains(args[2])) {
						player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("no_name_request"))));
						return;
					}
					pd.getRequest().remove(args[2]);
					player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("request_clear_correctly").replace("<username>", args[2]))));
				}
				return;
			}
			player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("friends_request"))));
			if(pd.getRequest().isEmpty()) {
				player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("friends_request_empty"))));
				return;
			}
			int page = 1;
			if(args.length == 2) {
				try {
					page = Integer.parseInt(args[1]);
				}catch(Exception e) {
					page = 1;
				}
			}
			int entriesPerPage = 10;
			int startIndex = (page - 1) * entriesPerPage;
			int endIndex = startIndex + entriesPerPage;
			if (endIndex > pd.getRequest().size()) {
				   endIndex = pd.getRequest().size();
			}
			try {
				for(String request : pd.getRequest().subList(startIndex, endIndex)) {
					Util.EjecutarComando(player, this.plugin.getLang().getString("friends_request_username").replace("<status>", (BungeeCord.getInstance().getPlayer(request) != null ? "Online" : "Offline")).replace("<username>", request), "/friends accept " + request);
				}
			} catch (Exception e) {
				sender.sendMessage(new TextComponent(c(this.plugin.getLang().getString("page_error"))));
			}
			
			if(pd.getRequest().size() >= (entriesPerPage * page)) {
				Util.EjecutarComando(player, c(this.plugin.getLang().getString("friends_request_next_page")), "/friends request " + (page + 1));
			}
			return;
		}
		
		if(args[0].equalsIgnoreCase("add")) {
			if(args.length != 2) {
				player.sendMessage(new TextComponent(c("&cCorrect usage: '/friends add <username>'")));
				return;
			}
			if(player.getName().equalsIgnoreCase(args[1])) {
				player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("player_add_error"))));
				return;
			}
				
			PlayerData targetData = this.plugin.getSettings().getData(args[1]);
			if(pd.getFriends().contains(args[1])) {
				player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("is_your_friend"))));
				return;
			}
			if(targetData.getRequest().contains(player.getName())) {
				player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("already_send_request"))));
				return;
			}
			if(targetData.getRequest().size() >= this.plugin.getConfig().getInt("settings.maximum_friend_requests")) {
				player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("maximum_friend_requests"))));
				return;
			}
			targetData.getRequest().add(player.getName());
			if(BungeeCord.getInstance().getPlayer(args[1]) != null && BungeeCord.getInstance().getPlayer(args[1]).isConnected()) {
				ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[1]);
				target.sendMessage(new TextComponent(c(this.plugin.getLang().getString("friend_request_received").replace("<name>", sender.getName()))));
			}
			player.sendMessage(new TextComponent(c(this.plugin.getLang().getString("friend_request_sent").replace("<name>", args[1]))));
			return;
		}
	}

	private String c(String c) {
		return ChatColor.translateAlternateColorCodes('&', c);
	}
	
}
