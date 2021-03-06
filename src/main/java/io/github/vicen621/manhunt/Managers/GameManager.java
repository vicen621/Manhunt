/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt.Managers;

import io.github.vicen621.manhunt.Utils.ItemBuilder;
import io.github.vicen621.manhunt.Utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public class GameManager {

    @Getter
    private final ArrayList<RunnerInstance> hunterInfo = new ArrayList<>();
    @Getter
    private final ArrayList<UUID> hunters = new ArrayList<>();
    @Getter
    private final ArrayList<UUID> runners = new ArrayList<>();
    @Getter
    @Setter
    private boolean started = false;

    public void addHunter(Player p) {
        if (hunters.contains(p.getUniqueId())) return;

        hunters.add(p.getUniqueId());
        RunnerInstance newHunter = new RunnerInstance(p);
        hunterInfo.add(newHunter);
        p.setPlayerListName(StringUtils.chat("&#0c768b" + p.getName()));
        Bukkit.broadcastMessage(StringUtils.chat(StringUtils.getPrefix() + p.getName() + " is now a &#0c768bhunter!"));
        p.getInventory().addItem(new ItemBuilder(Material.COMPASS).name("&6&lTracker").enchant(Enchantment.LUCK).flags(ItemFlag.HIDE_ENCHANTS).setCustomModelData(1).build());
    }

    public void removeHunter(Player p) {
        if (!hunters.contains(p.getUniqueId())) return;

        hunters.remove(p.getUniqueId());
        hunterInfo.remove(getHunter(p));

        p.setPlayerListName(StringUtils.chat("&f" + p.getName()));
        Bukkit.broadcastMessage(StringUtils.chat(StringUtils.getPrefix() + p.getName() + " is no longer a &#0c768bhunter!"));
        p.getInventory().remove(Material.COMPASS);
    }

    public boolean isHunter(Player p) {
        return hunters.contains(p.getUniqueId());
    }

    public RunnerInstance getHunter(Player p) {
        for (RunnerInstance hunter : hunterInfo)
            if (hunter.getHunter().equals(p.getUniqueId())) return hunter;
        return null;
    }

    public int getHunterCount() {
        return hunters.size();
    }

    public void addRunner(Player p) {
        if (runners.contains(p.getUniqueId())) return;

        runners.add(p.getUniqueId());
        p.setPlayerListName(StringUtils.chat("&#e70096" + p.getName()));
        Bukkit.broadcastMessage(StringUtils.chat(StringUtils.getPrefix() + p.getName() + " is now a &#e70096runner!"));
        p.getInventory().remove(Material.COMPASS);
    }

    public void removeRunner(Player p) {
        if (!runners.contains(p.getUniqueId())) return;

        runners.remove(p.getUniqueId());
        Bukkit.broadcastMessage(StringUtils.chat(StringUtils.getPrefix() + p.getName() + " is no longer a &#e70096runner!"));
        p.setPlayerListName(StringUtils.chat("&f" + p.getName()));
    }

    public boolean isRunner(Player p) {
        return runners.contains(p.getUniqueId());
    }

    public RunnerInstance getRunner(Player p) {
        for (RunnerInstance runner : hunterInfo)
            if (runner.getRunner().equals(p.getUniqueId())) return runner;
        return null;
    }

    public Player getRunner(int runnerID) {
        return Bukkit.getPlayer(runners.get(runnerID));
    }

    public int getRunnerCount() {
        return runners.size();
    }

    public boolean isTracker(ItemStack item) {
        return item.hasItemMeta() && item.getItemMeta().hasCustomModelData() &&
                item.getItemMeta().getCustomModelData() == 1;
    }
}
