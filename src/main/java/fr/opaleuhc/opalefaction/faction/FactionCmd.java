package fr.opaleuhc.opalefaction.faction;

import fr.opaleuhc.opalefaction.OpaleFaction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FactionCmd implements CommandExecutor {

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
        if (args[0].equalsIgnoreCase("create")) {
            if (args.length != 2) {
                sender.sendMessage(OpaleFaction.PREFIX + "§c/f create <nom>");
                return true;
            }
            if (FactionManager.INSTANCE.createFaction(p, args[1])) {
                sender.sendMessage(OpaleFaction.PREFIX + "§aVous avez créé la faction §e" + args[1] + "§a !");
            }
            return true;
        }
        return true;
    }
}
