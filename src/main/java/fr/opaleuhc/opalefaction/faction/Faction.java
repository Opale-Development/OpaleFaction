package fr.opaleuhc.opalefaction.faction;

import fr.opaleuhc.opalefaction.utils.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
    private Location home;

    public Faction(UUID uuid, String name, String description, HashMap<UUID, FactionRank> members, ArrayList<Long> claims, long creationDate,
                   HashMap<UUID, Long> allies, HashMap<UUID, Long> enemies, Location home) {
        this.uuid = uuid;
        this.name = name;
        this.description = description;
        this.members = members;
        this.claims = claims;
        this.creationDate = creationDate;
        this.allies = allies;
        this.enemies = enemies;
        this.home = home;
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

    public void sendMessageToAllMembers(String message) {
        for (UUID uuid : members.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.sendMessage(message);
            }
        }
    }

    public String getFactionInfo() {
        return "§6--- §eInformations de la faction §7" + name + "§6 ---\n" +
                "§eDescription : §f" + description + "\n" +
                "§eMembres : §f" + getOnlineMembers() + "/" + members.size() + "\n" +
                "§eClaims : §f" + claims.size() + "\n" +
                "§eAlliés : §f" + allies.size() + "\n" +
                "§eEnnemis : §f" + enemies.size() + "\n" +
                "§eDate de création : §f" + DateUtils.getDateTimeFormatted(creationDate);
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

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }
}
