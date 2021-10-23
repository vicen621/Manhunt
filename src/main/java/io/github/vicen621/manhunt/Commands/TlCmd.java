/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt.Commands;

import io.github.vicen621.manhunt.Main;
import io.github.vicen621.manhunt.Utils.Annotations.Register;
import io.github.vicen621.manhunt.Utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Register(name = "tl")
public record TlCmd(Main plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player p) {

            if (!p.hasPermission("manhunt.tl")) {
                StringUtils.sendMessage(sender, "&cYou don't have permission to run this command!");
                return true;
            }

            if (!Main.getManager().isHunter(p) && !Main.getManager().isRunner(p)) {
                StringUtils.sendMessage(p, "&cYou're not a hunter or a runner");
                return true;
            }

            String format = "&#313535[&#00ffb9TL&#313535] &#4b5061» &7&3&o{name}&8&o: &7X: &a{x} &7Y: &a{y} &7Z: &a{z} &8(&c{dimention}&8)";

            Location loc = p.getLocation();

            format = format.replace("{name}", p.getName());
            format = format.replace("{x}", "" + loc.getBlockX());
            format = format.replace("{y}", "" + loc.getBlockY());
            format = format.replace("{z}", "" + loc.getBlockZ());
            format = format.replace("{dimention}", environment(p.getWorld()));

            StringUtils.broadcastToTeam(Main.getManager().isHunter(p) ? "hunters" : "runners", format);
        }
        return false;
    }

    private String environment(World world) {
        return switch (world.getEnvironment()) {
            case NORMAL -> "Overworld";
            case NETHER -> "Nether";
            case THE_END -> "The End";
            default -> "Unknown";
        };
    }
}
