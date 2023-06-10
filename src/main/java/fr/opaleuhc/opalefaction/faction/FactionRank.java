package fr.opaleuhc.opalefaction.faction;

public enum FactionRank {

    CHEF("Chef", 0),
    CO_CHEF("Co-Chef", 1),
    MODERATEUR("Modérateur", 2),
    MEMBRE("Membre", 3),
    RECRUE("Recrue", 4);

    private final String name;
    private final int power;

    FactionRank(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public static FactionRank getRankFromPower(int power) {
        for (FactionRank rank : values()) {
            if (rank.getPower() == power) {
                return rank;
            }
        }
        return null;
    }

    public static FactionRank getRankFromName(String name) {
        for (FactionRank rank : values()) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }
        return null;
    }

    public static boolean isHigher(FactionRank rank, FactionRank rank2) {
        return rank.getPower() < rank2.getPower();
    }

    public static String getPublicColorFromRank(FactionRank fr) {
        return switch (fr) {
            case CHEF -> "§6";
            case CO_CHEF -> "§e";
            case MODERATEUR -> "§5";
            case MEMBRE -> "§f";
            default -> "§7";
        };
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }
}
