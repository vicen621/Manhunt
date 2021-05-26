package me.vicen621.playertracker.Commands;

import me.vicen621.playertracker.Main;
import me.vicen621.playertracker.Utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import static org.bukkit.World.Environment.NETHER;

/*
 * Copyright (c) 2021. Vicen621
 * All rights reserved.
 */

public class TrackerCommand implements CommandExecutor {

    private final Main plugin;

    public TrackerCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("hunter").setExecutor(this);
        plugin.getCommand("pray").setExecutor(this);
        plugin.getCommand("tl").setExecutor(this);
        plugin.getCommand("nl").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getMainScoreboard();
        if (sender instanceof Player && cmd.getName().equalsIgnoreCase("hunter") && sender.isOp()) {
            if (args.length != 1) {
                sender.sendMessage(Utils.chat(Main.prefix + "&cUsage: /hunter <player>"));
                return true;
            } else {
                Player p = Bukkit.getPlayer(args[0]);

                if (p == null) {
                    sender.sendMessage(Utils.chat(Main.prefix + "&cEl jugador debe estar online!"));
                    return true;
                }

                if (Main.Hunter.contains(p.getUniqueId())) {
                    board.getTeam("hunters").removeEntry(p.getName());
                    p.setPlayerListName(Utils.chat("&f" + p.getName()));
                    Main.Hunter.remove(p.getUniqueId());
                    Bukkit.broadcastMessage(Utils.chat(Main.prefix + p.getName() + " ya no es un &ccazador!"));
                    p.getInventory().remove(Material.COMPASS);
                    return true;
                }

                Main.Man.remove(p.getUniqueId());
                board.getTeam("hunters").addEntry(p.getName());
                p.setPlayerListName(Utils.chat("&#0c768b" + p.getName()));
                Main.Hunter.add(p.getUniqueId());
                Bukkit.broadcastMessage(Utils.chat(Main.prefix + p.getName() + " ahora es un &ccazador!"));
                p.getInventory().addItem(new ItemStack(Material.COMPASS));
            }
        } else if (sender instanceof Player && cmd.getName().equalsIgnoreCase("pray") && sender.isOp()) {
            if (args.length != 1) {
                sender.sendMessage(Utils.chat(Main.prefix + "&cUsage: /pray <player>"));
                return true;
            } else {
                Player p = Bukkit.getPlayer(args[0]);

                if (p == null) {
                    sender.sendMessage(Utils.chat(Main.prefix + "&cEl jugador debe estar online!"));
                    return true;
                }

                if (Main.Man.contains(p.getUniqueId())) {
                    board.getTeam("presas").removeEntry(p.getName());
                    p.setPlayerListName(Utils.chat("&f" + p.getName()));
                    Main.Man.remove(p.getUniqueId());
                    Bukkit.broadcastMessage(Utils.chat(Main.prefix + p.getName() + " ya no es la &cPresa!"));
                    return true;
                }

                if (Main.Man.size() == 1) {
                    sender.sendMessage(Utils.chat(Main.prefix + "Ya hay una presa puesta!"));
                    return true;
                }

                Main.Hunter.remove(p.getUniqueId());
                board.getTeam("presas").addEntry(p.getName());
                p.setPlayerListName(Utils.chat("&#e70096" + p.getName()));
                Main.Man.add(p.getUniqueId());
                Bukkit.broadcastMessage(Utils.chat(Main.prefix + p.getName() + " ahora es la &cPresa!"));
                p.getInventory().remove(Material.COMPASS);
            }
        } else if (sender instanceof Player && cmd.getName().equalsIgnoreCase("tl")) {
            Player player = (Player) sender;
            Team team = Utils.getTeam(player);

            if (team == null) {
                sender.sendMessage(ChatColor.RED + "You are not on a team.");
                return true;
            }

            String prefix = Utils.chat("&#313535[&bTeam&#313535] &#4b5061» ");
            String format = "&3&o{name}&8&o: &7X: &a{x} &7Y: &a{y} &7Z: &a{z} &8(&c{dimention}&8)";

            Location loc = player.getLocation();

            format = format.replace("{name}", player.getName());
            format = format.replace("{x}", "" + loc.getBlockX());
            format = format.replace("{y}", "" + loc.getBlockY());
            format = format.replace("{z}", "" + loc.getBlockZ());
            format = format.replace("{dimention}", environment(player.getWorld()));

            Utils.broadcastToTeam(team, Utils.chat(prefix + format));
            return true;
        } else if (sender instanceof Player && cmd.getName().equalsIgnoreCase("nl")) {
            Player s = (Player) sender;

            if (Main.Man.isEmpty()) {
                sender.sendMessage(Utils.chat(Main.prefix + "Nadie es la presa!"));
                return true;
            }

            Player p = Bukkit.getPlayer(Main.Man.get(0));

            if (p == null) {
                sender.sendMessage(Utils.chat(Main.prefix + "&cLa presa esta offline!"));
                return true;
            }

            if (!Main.Hunter.contains(s.getUniqueId())) {
                sender.sendMessage(Utils.chat(Main.prefix + "&cSolo los cazadores pueden usar este comando"));
                return true;
            }
            if (s.getWorld().getEnvironment() != NETHER) {
                s.sendMessage(Utils.chat(Main.prefix + "&cEste comando solo puede ser usado en el nether!"));
                return true;
            }

            if (p.getWorld().getEnvironment() == NETHER) {
                s.sendMessage(Utils.chat(Main.prefix + "La presa esta en: &6X&7: " + p.getLocation().getBlockX() + " &6Y&7:" + p.getLocation().getBlockY() + " &6Z&7: " + p.getLocation().getBlockZ()));
            } else {
                s.sendMessage(Utils.chat(Main.prefix + "La presa no esta en el nether!"));
            }
        }
        return true;
    }

    private String environment(World world) {
        switch (world.getEnvironment()) {
            case NORMAL:
                return "Overworld";
            case NETHER:
                return "Nether";
            case THE_END:
                return "The End";
            default:
                return "Unknown";
        }
    }
}
