package fr.opaleuhc.opalefaction.teleportation.spawn;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.teleportation.TeleportationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if (sender.hasPermission("opalefaction.spawnadmin")) {
            if (args.length == 1) {
                Player target = OpaleFaction.INSTANCE.getServer().getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(OpaleFaction.PREFIX + "§cLe joueur n'est pas connecté !");
                    return true;
                }
                TeleportationManager.INSTANCE.teleportToSpawn(target, true);
                sender.sendMessage(OpaleFaction.PREFIX + "§aVous avez téléporté §e" + target.getName() + " §aau spawn !");
                return true;
            }
            if (args.length == 0) {
                if (!(sender instanceof Player p)) {
                    sender.sendMessage(OpaleFaction.PREFIX + "§cSeul un joueur peut exécuter cette commande !");
                    return true;
                }
                TeleportationManager.INSTANCE.teleportToSpawn(p, false);
                return true;
            }
            sender.sendMessage(OpaleFaction.PREFIX + "§cUsage /spawn [joueur]");
            return true;
        }
        if (args.length != 0) {
            sender.sendMessage(OpaleFaction.PREFIX + "§cUsage /spawn");
            return true;
        }
        if (!(sender instanceof Player p)) {
            sender.sendMessage(OpaleFaction.PREFIX + "§cSeul un joueur peut exécuter cette commande !");
            return true;
        }
        TeleportationManager.INSTANCE.teleportToSpawn(p, false);
        return true;
    }

}
