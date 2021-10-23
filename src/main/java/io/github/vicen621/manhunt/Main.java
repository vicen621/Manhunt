package io.github.vicen621.manhunt;

import io.github.vicen621.manhunt.Managers.GameManager;
import io.github.vicen621.manhunt.Utils.Annotations.RegisterExecutor;
import lombok.Getter;
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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
