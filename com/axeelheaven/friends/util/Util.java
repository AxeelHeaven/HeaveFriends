package com.axeelheaven.friends.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Util {

	public static void EjecutarComando(final ProxiedPlayer p, final String message, final String command){
		TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
		component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		p.sendMessage(component);
	}
	
	public static void RecomendarComando(final ProxiedPlayer p, final String message, final String command){
		TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
		component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
		p.sendMessage(component);
	}

	public static TextComponent EjecutarComando2(final ProxiedPlayer p, final String message, final String command){
		TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
		component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		return component;
	}

}
