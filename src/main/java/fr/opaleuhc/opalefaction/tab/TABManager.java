package fr.opaleuhc.opalefaction.tab;

import fr.opaleuhc.opalefaction.OpaleFaction;
import fr.opaleuhc.opalefaction.dependencies.luckperms.LuckPermsAPI;
import fr.opaleuhc.opalefaction.faction.Faction;
import fr.opaleuhc.opalefaction.faction.FactionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TABManager {

    public static TABManager INSTANCE;
    private OpaleFaction plugin;

    public TABManager(OpaleFaction opaleFaction) {
        INSTANCE = this;
        this.plugin = opaleFaction;

        clock();
    }

    public void tabPlayer(Player p) {
        p.setPlayerListHeaderFooter("§3§lOpaleFaction\n§8" + OpaleFaction.VERSION + "\n\n§7En ligne : §a(co fac)§7/§6(co tt)\n",
                "\n§3play.opaleuhc.fr");
        Faction faction = FactionManager.INSTANCE.getFactionOf(p.getUniqueId());
        String factionName = faction == null ? "§7N/A" : faction.getNameAppliedForPlayer(p.getUniqueId());
        p.setPlayerListName("§7" + LuckPermsAPI.INSTANCE.getPrefix(p.getUniqueId()) + " §7" + factionName + " " + p.getName() + " §7" + LuckPermsAPI.INSTANCE.getSuffix(p.getUniqueId()));
    }

    public void clock() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                tabPlayer(p);
            }
        }, 0, 20 * 2);
    }
}
