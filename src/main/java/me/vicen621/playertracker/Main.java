package me.vicen621.playertracker;

import java.util.ArrayList;
import java.util.UUID;

import me.vicen621.playertracker.Commands.TrackerCommand;
import me.vicen621.playertracker.Listeners.Chat;
import me.vicen621.playertracker.Listeners.TrackerListener;
import me.vicen621.playertracker.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

public final class Main extends JavaPlugin {

    public static ArrayList<UUID> Hunter = new ArrayList<>();
    public static ArrayList<UUID> Man = new ArrayList<>();

    public static String prefix = "&#313535[&#00ffb9ManHunt&#313535] &#4b5061» &7";

    public void onEnable() {
        new TrackerCommand(this);
        new TrackerListener(this);
        new Chat(this);

        Tracker();
        RegisterTeams();
        RegisterScoreboards();
    }

    private void RegisterTeams(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getMainScoreboard();
        if (board.getTeam("hunters") == null) {
            Team h = board.registerNewTeam("hunters");
            h.setColor(ChatColor.DARK_AQUA);
            h.setAllowFriendlyFire(true);
        }
        if (board.getTeam("presas") == null) {
            Team p = board.registerNewTeam("presas");
            p.setColor(ChatColor.LIGHT_PURPLE);
            p.setAllowFriendlyFire(true);
        }
    }

    private void RegisterScoreboards(){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        assert manager != null;
        Scoreboard board = manager.getMainScoreboard();
        if (board.getObjective("hp") == null) {
            Objective ob = board.registerNewObjective("hp", Criterias.HEALTH, Utils.chat("&c♥"));
            ob.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }
        if (board.getObjective("vida") == null) {
            Objective ob1 = board.registerNewObjective("vida", Criterias.HEALTH);
            ob1.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            ob1.setRenderType(RenderType.INTEGER);
        }
    }

    public void Tracker() {
        new BukkitRunnable() {
            public void run() {
                if (!Man.isEmpty()) {
                    for (UUID uuid : Hunter) {
                        Player hunter = Bukkit.getPlayer(uuid);
                        Player man = Bukkit.getPlayer(Main.Man.get(0));

                        assert hunter != null;
                        assert man != null;

                        if (hunter.getWorld() == man.getWorld()) {
                            if (hunter.getWorld().getName().equalsIgnoreCase("world")) {
                                hunter.setCompassTarget(man.getLocation());
                            }
                        } else {
                            if (hunter.getWorld().getName().equalsIgnoreCase("world") && man.getWorld().getName().equalsIgnoreCase("world_nether")) {
                                hunter.setCompassTarget(TrackerListener.loc);
                            } else if (hunter.getWorld().getName().equalsIgnoreCase("world_nether") && man.getWorld().getName().equalsIgnoreCase("world")) {
                                hunter.setCompassTarget(TrackerListener.loc);
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(this, 0L, 20L);
    }
}