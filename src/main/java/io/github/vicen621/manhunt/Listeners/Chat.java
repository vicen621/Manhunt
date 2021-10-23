package io.github.vicen621.manhunt.Listeners;

import io.github.vicen621.manhunt.Main;
import io.github.vicen621.manhunt.Utils.Annotations.Register;
import io.github.vicen621.manhunt.Utils.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/*
 * Copyright (c) 2021. Vicen621
 * All rights reserved.
 */

@Register
public record Chat(Main plugin) implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();

        msg = msg.replace("%", "");

        if (Main.getManager().isHunter(p)) {
            e.setFormat(StringUtils.chat("&#313535[&#0c768bHunter&#313535] &7" + p.getDisplayName() + " &#4b5061» &f" + msg));
        } else if (Main.getManager().isRunner(p)) {
            e.setFormat(StringUtils.chat("&#313535[&#e70096Runner&#313535] &7" + p.getDisplayName() + " &#4b5061» &f" + msg));
        } else if (Main.getManager().isStarted()) {
            e.setFormat(StringUtils.chat("&#313535[&#00f7ffSpectator&#313535] &7" + p.getDisplayName() + " &#4b5061» &f" + msg));
        } else {
            e.setFormat(StringUtils.chat("&7" + p.getDisplayName() + " &#4b5061» &f" + msg));
        }
    }
}
