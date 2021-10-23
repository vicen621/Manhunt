/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt.Listeners;

import io.github.vicen621.manhunt.Main;
import io.github.vicen621.manhunt.Managers.RunnerInstance;
import io.github.vicen621.manhunt.Utils.Annotations.Register;
import io.github.vicen621.manhunt.Utils.ItemBuilder;
import io.github.vicen621.manhunt.Utils.StringUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

@Register
public record HunterListener(Main plugin) implements Listener {

    @EventHandler
    public void onLeftClick(PlayerInteractEvent e) {
        if ((e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) && e.getItem() != null &&
                Main.getManager().isTracker(e.getItem())) {
            Player p = e.getPlayer();
            //If there are 0 runners
            if (Main.getManager().getRunnerCount() == 0) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(StringUtils.chat("&c&lThere are no players to track!")));
                return;
            }

            RunnerInstance hunter = Main.getManager().getHunter(p);
            if (hunter == null) {
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(StringUtils.chat("&c&lYou are not a hunter!")));
                return;
            }

            int maxRunners = Main.getManager().getRunnerCount();
            int runnerID = hunter.getRunnerID();

            runnerID++;
            if (runnerID >= maxRunners) runnerID = 0;

            hunter.setRunnerID(runnerID);

            Player runner = Main.getManager().getRunner(runnerID);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(StringUtils.chat("§6Now tracking: " + "§4§l" + runner.getDisplayName())));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (Main.getManager().isHunter(p))
            p.setPlayerListName(StringUtils.chat("&#0c768b" + p.getName()));
        else if (Main.getManager().isRunner(p))
            p.setPlayerListName(StringUtils.chat("&#e70096" + p.getName()));
        else if (Main.getManager().isStarted())
            p.setPlayerListName(StringUtils.chat("&7" + p.getName()));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        if (Main.getManager().isHunter(p)) {
            p.getInventory().addItem(new ItemBuilder(Material.COMPASS).name("&6&lTracker").enchant(Enchantment.LUCK).flags(ItemFlag.HIDE_ENCHANTS).setCustomModelData(1).build());
        }
    }

    @EventHandler
    public void asd(PlayerDropItemEvent e) {
        if (e.getItemDrop().getItemStack().getType() == Material.COMPASS && Main.getManager().isHunter(e.getPlayer()) &&
                Main.getManager().isTracker(e.getItemDrop().getItemStack())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        for (ItemStack drops : e.getDrops()) {
            if (drops.getType() == Material.COMPASS && Main.getManager().isTracker(drops)) {
                e.getDrops().remove(drops);
                break;
            }
        }
    }
}
