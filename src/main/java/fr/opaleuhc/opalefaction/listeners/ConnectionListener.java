package fr.opaleuhc.opalefaction.listeners;

import fr.mrmicky.fastboard.FastBoard;
import fr.opaleuhc.opalefaction.scoreboard.ScoreBoardManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectionListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        ScoreBoardManager.INSTANCE.boards.put(p.getUniqueId(), new FastBoard(p));
        ScoreBoardManager.INSTANCE.setBoardNumber(p.getUniqueId(), 1);
    }

}
