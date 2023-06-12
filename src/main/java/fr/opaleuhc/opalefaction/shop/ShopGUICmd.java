package fr.opaleuhc.opalefaction.shop;

import fr.opaleuhc.opalefaction.shop.guis.MainShopGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ShopGUICmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cSeul un joueur peut executer cette commande.");
            return false;
        }
        new MainShopGUI().open(p);
        return false;
    }
}
