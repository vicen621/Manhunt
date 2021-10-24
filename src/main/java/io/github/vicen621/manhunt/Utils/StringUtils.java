/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt.Utils;

import io.github.vicen621.manhunt.Main;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
    @Getter
    private static final String Prefix = "&#313535[&#00ffb9ManHunt&#313535] &#4b5061» &7";

    public static String chat(String s) {
        s = s.replace("&k", "&r");
        if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")) {
            s = s.replace("&#", "#");
            Matcher match = pattern.matcher(s);
            while (match.find()) {
                String color = s.substring(match.start(), match.end());
                s = s.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                match = pattern.matcher(s);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendMessage(CommandSender p, String s) {
        p.sendMessage(chat(getPrefix() + s));
    }

    public static void broadcastToTeam(String team, String msg) {
        List<UUID> list = team.equalsIgnoreCase("runners") ? Main.getManager().getRunners() : Main.getManager().getHunters();
        for (UUID uuid : list) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) continue;
            p.sendMessage(chat(msg));
        }
    }

    public static String formatTime(int secs) {
        int remainder = secs % 86400;

        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = (remainder / 60) - (hours * 60);
        int seconds = (remainder % 3600) - (minutes * 60);

        if (days > 0) {
            return days + "d" + hours + "h" + minutes + "m" + seconds + "s";
        } else if (hours > 0) {
            return hours + "h" + minutes + "m" + seconds + "s";
        } else if (minutes > 0) {
            return minutes + "m" + seconds + "s";
        } else {
            return seconds + "s";
        }
    }
}
