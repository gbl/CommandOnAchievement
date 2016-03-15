/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.guntram.bukkit.commandonachievement;

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author gbl
 */
public class CommandOnAchievement extends JavaPlugin implements Listener {

    static boolean havePlaceholders=false;
    final String playerPlaceholder="%player_name%";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            havePlaceholders=true;
        }
    }

    @Override
    public void onDisable() {
    }
    
    @EventHandler
    public void onPlayerAchievement(PlayerAchievementAwardedEvent e) {
        Player player=e.getPlayer();
        Achievement achievement=e.getAchievement();
        List<String> entries=this.getConfig().getStringList(achievement.toString().toLowerCase());
        for (String s:entries) {
            s=org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
            if (havePlaceholders) {
                s=PlaceholderAPI.setPlaceholders(player, s);
            } else {
                for (int i=0; i<s.length(); i++) {
                    if (s.charAt(i)=='%' && s.regionMatches(i, playerPlaceholder, 0, playerPlaceholder.length()))
                        s=s.substring(0, i)+player.getName()+s.substring(i+playerPlaceholder.length());
                }
            }
            if (s.startsWith("/"))
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), s.substring(1));
            else
                player.sendMessage(s);
        }
    }
}
