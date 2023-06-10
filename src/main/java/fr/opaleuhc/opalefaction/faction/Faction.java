package fr.opaleuhc.opalefaction.faction;

import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Faction {

    private UUID uuid;
    private String name;
    private String description;
    private HashMap<UUID, FactionRank> members;
    private ArrayList<Long> claims;
    private long creationDate;
    private HashMap<UUID, Long> allies;
    private HashMap<UUID, Long> enemies;

    public Faction(UUID uuid, String name, String description, HashMap<UUID, FactionRank> members, ArrayList<Long> claims, long creationDate, HashMap<UUID, Long> allies, HashMap<UUID, Long> enemies) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.members = members;
        this.claims = claims;
        this.creationDate = creationDate;
        this.allies = allies;
        this.enemies = enemies;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getNameAppliedForPlayer(UUID uuid) {
        return name + FactionRank.getPublicColorFromRank(members.get(uuid));
    }

    public String getDescription() {
        return description;
    }

    public HashMap<UUID, FactionRank> getMembers() {
        return members;
    }

    public ArrayList<Long> getClaims() {
        return claims;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public HashMap<UUID, Long> getAllies() {
        return allies;
    }

    public HashMap<UUID, Long> getEnemies() {
        return enemies;
    }

    public int getOnlineMembers() {
        int i = 0;
        for (UUID uuid : members.keySet()) {
            if (Bukkit.getPlayer(uuid) != null) {
                i++;
            }
        }
        return i;
    }
}
