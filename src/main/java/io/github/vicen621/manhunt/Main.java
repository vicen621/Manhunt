/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt;

import io.github.vicen621.manhunt.Managers.GameManager;
import io.github.vicen621.manhunt.Utils.Annotations.RegisterExecutor;
import io.github.vicen621.manhunt.Utils.StringUtils;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Getter
    private static Main instance;
    @Getter
    private static GameManager manager;

    @Override
    public void onEnable() {
        instance = this;
        manager = new GameManager();
        new RegisterExecutor().register();
        getLogger().info("""
                Bringing out Rex the Test Dog...
                Feeding him...
                Reasurring him...
                Sending Rex the Test Dog...
                Ouch...
                Buying new Rex...
                Sending...
                Plugin initialization complete.
                Plugin made by: Vicen621""");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
