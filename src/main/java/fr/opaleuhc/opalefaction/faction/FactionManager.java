package fr.opaleuhc.opalefaction.faction;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.faction.claims.ClaimManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class FactionManager {

    public static FactionManager INSTANCE;
    public ArrayList<UUID> disbanding = new ArrayList<>();
    public ArrayList<UUID> leaving = new ArrayList<>();
    public ArrayList<UUID> unclaimall = new ArrayList<>();
    private OpaleFaction plugin;
    private ArrayList<Faction> factions = new ArrayList<>();

    public FactionManager(OpaleFaction plugin) {
        INSTANCE = this;
        this.plugin = plugin;

        new ClaimManager(this.plugin);

        invitationExpireCheck();
    }

    public ArrayList<Faction> getFactions() {
        return factions;
    }

    public boolean createFaction(Player p, String name) {
        if (getFactionByName(name) != null) {
            p.sendMessage(OpaleFaction.PREFIX + "§cUne faction avec ce nom existe déjà !");
            return false;
        }
        if (isInAFaction(p.getUniqueId())) {
            p.sendMessage(OpaleFaction.PREFIX + "§cVous êtes déjà dans une faction !");
            return false;
        }
        Faction faction = new Faction(UUID.randomUUID(), name, "", new HashMap<>(), new HashMap<>(), new ArrayList<>(), System.currentTimeMillis(), new HashMap<>(),
                new HashMap<>(), null);
        faction.getMembers().put(p.getUniqueId(), FactionRank.CHEF);
        factions.add(faction);
        return true;
    }

    public Faction getFactionByUUID(UUID uuid) {
        for (Faction faction : factions) {
            if (faction.getUuid().equals(uuid)) {
                return faction;
            }
        }
        return null;
    }

    public boolean disbandFaction(Faction faction) {
        if (factions.contains(faction)) {
            faction.sendMessageToAllMembers(OpaleFaction.PREFIX + "§cLa faction a été dissoute !");
            factions.remove(faction);
            faction = null;
            return true;
        }
        return false;
    }

    public void leaveFaction(Faction faction, Player p) {
        if (factions.contains(faction)) {
            if (faction.getMembers().containsKey(p.getUniqueId())) {
                faction.getMembers().remove(p.getUniqueId());
                faction.sendMessageToAllMembers(OpaleFaction.PREFIX + "§e" + p.getName() + " §ca quitté la faction !");
            }
        }
    }

    public void invitationExpireCheck() {
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Faction faction : factions) {
                for (UUID uuid : faction.getInvitations().keySet()) {
                    if (System.currentTimeMillis() - faction.getInvitations().get(uuid) >= 1000 * 30) {
                        faction.getInvitations().remove(uuid);
                        faction.sendMessageToAllMembers(OpaleFaction.PREFIX + "§e" + faction.getInvitationsName().getOrDefault(uuid, "?") + " §cn'a pas répondu à l'invitation !");
                    }
                }
            }
        }, 0, 20L * 2);
    }

    public Faction getFactionByName(String name) {
        for (Faction faction : factions) {
            if (faction.getName().equalsIgnoreCase(name)) {
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
