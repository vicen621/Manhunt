/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt.Managers;

import io.github.vicen621.manhunt.Main;
import io.github.vicen621.manhunt.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class GameRunnable extends BukkitRunnable {

    GameManager manager;

    public GameRunnable() {
        manager = Main.getManager();
        manager.setStarted(true);
    }

    @Override
    public void run() {
        if (manager.getHunterCount() == 0) return;
        if (manager.getRunnerCount() == 0) return;

        for (UUID uuid : manager.getHunters()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null || p.isDead()) return;
            RunnerInstance hunter = manager.getHunter(p);
            Player runner = manager.getRunner(hunter.getRunnerID());

            if (runner == null || runner.isDead()) return;

            if (p.getWorld().getEnvironment() == runner.getWorld().getEnvironment()) {
                Location runnerLoc = runner.getLocation();

                if (runnerLoc.getWorld().getEnvironment() == World.Environment.NORMAL)
                    p.setCompassTarget(runnerLoc);
                else if (runnerLoc.getWorld().getEnvironment() == World.Environment.NETHER) {
                    ItemStack item = null;
                    HashMap<Integer, ? extends ItemStack> compasses = p.getInventory().all(Material.COMPASS);
                    for (Integer key : compasses.keySet()) {
                        ItemStack compass = compasses.get(key);
                        if (manager.isTracker(compass))
                            item = compass;
                    }

                    assert item != null;
                    CompassMeta meta = (CompassMeta) item.getItemMeta();
                    meta.setLodestone(runnerLoc);
                    meta.setLodestoneTracked(false);
                    item.setItemMeta(meta);
                }
            }
        }
    }
}
