package io.github.vicen621.manhunt.Commands;

import io.github.vicen621.manhunt.Main;
import io.github.vicen621.manhunt.Utils.Annotations.Register;
import io.github.vicen621.manhunt.Utils.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@Register(name = "runner")
public record RunnerCmd(Main plugin) implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if (!sender.hasPermission("manhunt.runner")) {
            StringUtils.sendMessage(sender, "&cYou don't have permission to run this command!");
            return true;
        }

        if (args.length != 1) {
            StringUtils.sendMessage(sender, "&cUsage: /runner <player>");
            return true;
        }

        Player p = Bukkit.getPlayer(args[0]);

        if (p == null) {
            StringUtils.sendMessage(sender, "&cThe player must be online!");
            return true;
        }

        if (Main.getManager().isHunter(p)) {
            StringUtils.sendMessage(sender, "&cThe player you're trying to choose is a hunter, please choose other player!");
            return true;
        }

        if (Main.getManager().isRunner(p)) Main.getManager().removeRunner(p);
        else Main.getManager().addRunner(p);

        return true;
    }
}
