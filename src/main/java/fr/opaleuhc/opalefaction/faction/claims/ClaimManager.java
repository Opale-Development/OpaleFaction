package fr.opaleuhc.opalefaction.faction.claims;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.faction.Faction;
import fr.opaleuhc.opalefaction.faction.FactionManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ClaimManager {

    public static ClaimManager INSTANCE;
    private OpaleFaction plugin;

    public ClaimManager(OpaleFaction plugin) {
        INSTANCE = this;
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(new ClaimListener(), plugin);
    }

    public Faction getFactionAt(Location loc) {
        final long claim = loc.getChunk().getChunkKey();
        for (Faction faction : FactionManager.INSTANCE.getFactions()) {
            if (faction.getClaims().contains(claim)) {
                return faction;
            }
        }
        return null;
    }

    public boolean claim(Faction faction, Location loc, Player p) {
        final long claim = loc.getChunk().getChunkKey();
        if (faction.getClaims().contains(claim)) {
            p.sendMessage(OpaleFaction.PREFIX + "§cVous avez déjà claim ce chunk !");
            return false;
        }
        Faction factionAt = getFactionAt(loc);
        if (factionAt != null) {
            p.sendMessage(OpaleFaction.PREFIX + "§cCe chunk appartient déjà à une faction !");
            return false;
        }
        faction.getClaims().add(claim);
        return true;
    }

    public boolean unclaim(Faction faction, Location loc, Player p) {
        final long claim = loc.getChunk().getChunkKey();
        if (!faction.getClaims().contains(claim)) {
            p.sendMessage(OpaleFaction.PREFIX + "§cVous n'avez pas claim ce chunk !");
            return false;
        }
        faction.getClaims().remove(claim);
        return true;
    }
}
