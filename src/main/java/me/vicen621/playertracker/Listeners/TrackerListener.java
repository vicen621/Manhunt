package me.vicen621.playertracker.Listeners;

import me.vicen621.playertracker.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class TrackerListener implements Listener {
    public Main plugin;
    public static Location loc = null;

    public TrackerListener(Main plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (Main.Hunter.contains(p.getUniqueId())) {
            ItemStack compass = new ItemStack(Material.COMPASS);
            p.getInventory().addItem(compass);
        }
    }

    @EventHandler
    public void onDimensionChange(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        if (Main.Man.contains(p.getUniqueId())) {
            loc = e.getPlayer().getLocation();
        }
    }

    @EventHandler
    public void asd(PlayerDropItemEvent e){
        if (e.getItemDrop().getItemStack().getType() == Material.COMPASS && Main.Hunter.contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        for (ItemStack drops : e.getDrops()) {
            if (drops.getType() == Material.COMPASS) {
                e.getDrops().remove(drops);
                break;
            }
        }
    }
}
