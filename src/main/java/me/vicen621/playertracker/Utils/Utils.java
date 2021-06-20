package me.vicen621.playertracker.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.Collection;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Copyright (c) 2021. Vicen621
 * All rights reserved.
 */

public class Utils {
    private static final Scoreboard BOARD = Bukkit.getScoreboardManager().getMainScoreboard();
    private static final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public static String chat(String s) {
        if (Bukkit.getVersion().contains("1.16") || Bukkit.getVersion().contains("1.17")) {
            s = s.replace("&#", "#").replace("&k", "");
            Matcher match = pattern.matcher(s);
            while (match.find()) {
                String color = s.substring(match.start(), match.end());
                s = s.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                match = pattern.matcher(s);
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void broadcastToTeam(Team team, String message) {
        for (OfflinePlayer teammate : team.getPlayers()) {
            Player online = teammate.getPlayer();

            if (online == null) {
                continue;
            }

            online.sendMessage(message);
        }
    }

    public static Boolean hasTeam(OfflinePlayer p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getMainScoreboard();
        return board.getPlayerTeam(p) != null;
    }

    public static Team getTeam(OfflinePlayer p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getMainScoreboard();
        if (hasTeam(p)) {
            return board.getPlayerTeam(p);
        }
        return null;
    }

    private static String getColorTeam(OfflinePlayer p) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getMainScoreboard();
        if (board.getPlayerTeam(p) != null) {
            return Objects.requireNonNull(board.getPlayerTeam(p)).getColor().toString();
        }
        return "&f";
    }

    public static String setColor(OfflinePlayer p) {
        return hasTeam(p) ? getColorTeam(p) : "&7";
    }


    public static boolean canBreak(ItemStack item, Block bl, Entity entity) {
        Collection<ItemStack> result = bl.getDrops(item, entity);
        return !result.isEmpty();
    }
}
