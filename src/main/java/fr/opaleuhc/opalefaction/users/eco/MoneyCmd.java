package fr.opaleuhc.opalefaction.users.eco;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.users.FactionUser;
import fr.opaleuhc.opalefaction.users.FactionUserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MoneyCmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 0) {
                FactionUser user = FactionUserManager.INSTANCE.getFactionUser(p.getUniqueId());
                if (user != null) {
                    p.sendMessage(OpaleFaction.PREFIX + "§aVous avez §e" + NumberFormat.getInstance().format(user.getMoney()) + " §a$");
                } else {
                    p.sendMessage(OpaleFaction.PREFIX + "§cUne erreur est survenue.");
                }
                return true;
            }
        }
        if (args.length == 1) {
            FactionUser user = FactionUserManager.INSTANCE.getFactionUser(args[0]);
            if (user != null) {
                sender.sendMessage(OpaleFaction.PREFIX + "§a" + user.getName() + " a §e" + NumberFormat.getInstance().format(user.getMoney()) + " §a$");
            } else {
                sender.sendMessage(OpaleFaction.PREFIX + "§cCe joueur n'existe pas.");
            }
            return true;
        }
        if (!sender.hasPermission("opalefaction.admin")) {
            sender.sendMessage(OpaleFaction.PREFIX + "§cUsage: /money <joueur>");
            return true;
        }
        if (args.length == 3) {
            FactionUser user = FactionUserManager.INSTANCE.getFactionUser(args[0]);
            if (user == null) {
                sender.sendMessage(OpaleFaction.PREFIX + "§cCe joueur n'existe pas.");
                return true;
            }
            if (args[1].equalsIgnoreCase("add")) {
                try {
                    Double amount = Double.parseDouble(args[2]);
                    if (amount < 0) {
                        sender.sendMessage(OpaleFaction.PREFIX + "§cLe montant doit être positif ou égal à 0.");
                        return true;
                    }
                    user.addMoney(amount);
                    sender.sendMessage(OpaleFaction.PREFIX + "§aVous avez ajouté §e" + NumberFormat.getInstance().format(amount) + " §a$ à §e" + user.getName() + "§a.");
                    Player target = OpaleFaction.INSTANCE.getServer().getPlayer(user.getUuid());
                    if (target != null) {
                        target.sendMessage(OpaleFaction.PREFIX + "§aVous avez reçu §e" + NumberFormat.getInstance().format(amount) + " §a$.");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(OpaleFaction.PREFIX + "§cLe montant doit être un nombre.");
                }
                return true;
            }
            if (args[1].equalsIgnoreCase("remove")) {
                try {
                    Double amount = Double.parseDouble(args[2]);
                    if (amount < 0) {
                        sender.sendMessage(OpaleFaction.PREFIX + "§cLe montant doit être positif ou égal à 0.");
                        return true;
                    }
                    user.removeMoney(amount);
                    sender.sendMessage(OpaleFaction.PREFIX + "§aVous avez retiré §e" + NumberFormat.getInstance().format(amount) + " §a$ à §e" + user.getName() + "§a.");
                    Player target = OpaleFaction.INSTANCE.getServer().getPlayer(user.getUuid());
                    if (target != null) {
                        target.sendMessage(OpaleFaction.PREFIX + "§aVous avez perdu §e" + NumberFormat.getInstance().format(amount) + " §a$.");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(OpaleFaction.PREFIX + "§cLe montant doit être un nombre.");
                }
                return true;
            }
            if (args[1].equalsIgnoreCase("set")) {
                try {
                    Double amount = Double.parseDouble(args[2]);
                    if (amount < 0) {
                        sender.sendMessage(OpaleFaction.PREFIX + "§cLe montant doit être positif ou égal à 0.");
                        return true;
                    }
                    user.setMoney(amount);
                    sender.sendMessage(OpaleFaction.PREFIX + "§aVous avez défini l'argent de §e" + user.getName() + " §aà §e" + NumberFormat.getInstance().format(amount) + " §a$.");
                    Player target = OpaleFaction.INSTANCE.getServer().getPlayer(user.getUuid());
                    if (target != null) {
                        target.sendMessage(OpaleFaction.PREFIX + "§aVotre argent a été défini à §e" + NumberFormat.getInstance().format(amount) + " §a$.");
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage(OpaleFaction.PREFIX + "§cLe montant doit être un nombre.");
                }
                return true;
            }
        }
        sender.sendMessage(OpaleFaction.PREFIX + "§cUsage: /money <joueur> <add|remove|set> <montant>");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (FactionUser user : FactionUserManager.INSTANCE.getFactionUsers()) {
                if (sender.getName().equals(user.getName())) continue;
                if (user.getName().toLowerCase().startsWith(args[0].toLowerCase())) completions.add(user.getName());
            }
        }
        if (!sender.hasPermission("opalefaction.admin")) {
            return completions;
        }
        if (args.length == 2) {
            completions.add("add");
            completions.add("remove");
            completions.add("set");
        }
        return completions;
    }
}
