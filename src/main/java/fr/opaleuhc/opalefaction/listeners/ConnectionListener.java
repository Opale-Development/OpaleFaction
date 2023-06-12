package fr.opaleuhc.opalefaction.listeners;

import fr.mrmicky.fastboard.FastBoard;
import fr.opaleuhc.opalefaction.dependencies.luckperms.LuckPermsAPI;
import fr.opaleuhc.opalefaction.faction.Faction;
import fr.opaleuhc.opalefaction.faction.FactionManager;
import fr.opaleuhc.opalefaction.scoreboard.ScoreBoardManager;
import fr.opaleuhc.opalefaction.tab.TABManager;
import fr.opaleuhc.opalefaction.users.FactionUser;
import fr.opaleuhc.opalefaction.users.FactionUserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        FactionUserManager.INSTANCE.checkForFactionUser(p.getUniqueId(), p.getName());

        ScoreBoardManager.INSTANCE.boards.put(p.getUniqueId(), new FastBoard(p));
        ScoreBoardManager.INSTANCE.setBoardNumber(p.getUniqueId(), 1);
        TABManager.INSTANCE.tabPlayer(p);

        Faction faction = FactionManager.INSTANCE.getFactionOf(p.getUniqueId());
        String factionName = faction == null ? "N/A" : faction.getNameAppliedForPlayer(p.getUniqueId());
        e.setJoinMessage("§7[§a+§7] §f" + LuckPermsAPI.INSTANCE.getPrefix(p.getUniqueId()) + " §7" + factionName + " " + p.getName() + " " + LuckPermsAPI.INSTANCE.getSuffix(p.getUniqueId()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        FactionUser factionUser = FactionUserManager.INSTANCE.getFactionUser(p.getUniqueId());
        if (factionUser != null) FactionUserManager.INSTANCE.updateFactionUser(factionUser);

        ScoreBoardManager.INSTANCE.boards.remove(p.getUniqueId());

        Faction faction = FactionManager.INSTANCE.getFactionOf(p.getUniqueId());
        String factionName = faction == null ? "N/A" : faction.getNameAppliedForPlayer(p.getUniqueId());
        e.setQuitMessage("§7[§c-§7] §f" + LuckPermsAPI.INSTANCE.getPrefix(p.getUniqueId()) + " §7" + factionName + " " + p.getName() + " " + LuckPermsAPI.INSTANCE.getSuffix(p.getUniqueId()));
    }

}
