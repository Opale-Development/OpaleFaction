package fr.opaleuhc.opalefaction.faction;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.faction.claims.ClaimManager;
import fr.opaleuhc.opalefaction.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FactionCmd implements CommandExecutor, TabCompleter {

    public static final String HELP_MSG = """
            §6--- §eAide /f sous commandes§6 ---
            §c- §ecreate <nom> : §7Permet de créer une faction
            §c- §einvite <joueur> : §7Permet d'inviter un joueur dans votre faction
            §c- §ekick <joueur> : §7Permet d'expulser un joueur de votre faction
            §c- §eaccept : §7Permet d'accepter une invitation
            §c- §edeny : §7Permet de refuser une invitation
            §c- §einfo : §7Permet d'afficher les informations de votre faction
            §c- §eleave : §7Permet de quitter votre faction
            §c- §eclaim : §7Permet de claim un chunk
            §c- §eunclaim : §7Permet de unclaim un chunk
            §c- §eunclaimall : §7Permet de unclaim tous les chunks de votre faction
            §c- §einvites : §7Permet d'afficher les invitations reçues""";


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (args.length == 0 || args.length > 2) {
            sender.sendMessage(OpaleFaction.PREFIX + "§cCommandes et aide : /f help");
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(HELP_MSG);
            return true;
        }
        if (!(sender instanceof Player p)) {
            sender.sendMessage(OpaleFaction.PREFIX + "§cSeul un joueur peut exécuter cette commande !");
            return true;
        }
        Faction faction = FactionManager.INSTANCE.getFactionOf(p.getUniqueId());
        if (args[0].equalsIgnoreCase("create")) {
            if (faction != null) {
                p.sendMessage(OpaleFaction.PREFIX + "§cVous êtes déjà dans une faction !");
                return true;
            }
            if (args.length == 2) {
                if (!StringUtils.isStringValid(args[1])) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cLe nom de la faction ne peut contenir que des lettres, des chiffres et des _");
                    return true;
                }
                if (args[1].length() < 3 || args[1].length() > 16) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cLe nom de la faction doit contenir entre 3 et 16 caractères");
                    return true;
                }
                if (FactionManager.INSTANCE.createFaction(p, args[1])) {
                    p.sendMessage(OpaleFaction.PREFIX + "§aVous avez créé la faction §e" + args[1] + "§a !");
                }
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f create <nom>");
            return true;
        }

        if (faction == null) {
            p.sendMessage(OpaleFaction.PREFIX + "§cVous n'êtes pas dans une faction !");
            return true;
        }

        if (args[0].equalsIgnoreCase("claim")) {
            //perm fac?
            if (args.length == 1) {
                Location loc = p.getLocation();
                if (ClaimManager.INSTANCE.claim(faction, loc, p)) {
                    p.sendMessage(OpaleFaction.PREFIX + "§aClaim effectué avec succès !");
                }
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f claim");
            return true;
        }
        if (args[0].equalsIgnoreCase("unclaim")) {
            //perm fac?
            if (args.length == 1) {
                Location loc = p.getLocation();
                if (ClaimManager.INSTANCE.unclaim(faction, loc, p)) {
                    p.sendMessage(OpaleFaction.PREFIX + "§aUnclaim effectué avec succès !");
                }
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f unclaim");
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        List<String> toReturn = new ArrayList<>();
        if (args.length == 1) {
            toReturn.add("help");
            toReturn.add("create");
            toReturn.add("invite");
            toReturn.add("kick");
            toReturn.add("accept");
            toReturn.add("deny");
            toReturn.add("info");
            toReturn.add("leave");
            toReturn.add("claim");
            toReturn.add("unclaim");
            toReturn.add("unclaimall");
            toReturn.add("invites");
            return toReturn;
        } else if (args.length == 2) {

        }
        return toReturn;
    }
}
