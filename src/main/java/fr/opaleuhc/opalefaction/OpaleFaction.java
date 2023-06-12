package fr.opaleuhc.opalefaction;

import fr.mrmicky.fastboard.FastBoard;
import fr.mrmicky.fastinv.FastInvManager;
import fr.opaleuhc.opalefaction.dependencies.fawe.FAWEAPI;
import fr.opaleuhc.opalefaction.dependencies.holograms.DecentHologramAPI;
import fr.opaleuhc.opalefaction.dependencies.luckperms.LuckPermsAPI;
import fr.opaleuhc.opalefaction.dependencies.npc.NpcAPI;
import fr.opaleuhc.opalefaction.faction.FactionCmd;
import fr.opaleuhc.opalefaction.faction.FactionManager;
import fr.opaleuhc.opalefaction.listeners.ConnectionListener;
import fr.opaleuhc.opalefaction.scoreboard.ScoreBoardManager;
import fr.opaleuhc.opalefaction.shop.ShopGUICmd;
import fr.opaleuhc.opalefaction.shop.ShopGUIManager;
import fr.opaleuhc.opalefaction.tab.TABManager;
import fr.opaleuhc.opalefaction.teleportation.TeleportationManager;
import fr.opaleuhc.opalefaction.teleportation.spawn.SpawnCmd;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class OpaleFaction extends JavaPlugin {

    public static final String PREFIX = "§r§l>> §r";
    public static OpaleFaction INSTANCE;
    public static String VERSION;

    @Override
    public void onEnable() {
        INSTANCE = this;

        VERSION = getDescription().getVersion();

        getLogger().info("Registering dependencies...");
        new LuckPermsAPI();
        new DecentHologramAPI(this);
        new NpcAPI(this);
        new FAWEAPI(this);

        getLogger().info("Registering managers...");
        new FactionManager(this);
        new ScoreBoardManager(this);
        new TABManager(this);
        new TeleportationManager(this);
        new ShopGUIManager(this);

        getLogger().info("Registering FastInv...");
        FastInvManager.register(this);

        getLogger().info("Registering CPM...");

        getLogger().info("Registering listeners...");
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);

        getLogger().info("Registering commands...");
        getCommand("f").setExecutor(new FactionCmd());
        getCommand("spawn").setExecutor(new SpawnCmd());
        getCommand("shop").setExecutor(new ShopGUICmd());

        getLogger().info("Starting tasks...");
        for (Player p : Bukkit.getOnlinePlayers()) {
            ScoreBoardManager.INSTANCE.boards.put(p.getUniqueId(), new FastBoard(p));
            ScoreBoardManager.INSTANCE.setBoardNumber(p.getUniqueId(), 1);
        }

        getLogger().info("Plugin enabled !");
    }

    @Override
    public void onDisable() {
        DecentHologramAPI.INSTANCE.disable();
    }
}
