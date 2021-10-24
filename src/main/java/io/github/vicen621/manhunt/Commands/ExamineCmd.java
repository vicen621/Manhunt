/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt.Commands;

import io.github.vicen621.manhunt.Main;
import io.github.vicen621.manhunt.Utils.Annotations.Register;
import io.github.vicen621.manhunt.Utils.ItemBuilder;
import io.github.vicen621.manhunt.Utils.StringUtils;
import io.github.vicen621.manhunt.Utils.Utils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Register(name = "examine")
public record ExamineCmd(Main plugin) implements Listener, CommandExecutor {

    private static final DecimalFormat df = new DecimalFormat("0.0");

    private static ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(StringUtils.chat(name));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        item.setItemMeta(meta);

        return item;
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() == GameMode.SPECTATOR && e.getRightClicked() instanceof Player &&
            e.getPlayer().hasPermission("manhunt.examine") && e.getHand() == EquipmentSlot.HAND) {
            p.chat("/examine " + e.getRightClicked().getName());
        }
    }

    @EventHandler
    public void onInventory(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        for (Player spec : Bukkit.getOnlinePlayers()) {
            if (spec.getGameMode() != GameMode.SURVIVAL && spec.getOpenInventory().getTitle().contains(p.getName())) {
                openInv(spec, p);
            }
        }
    }

    @EventHandler
    public void onEat(PlayerItemConsumeEvent e) {
        Player p = e.getPlayer();

        for (Player spec : Bukkit.getOnlinePlayers()) {
            if (spec.getGameMode() != GameMode.SURVIVAL && spec.getOpenInventory().getTitle().contains(p.getName())) {
                openInv(spec, p);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        for (Player spec : Bukkit.getOnlinePlayers()) {
            if (spec.getGameMode() != GameMode.SURVIVAL && spec.getOpenInventory().getTitle().contains(p.getName())) {
                openInv(spec, p);
            }
        }
    }

    @EventHandler
    public void onPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            for (Player spec : Bukkit.getOnlinePlayers()) {
                if (spec.getGameMode() != GameMode.SURVIVAL && spec.getOpenInventory().getTitle().contains(p.getName())) {
                    openInv(spec, p);
                }
            }
        }
    }

    @EventHandler
    public void onThrow(InventoryDragEvent e) {
        Player p = (Player) e.getWhoClicked();

        for (Player spec : Bukkit.getOnlinePlayers()) {
            if (spec.getGameMode() != GameMode.SURVIVAL && spec.getOpenInventory().getTitle().contains(p.getName())) {
                openInv(spec, p);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("examine") && sender.hasPermission("coso.cosito")) {
            if (sender instanceof ConsoleCommandSender) {
                StringUtils.sendMessage(sender, "&cEste comando solo puede ser usado por jugadores");
                return true;
            }

            Player p = (Player) sender;

            if (args.length == 0) {
                StringUtils.sendMessage(sender, "&cUsage: /examine <Player>");
                return true;
            }
            if (args.length == 1) {
                Player t = Bukkit.getPlayer(args[0]);

                if (p.getGameMode() == GameMode.SURVIVAL) {
                    StringUtils.sendMessage(sender, "&cEste comando solo puede ser usado en Espectador");
                    return true;
                }

                if (t == null) {
                    StringUtils.sendMessage(sender, "&cEl jugador tiene que estar online!");
                    return true;
                }

                openInv(p, t);
            }
        }
        return false;
    }

    public void openInv(Player p, Player t) {

        Inventory inv = Bukkit.createInventory(null, 6 * 9, "§7Inventory of §b" + t.getName());

        List<String> lore = new ArrayList<>();

        for (PotionEffect effect : t.getActivePotionEffects()) {
            String Effect = effect.getType().getName().toLowerCase().replace("_", " ");

            if (Effect.equalsIgnoreCase("night vision"))
                continue;

            String duration = StringUtils.formatTime(effect.getDuration() / 20);
            String nivel = "I";

            switch (Effect) {
                case "damage resistance" -> Effect = "Resistance";
                case "fast digging" -> Effect = "Haste";
                case "harm" -> Effect = "Instant Damage";
                case "heal" -> Effect = "Instant Health";
                case "increase damage" -> Effect = "Strength";
                case "jump" -> Effect = "Jump Boost";
                case "slow" -> Effect = "Slowness";
                case "slow digging" -> Effect = "Mining Fatigue";
            }

            switch (effect.getAmplifier()) {
                case 0 -> nivel = "I";
                case 1 -> nivel = "II";
                case 2 -> nivel = "III";
                case 3 -> nivel = "IV";
                case 4 -> nivel = "V";
                case 5 -> nivel = "VI";
                case 6 -> nivel = "VII";
                case 7 -> nivel = "VIII";
                case 8 -> nivel = "IX";
                case 9 -> nivel = "X";
            }
            lore.add(StringUtils.chat(" &8» &d" + WordUtils.capitalize(Effect) + " &b" + nivel + " &7| &3" + duration));
        }

        if (lore.isEmpty()) {
            lore.add(StringUtils.chat("&bNo Effects"));
        }

        ItemStack potion = new ItemBuilder(Material.POTION).potionColor(Color.NAVY)
                .flags(ItemFlag.values()).name(StringUtils.chat("&7Effects:")).lore(lore).build();

        for (int i = 0; i < 36; i++)
            inv.setItem(i, t.getInventory().getItem(i));

        for (int i = 36; i < 45; i++)
            inv.setItem(i, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));

        if (t.getInventory().getHelmet() == null) {
            inv.setItem(45, createItem(Material.GLASS_PANE, "&cHelmet"));
        } else {
            inv.setItem(45, t.getInventory().getHelmet());
        }
        if (t.getInventory().getChestplate() == null) {
            inv.setItem(46, createItem(Material.GLASS_PANE, "&cChestplate"));
        } else {
            inv.setItem(46, t.getInventory().getChestplate());
        }
        if (t.getInventory().getLeggings() == null) {
            inv.setItem(47, createItem(Material.GLASS_PANE, "&cLeggings"));
        } else {
            inv.setItem(47, t.getInventory().getLeggings());
        }
        if (t.getInventory().getBoots() == null) {
            inv.setItem(48, createItem(Material.GLASS_PANE, "&cBoots"));
        } else {
            inv.setItem(48, t.getInventory().getBoots());
        }
        if (t.getInventory().getItemInOffHand().getType() == Material.AIR) {
            inv.setItem(49, createItem(Material.GLASS_PANE, "&cOff-Hand"));
        } else {
            inv.setItem(49, t.getInventory().getItemInOffHand());
        }
        inv.setItem(50, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(51, createItem(Material.BLACK_STAINED_GLASS_PANE, " "));
        inv.setItem(52, potion);
        Utils.createSetItem(inv, Material.BOOK, 1, 54, "&7Info", Arrays.asList("&8» &bHearts: &c" + df.format((t.getHealth() + t.getAbsorptionAmount()) / 2), "&8» &bHunger: &c" + df.format(t.getFoodLevel() / 2), "&8» &bLevels: &c" + t.getLevel(), "&8» &bLocation: &cX: " + t.getLocation().getBlockX() + " Y: " + t.getLocation().getBlockY() + " Z: " + t.getLocation().getBlockZ()));

        p.openInventory(inv);
    }
}
