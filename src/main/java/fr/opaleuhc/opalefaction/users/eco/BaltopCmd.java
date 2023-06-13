package fr.opaleuhc.opalefaction.users.eco;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.users.FactionUser;
import fr.opaleuhc.opalefaction.users.FactionUserManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class BaltopCmd implements CommandExecutor, TabCompleter {

    public HashMap<Integer, FactionUser> getBaltop(int page) {
        HashMap<Integer, FactionUser> baltop = new HashMap<>();
        ArrayList<FactionUser> users = FactionUserManager.INSTANCE.getFactionUsers();
        users.sort((o1, o2) -> {
            return Double.compare(o2.getMoney(), o1.getMoney());
        });
        int i = 1;
        for (FactionUser user : users) {
            baltop.put(i, user);
            i++;
            if (i == page * 10) {
                break;
            }
        }
        return baltop;
    }

    public int getMaxPage() {
        return (int) Math.ceil((double) FactionUserManager.INSTANCE.getFactionUsers().size() / 10);
    }

    public String getBottomBar(int len) {
        StringBuilder sb = new StringBuilder();
        sb.append("§6§m");
        sb.append(" ".repeat(Math.max(0, len)));
        return sb.toString();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0) {
            HashMap<Integer, FactionUser> baltop = getBaltop(1);
            sender.sendMessage(OpaleFaction.PREFIX + getBottomBar(5) + "§6 Baltop " + getBottomBar(5));
            for (int i = 1; i <= baltop.size(); i++) {
                FactionUser user = baltop.get(i);
                sender.sendMessage(OpaleFaction.PREFIX + "§6" + i + ". §e" + user.getName() + " §7- §e" + user.getMoney() + " §6$");
            }
            sender.sendMessage(OpaleFaction.PREFIX + getBottomBar(32));
            return true;
        }
        if (args.length == 1) {
            try {
                int page = Integer.parseInt(args[0]);
                if (page < 1) {
                    sender.sendMessage(OpaleFaction.PREFIX + "§cVeuillez entrer un nombre valide.");
                    return true;
                }
                if (page > getMaxPage()) {
                    sender.sendMessage(OpaleFaction.PREFIX + "§cCette page n'existe pas.");
                    return true;
                }
                HashMap<Integer, FactionUser> baltop = getBaltop(page);
                sender.sendMessage(OpaleFaction.PREFIX + getBottomBar(5) + "§6 Baltop Page " + page + " " + getBottomBar(5));
                for (int i = 1; i <= baltop.size(); i++) {
                    FactionUser user = baltop.get(i);
                    sender.sendMessage(OpaleFaction.PREFIX + "§6" + i + ". §e" + user.getName() + " §7- §e" + user.getMoney() + " §6$");
                }
                sender.sendMessage(OpaleFaction.PREFIX + getBottomBar(32));
            } catch (NumberFormatException e) {
                sender.sendMessage(OpaleFaction.PREFIX + "§cVeuillez entrer un nombre valide.");
            }
            return true;
        }
        sender.sendMessage(OpaleFaction.PREFIX + "§cUtilisation: /baltop [page]");
        return true;
    }

    @Override
    public ArrayList<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> completions = new ArrayList<>();
        if (args.length == 1) {
            for (int i = 1; i <= getMaxPage(); i++) {
                completions.add(String.valueOf(i));
            }
        }
        return completions;
    }
}
