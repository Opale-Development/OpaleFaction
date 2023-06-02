package fr.opaleuhc.opalefaction.dependencies.npc;

import fr.opaleuhc.opalefaction.OpaleFaction;

public class NpcAPI {

    public static NpcAPI INSTANCE;
    private OpaleFaction opaleFaction;

    public NpcAPI(OpaleFaction opaleFaction) {
        INSTANCE = this;
        this.opaleFaction = opaleFaction;

        opaleFaction.getLogger().info("NpcAPI registered !");
    }
}
