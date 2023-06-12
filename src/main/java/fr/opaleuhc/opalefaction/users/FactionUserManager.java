package fr.opaleuhc.opalefaction.users;

import fr.opaleuhc.opalefaction.OpaleFaction;

import java.util.ArrayList;
import java.util.UUID;

public class FactionUserManager {

    public static FactionUserManager INSTANCE;
    private final OpaleFaction plugin;

    private ArrayList<FactionUser> users = new ArrayList<>();

    public FactionUserManager(OpaleFaction plugin) {
        INSTANCE = this;
        this.plugin = plugin;
    }

    public void updateFactionUser(FactionUser factionUser) {
        //API REQUEST
    }

    public ArrayList<FactionUser> getFactionUsers() {
        return users;
    }

    public FactionUser getFactionUser(String name) {
        for (FactionUser user : users) {
            if (user.getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public void checkForFactionUser(UUID uuid, String name) {
        for (FactionUser user : users) {
            if (user.getUuid().equals(uuid)) {
                return;
            }
        }
        createFactionUser(uuid, name);
    }

    public FactionUser getFactionUser(UUID uuid) {
        for (FactionUser user : users) {
            if (user.getUuid().equals(uuid)) {
                return user;
            }
        }
        if (uuid == null) {
            //API REQUEST
            //return null;
        }
        return null;
    }

    public void createFactionUser(UUID uuid, String name) {
        users.add(new FactionUser(uuid, name, 0, 0, 200));

        //API REQUEST
    }
}
