package me.vicen621.playertracker.Listeners;

import me.vicen621.playertracker.Main;
import me.vicen621.playertracker.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class Chat implements Listener {

    private final Main plugin;

    public Chat(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = e.getMessage();

        msg = msg.replace("%", "");

        if (Utils.hasTeam(p)) {
            Team t = Utils.getTeam(p);
            assert t != null;

            if (t.getName().equalsIgnoreCase("hunters")){
                e.setFormat(Utils.chat("&#313535[&#0c768bHunter&#313535] &7" + p.getDisplayName() + " &#4b5061» &f" + msg));
            }else if (t.getName().equalsIgnoreCase("presas")){
                e.setFormat(Utils.chat("&#313535[&#e70096Prey&#313535] &7" + p.getDisplayName() + " &#4b5061» &f" + msg));
            }
        }else{
            e.setFormat(Utils.chat("&#313535[&#00f7ffSpectator&#313535] &7" + p.getDisplayName() + " &#4b5061» &f" + msg));
        }
    }
}
