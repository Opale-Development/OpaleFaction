package fr.opaleuhc.opalefaction.listeners;

import fr.mrmicky.fastboard.FastBoard;
import fr.opaleuhc.opalefaction.dependencies.luckperms.LuckPermsAPI;
import fr.opaleuhc.opalefaction.faction.Faction;
import fr.opaleuhc.opalefaction.faction.FactionManager;
import fr.opaleuhc.opalefaction.scoreboard.ScoreBoardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ScoreBoardManager.INSTANCE.boards.put(p.getUniqueId(), new FastBoard(p));
        ScoreBoardManager.INSTANCE.setBoardNumber(p.getUniqueId(), 1);

        Faction faction = FactionManager.INSTANCE.getFactionOf(p.getUniqueId());
        String factionName = faction == null ? "N/A" : faction.getName();
        e.setJoinMessage("§7[§a+§7] §6" + factionName + " " + LuckPermsAPI.INSTANCE.getPrefix(p.getUniqueId()) + " " + p.getName() + " " + LuckPermsAPI.INSTANCE.getSuffix(p.getUniqueId()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        ScoreBoardManager.INSTANCE.boards.remove(p.getUniqueId());

        Faction faction = FactionManager.INSTANCE.getFactionOf(p.getUniqueId());
        String factionName = faction == null ? "N/A" : faction.getName();
        e.setQuitMessage("§7[§c-§7] §6" + factionName + " " + LuckPermsAPI.INSTANCE.getPrefix(p.getUniqueId()) + " " + p.getName() + " " + LuckPermsAPI.INSTANCE.getSuffix(p.getUniqueId()));
    }

}
