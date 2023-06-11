package fr.opaleuhc.opalefaction.faction;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.faction.claims.ClaimManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FactionManager {

    public static FactionManager INSTANCE;
    private OpaleFaction plugin;

    private ArrayList<Faction> factions = new ArrayList<>();

    public FactionManager(OpaleFaction plugin) {
        INSTANCE = this;
        this.plugin = plugin;

        new ClaimManager(this.plugin);
    }

    public ArrayList<Faction> getFactions() {
        return factions;
    }

    public boolean createFaction(Player p, String name) {
        if (isInAFaction(p.getUniqueId())) {
            p.sendMessage(OpaleFaction.PREFIX + "§cVous êtes déjà dans une faction !");
            return false;
        }
        Faction faction = new Faction(UUID.randomUUID(), name, "", new HashMap<>(), new ArrayList<>(), System.currentTimeMillis(), new HashMap<>(),
                new HashMap<>());
        faction.getMembers().put(p.getUniqueId(), FactionRank.CHEF);
        factions.add(faction);
        return true;
    }

    public Faction getFaction(UUID uuid) {
        for (Faction faction : factions) {
            if (faction.getUuid().equals(uuid)) {
                return faction;
            }
        }
        return null;
    }

    public Faction getFactionOf(UUID playerUUID) {
        for (Faction faction : factions) {
            if (faction.getMembers().containsKey(playerUUID)) {
                return faction;
            }
        }
        return null;
    }

    public boolean isInAFaction(UUID uuid) {
        return getFactionOf(uuid) != null;
    }
}
