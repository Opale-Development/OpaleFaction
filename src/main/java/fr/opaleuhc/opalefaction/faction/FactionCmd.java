package fr.opaleuhc.opalefaction.faction;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.faction.claims.ClaimManager;
import fr.opaleuhc.opalefaction.teleportation.TeleportationManager;
import fr.opaleuhc.opalefaction.utils.StringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FactionCmd implements CommandExecutor, TabCompleter {

    public static final String HELP_MSG = """
            §6--- §eAide /f sous commandes§6 ---
            §c- §ecreate <nom> : §7Permet de créer une faction
            §c- §einvite <joueur> : §7Permet d'inviter un joueur dans votre faction
            §c- §ekick <joueur> : §7Permet d'expulser un joueur de votre faction
            §c- §eaccept : §7Permet d'accepter une invitation
            §c- §edeny : §7Permet de refuser une invitation
            §c- §erminvite : §7Permet de retirer une invitation envoyée à un joueur
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
        if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 2) {
                Faction f = FactionManager.INSTANCE.getFactionByName(args[1]);
                if (f == null) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cCette faction n'existe pas !");
                    return true;
                }
                p.sendMessage(f.getFactionInfo());
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f info <faction>");
        }

        if (faction == null) {
            p.sendMessage(OpaleFaction.PREFIX + "§cVous n'êtes pas dans une faction !");
            return true;
        }

        if (args[0].equalsIgnoreCase("home")) {
            //perm fac?
            if (args.length == 1) {
                if (faction.getHome() == null) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cLe home de votre faction n'a pas été défini !");
                    return true;
                }
                TeleportationManager.INSTANCE.teleportToLocation(p, faction.getHome(), "f home", false);
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f home");
            return true;
        }
        if (args[0].equalsIgnoreCase("sethome")) {
            //perm fac?
            if (args.length == 1) {
                Location loc = p.getLocation();
                faction.setHome(loc);
                p.sendMessage(OpaleFaction.PREFIX + "§aVous avez défini le home de votre faction !");
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f sethome");
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
        if (args[0].equalsIgnoreCase("unclaimall")) {
            //perm fac?
            if (args.length == 1) {
                if (faction.getClaims().isEmpty()) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cVotre faction n'a pas de claims !");
                    return true;
                }
                if (!FactionManager.INSTANCE.unclaimall.contains(p.getUniqueId())) {
                    FactionManager.INSTANCE.unclaimall.add(p.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(OpaleFaction.INSTANCE, () -> {
                        if (FactionManager.INSTANCE.unclaimall.contains(p.getUniqueId())) {
                            FactionManager.INSTANCE.unclaimall.remove(p.getUniqueId());
                            p.sendMessage(OpaleFaction.PREFIX + "§cVous n'avez pas confirmé votre action à temps !");
                        }
                    }, 20 * 10);
                    p.sendMessage(OpaleFaction.PREFIX + "§aVous allez unclaim toutes les claims de votre faction ! Pour confirmer, retapez la commande dans les 10 secondes !");
                    return true;
                }
                if (ClaimManager.INSTANCE.unclaimall(faction, p)) {
                    p.sendMessage(OpaleFaction.PREFIX + "§aUnclaim effectué avec succès !");
                } else {
                    p.sendMessage(OpaleFaction.PREFIX + "§cVous devez avoir des claims pour pouvoir unclaim !");
                }
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f unclaimall");
            return true;
        }
        if (args[0].equalsIgnoreCase("invite")) {
            //perm fac?
            if (args.length == 2) {
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cCe joueur n'est pas connecté ou n'existe pas !");
                    return true;
                }
                if (target.equals(p)) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cVous ne pouvez pas vous inviter vous-même !");
                    return true;
                }
                if (faction.getMembers().containsKey(target.getUniqueId())) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cCe joueur est déjà dans votre faction !");
                    return true;
                }
                if (faction.getInvitations().containsKey(target.getUniqueId())) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cCe joueur a déjà une invitation en attente !");
                    return true;
                }
                faction.addInvitation(target.getUniqueId());
                p.sendMessage(OpaleFaction.PREFIX + "§aVous avez invité §e" + target.getName() + " §aà rejoindre votre faction !");
                Component message = Component.text(OpaleFaction.PREFIX + "§e" + p.getName() + " §avous a invité à rejoindre sa faction !");
                Component accept = Component.text("§a[ACCEPTER").clickEvent(ClickEvent.runCommand("/f accept " + faction.getName()));
                Component deny = Component.text("§c[REFUSER]").clickEvent(ClickEvent.runCommand("/f deny " + faction.getName()));
                target.sendMessage(message.append(accept).append(Component.text(" §a/ ").append(deny)));
                faction.sendMessageToAllMembers(OpaleFaction.PREFIX + "§e" + p.getName() + " §aa invité §e" + target.getName() + " §aà rejoindre la faction !");
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f invite <joueur>");
            return true;
        }
        if (args[0].equalsIgnoreCase("invites")) {
            //perm fac?
            if (args.length == 1) {
                if (faction.getInvitations().isEmpty()) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cVotre faction n'a aucune invitation en attente !");
                    return true;
                }
                p.sendMessage(OpaleFaction.PREFIX + "§aListe des invitations en attente :");
                for (UUID uuid : faction.getInvitations().keySet()) {
                    Component rm = Component.text("§a[SUPPRIMER LA DEMANDE]").clickEvent(ClickEvent.runCommand("/f rminvite " + uuid));
                    Component text = Component.text("§e- " + faction.getInvitationsName().getOrDefault(uuid, "§cInconnu"));
                    p.sendMessage(text.append(Component.text(" ").append(rm)));
                }
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f invites");
            return true;
        }
        if (args[0].equalsIgnoreCase("rminvite")) {
            //perm fac?
            if (args.length == 2) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(args[1]);
                } catch (Exception e) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cErreur interne !");
                    return true;
                }
                if (!faction.getInvitations().containsKey(uuid)) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cCe joueur n'a pas d'invitation en attente !");
                    return true;
                }
                faction.removeInvitation(uuid);
                p.sendMessage(OpaleFaction.PREFIX + "§aVous avez supprimé l'invitation de §e" + faction.getInvitationsName().getOrDefault(uuid, "§cInconnu") + " §a!");
                Player target = Bukkit.getPlayer(uuid);
                if (target != null) {
                    target.sendMessage(OpaleFaction.PREFIX + "§cVotre invitation à rejoindre la faction §e" + faction.getName() + " §ca été supprimée !");
                }
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f rminvite <joueur>");
            return true;
        }
        if (args[0].equalsIgnoreCase("kick")) {
            //perm fac?
            if (args.length == 2) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(args[1]);
                } catch (Exception e) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                    if (target.getPlayer() == null) {
                        p.sendMessage(OpaleFaction.PREFIX + "§cCe joueur n'est pas connecté ou n'existe pas !");
                        return true;
                    }
                    if (target.equals(p)) {
                        p.sendMessage(OpaleFaction.PREFIX + "§cVous ne pouvez pas vous kick vous-même !");
                        return true;
                    }
                    if (!faction.getMembers().containsKey(target.getUniqueId())) {
                        p.sendMessage(OpaleFaction.PREFIX + "§cCe joueur n'est pas dans votre faction !");
                        return true;
                    }
                    if (FactionRank.isHigher(faction.getMembers().get(p.getUniqueId()), faction.getMembers().get(target.getUniqueId()))) {
                        p.sendMessage(OpaleFaction.PREFIX + "§cVous ne pouvez pas kick ce joueur !");
                        return true;
                    }
                    faction.removeMember(target.getUniqueId());
                    p.sendMessage(OpaleFaction.PREFIX + "§aVous avez kick §e" + target.getName() + " §ade votre faction !");
                    if (target.getPlayer() != null)
                        target.getPlayer().sendMessage(OpaleFaction.PREFIX + "§cVous avez été kick de la faction §e" + faction.getName() + " §c!");
                    faction.sendMessageToAllMembers(OpaleFaction.PREFIX + "§e" + p.getName() + " §aa kick §e" + target.getName() + " §ade la faction !");
                    return true;
                }
                if (!faction.getMembers().containsKey(uuid)) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cCe joueur n'est pas dans votre faction !");
                    return true;
                }
                if (uuid.equals(p.getUniqueId())) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cVous ne pouvez pas vous kick vous-même !");
                    return true;
                }
                if (FactionRank.isHigher(faction.getMembers().get(p.getUniqueId()), faction.getMembers().get(uuid))) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cVous ne pouvez pas kick ce joueur !");
                    return true;
                }
                faction.removeMember(uuid);
                p.sendMessage(OpaleFaction.PREFIX + "§aVous avez kick §e" + faction.getMembersName().getOrDefault(uuid, "§cInconnu") + " §ade votre faction !");
                Player target = Bukkit.getPlayer(uuid);
                if (target != null)
                    target.sendMessage(OpaleFaction.PREFIX + "§cVous avez été kick de la faction §e" + faction.getName() + " §c!");
                faction.sendMessageToAllMembers(OpaleFaction.PREFIX + "§e" + p.getName() + " §aa kick §e" + faction.getMembersName().getOrDefault(uuid, "§cInconnu") + " §ade la faction !");
                return true;
            }
        }
        if (args[0].equalsIgnoreCase("leave")) {
            if (args.length == 1) {
                if (faction.getMembers().getOrDefault(p.getUniqueId(), FactionRank.RECRUE).canDelete()) {
                    p.sendMessage(OpaleFaction.PREFIX + "§cVous êtes le chef de cette faction ! Vous devez la dissoudre pour la quitter !");
                    return true;
                }
                if (!FactionManager.INSTANCE.leaving.contains(p.getUniqueId())) {
                    FactionManager.INSTANCE.leaving.add(p.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(OpaleFaction.INSTANCE, () -> {
                        if (FactionManager.INSTANCE.leaving.contains(p.getUniqueId())) {
                            FactionManager.INSTANCE.leaving.remove(p.getUniqueId());
                            p.sendMessage(OpaleFaction.PREFIX + "§cDélai de confirmation dépassé !");
                        }
                    }, 20 * 10);
                    p.sendMessage(OpaleFaction.PREFIX + "§aÊtes-vous sûr de vouloir quitter votre faction ? Tapez §e/f leave §aà nouveau pour confirmer, vous avez §e10 secondes §a!");
                    return true;
                }
                FactionManager.INSTANCE.leaveFaction(faction, p);
                p.sendMessage(OpaleFaction.PREFIX + "§aVous avez quitté votre faction !");
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f leave");
            return true;
        }
        if (args[0].equalsIgnoreCase("disband")) {
            if (!faction.getMembers().getOrDefault(p.getUniqueId(), FactionRank.RECRUE).canDelete()) {
                p.sendMessage(OpaleFaction.PREFIX + "§cVous n'êtes pas le chef de cette faction !");
                return true;
            }
            if (args.length == 1) {
                if (!FactionManager.INSTANCE.disbanding.contains(p.getUniqueId())) {
                    FactionManager.INSTANCE.disbanding.add(p.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(OpaleFaction.INSTANCE, () -> {
                        if (FactionManager.INSTANCE.disbanding.contains(p.getUniqueId())) {
                            FactionManager.INSTANCE.disbanding.remove(p.getUniqueId());
                            p.sendMessage(OpaleFaction.PREFIX + "§cVous n'avez pas confirmé la dissolution de votre faction !");
                        }
                    }, 20 * 10);
                    p.sendMessage(OpaleFaction.PREFIX + "§cMerci de confirmer la dissolution de votre faction avec §e/f disband§c une seconde fois, vous avez §e10 secondes§c.");
                    return true;
                }
                if (FactionManager.INSTANCE.disbandFaction(faction)) {
                    p.sendMessage(OpaleFaction.PREFIX + "§aVous avez dissous votre faction !");
                } else {
                    p.sendMessage(OpaleFaction.PREFIX + "§cUne erreur est survenue !");
                }
                FactionManager.INSTANCE.disbanding.remove(p.getUniqueId());
                return true;
            }
            p.sendMessage(OpaleFaction.PREFIX + "§c/f disband");
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
            toReturn.add("sethome");
            toReturn.add("home");
            toReturn.add("disband");
            return toReturn;
        } else if (args.length == 2) {

        }
        return toReturn;
    }
}
