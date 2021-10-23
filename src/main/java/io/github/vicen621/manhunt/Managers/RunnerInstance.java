package io.github.vicen621.manhunt.Managers;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RunnerInstance {

    @Getter private final UUID hunter;
    @Getter @Setter private int runnerID;
    @Getter @Setter private UUID runner;

    public RunnerInstance(Player p) {
        hunter = p.getUniqueId();
        runnerID = 0;
    }
}
