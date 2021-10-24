/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt.Commands;

import io.github.vicen621.manhunt.Main;
import io.github.vicen621.manhunt.Managers.GameRunnable;
import io.github.vicen621.manhunt.Utils.Annotations.Register;
import io.github.vicen621.manhunt.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Register(name = "start")
public record StartCmd(Main plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("manhunt.start")) {
            StringUtils.sendMessage(sender, "&cYou don't have permission to run this command!");
            return true;
        }

        if (Main.getManager().getRunnerCount() == 0 || Main.getManager().getHunterCount() == 0) {
            StringUtils.sendMessage(sender, "&cThere are not enough runners or hunters to start the game!");
            return true;
        }

        for (UUID uuid : Main.getManager().getHunters()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) continue;
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6*20, 200));
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6*20, 150));
            p.teleport(p.getWorld().getSpawnLocation());
        }

        for (UUID uuid : Main.getManager().getRunners()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) continue;
            p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 6*20, 200));
            p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 6*20, 150));
            Location loc = p.getWorld().getSpawnLocation().add(3, 0, 0);
            loc.setY(p.getWorld().getHighestBlockYAt(loc.getBlockX(), loc.getBlockZ()));
            p.teleport(loc);
        }

        new BukkitRunnable() {
            int i = 5;
            @Override
            public void run() {
                switch (i--) {
                    case 5 -> {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (!Main.getManager().isHunter(p) && !Main.getManager().isRunner(p)) {
                                p.setPlayerListName(StringUtils.chat("&8" + p.getName()));
                                p.setGameMode(GameMode.SPECTATOR);
                            }
                            p.sendTitle(StringUtils.chat("&45"), "");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 2);
                        }
                    }
                    case 4 -> {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendTitle(StringUtils.chat("&c4"), "");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 2);
                        }
                    }
                    case 3 -> {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendTitle(StringUtils.chat("&e3"), "");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 2);
                        }
                    }
                    case 2 -> {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendTitle(StringUtils.chat("&22"), "");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 2);
                        }
                    }
                    case 1 -> {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendTitle(StringUtils.chat("&a1"), "");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 2);
                        }
                    }
                    case 0 -> {
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            p.sendTitle(StringUtils.chat("&bGo!"), "");
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_XYLOPHONE, 1, 2);
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 20, 255, false, false));
                        }

                        new GameRunnable().runTaskTimer(plugin, 0L, 10L);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
        return true;
    }
}
