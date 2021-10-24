/*
 * Copyright (c) 2021. Vicen621.
 * All rights reserved.
 */

package io.github.vicen621.manhunt.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

public class Utils {

    public static Scoreboard getScoreboard() {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        return manager.getMainScoreboard();
    }

    public static boolean canBreak(ItemStack item, Block bl, Entity entity) {
        return !bl.getDrops(item, entity).isEmpty();
    }

    public static void setDurability(ItemStack item, Player p) {

        if (!(item.getItemMeta() instanceof Damageable)) return;

        Map<Enchantment, Integer> enchants = item.getEnchantments();
        if (!enchants.isEmpty() && item.containsEnchantment(Enchantment.DURABILITY)) {
            int chance = 100 / (enchants.get(Enchantment.DURABILITY) + 1);
            if (new Random().nextInt(100) + 1 <= chance) {
                item.setDurability((short) (item.getDurability() + 1));
            }
        } else {
            item.setDurability((short) (item.getDurability() + 1));
        }

        if (item.getDurability() == item.getType().getMaxDurability()) {
            p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
            for (Player all : Bukkit.getOnlinePlayers())
                all.playSound(p.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
        }
    }

    public static Integer getRangeInt(Integer min, Integer max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static void getConnectedBlocks(Block b1, final int x1, final int y1, final int z1, ArrayList<Block> list) {
        for (int x = -1; x <= 1; x++) { //These 3 for loops check a 3x3x3 cube around the block in question
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) { //We can skip the 0,0,0 case because that is the block in question
                        continue;
                    }
                    Block b2 = b1.getRelative(x, y, z);
                    int blockX = b2.getX();
                    int blockY = b2.getY();
                    int blockZ = b2.getZ();

                    if (blockX == x1 && blockY == y1 && blockZ == z1) { //Makes sure the original block is never added to veinOres
                        continue;
                    }

                    if (b2.getType() == b1.getType()) {
                        if (!list.contains(b2)) {
                            list.add(b2);
                            Utils.getConnectedBlocks(b2, x1, y1, z1, list);
                        }
                    }
                }
            }
        }
    }

    public static boolean isTool(Material type) {
        return type.toString().toLowerCase().endsWith("_axe") || type.toString().toLowerCase().endsWith("_pickaxe") ||
               type.toString().toLowerCase().endsWith("_shovel") || type.toString().toLowerCase().endsWith("_hoe") ||
               type == Material.SHEARS;
    }

    public static int applyFortune(ItemStack hand, int amount) {
        if (hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) < 1)
            return amount;
        return (int) (amount * (Math.max((Math.random() * (hand.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 2)) - 1, 0) + 1));
    }

    public static void createSetItem(Inventory inv, Material material, int amount, int invSlot, String displayName, List<String> loreList) {
        List<String> lore = new ArrayList<>();
        loreList.forEach(s -> lore.add(StringUtils.chat(s)));
        ItemStack item = new ItemBuilder(material).amount(amount).name(StringUtils.chat(displayName)).flags(ItemFlag.values()).lore(lore).build();
        inv.setItem(invSlot - 1, item);
    }
}
