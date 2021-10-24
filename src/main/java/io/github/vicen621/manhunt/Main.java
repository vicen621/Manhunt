/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import io.github.vicen621.manhunt.Managers.GameManager;
import io.github.vicen621.manhunt.Utils.Annotations.RegisterExecutor;
import io.github.vicen621.manhunt.Utils.StringUtils;
import io.github.vicen621.manhunt.Utils.Utils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    @Getter
    private static GameManager manager;
    private static TaskChainFactory taskChainFactory;

    @Override
    public void onEnable() {
        instance = this;
        manager = new GameManager();
        taskChainFactory = BukkitTaskChainFactory.create(this);
        RegisterScoreboards();
        new RegisterExecutor().register();
        getLogger().info("Bringing out Rex the Test Dog...");
        getLogger().info("Feeding him...");
        getLogger().info("Reasurring him...");
        getLogger().info("Sending Rex the Test Dog...");
        getLogger().info("Ouch...");
        getLogger().info("Buying new Rex...");
        getLogger().info("Sending...");
        getLogger().info("Plugin initialization complete.");
        getLogger().info("Plugin made by: Vicen621");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    private void RegisterScoreboards() {
        Scoreboard board = Utils.getScoreboard();
        if (board.getObjective("hp") == null) {
            Objective ob = board.registerNewObjective("hp", Criterias.HEALTH, StringUtils.chat("&c♥"));
            ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        if (board.getObjective("vida") == null) {
            Objective ob1 = board.registerNewObjective("vida", Criterias.HEALTH);
            ob1.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            ob1.setRenderType(RenderType.HEARTS);
        }
    }
}
