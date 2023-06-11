package fr.opaleuhc.opalefaction.faction.claims;

import fr.opaleuhc.opalefaction.faction.Faction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClaimListener implements Listener {

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        final Faction faction = ClaimManager.INSTANCE.getFactionAt(e.getClickedBlock().getLocation());
        if (faction == null) return;

        if (!faction.getMembers().containsKey(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cVous ne pouvez pas intéragir ici !");
        }
    }

}
