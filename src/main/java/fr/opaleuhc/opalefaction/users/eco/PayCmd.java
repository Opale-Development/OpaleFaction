package fr.opaleuhc.opalefaction.users.eco;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.users.FactionUser;
import fr.opaleuhc.opalefaction.users.FactionUserManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.util.List;

public class PayCmd implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
            return true;
        }
        FactionUser factionUser = FactionUserManager.INSTANCE.getFactionUser(p.getUniqueId());
        if (factionUser == null) {
            p.sendMessage(OpaleFaction.PREFIX + "§cErreur interne, veuillez réessayer plus tard !");
            return true;
        }
        if (args.length == 2) {
            FactionUser target = FactionUserManager.INSTANCE.getFactionUser(args[0]);
            if (target == null) {
                p.sendMessage(OpaleFaction.PREFIX + "§cCe joueur n'existe pas !");
                return true;
            }
            Player targetPlayer = Bukkit.getPlayer(target.getUuid());
            if (targetPlayer == null) {
                p.sendMessage(OpaleFaction.PREFIX + "§cTemporairement, pour des raisons de sécurité, vous ne pouvez pas envoyer d'argent à un joueur " +
                        "hors ligne ou non connecté à votre serveur !");
                return true;
            }
            if (targetPlayer.equals(p)) {
                p.sendMessage(OpaleFaction.PREFIX + "§cVous ne pouvez pas vous envoyer de l'argent à vous même !");
                return true;
            }
            double amount;
            try {
                amount = Double.parseDouble(args[1]);
            } catch (NumberFormatException e) {
                p.sendMessage(OpaleFaction.PREFIX + "§cVeuillez entrer un nombre valide !");
                return true;
            }
            if (amount <= 0) {
                p.sendMessage(OpaleFaction.PREFIX + "§cVeuillez entrer un nombre strictement supérieur à 0 !");
                return true;
            }
            if (factionUser.getMoney() < amount) {
                p.sendMessage(OpaleFaction.PREFIX + "§cVous n'avez pas assez d'argent !");
                return true;
            }
            factionUser.removeMoney(amount);
            target.addMoney(amount);
            p.sendMessage(OpaleFaction.PREFIX + "§aVous avez envoyé §e" + NumberFormat.getInstance().format(amount) + " §aà §e" + target.getName() + "§a !");
            targetPlayer.sendMessage(OpaleFaction.PREFIX + "§aVous avez reçu §e" + NumberFormat.getInstance().format(amount) + " §ade la part de §e" + p.getName() + "§a !");
            return true;
        }
        p.sendMessage(OpaleFaction.PREFIX + "§c/pay <joueur> <montant>");
        return true;
    }

    @Override
    public java.util.List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new java.util.ArrayList<>();
        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (sender.getName().equals(sender.getName())) continue;
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) completions.add(player.getName());
            }
        }
        return completions;
    }
}
