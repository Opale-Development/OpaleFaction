package fr.opaleuhc.opalefaction.users;

import java.util.UUID;

public class FactionUser {

    private final UUID uuid;
    private final String name;
    private int kills;
    private int deaths;
    private double money;
    //Voir CDC pour les autres choses qui ne sont pas encore implémentées

    public FactionUser(UUID uuid, String name, int kills, int deaths, double money) {
        this.uuid = uuid;
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.money = money;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double amount) {
        if (amount < 0) amount = 0;
        money = amount;
        FactionUserManager.INSTANCE.updateFactionUser(this);
    }

    public void incrementKills() {
        kills++;
        FactionUserManager.INSTANCE.updateFactionUser(this);
    }

    public void incrementDeaths() {
        deaths++;
        FactionUserManager.INSTANCE.updateFactionUser(this);
    }

    public void addMoney(double amount) {
        money += amount;
        FactionUserManager.INSTANCE.updateFactionUser(this);
    }

    public void removeMoney(double amount) {
        if (!hasMoney(amount)) money = 0;
        else money -= amount;
        FactionUserManager.INSTANCE.updateFactionUser(this);
    }

    public boolean hasMoney(double amount) {
        return money >= amount;
    }

    public boolean applyNegativeTransaction(double amount) {
        if (!hasMoney(amount)) {
            return false;
        }
        money -= amount;
        FactionUserManager.INSTANCE.updateFactionUser(this);
        return true;
    }
}
