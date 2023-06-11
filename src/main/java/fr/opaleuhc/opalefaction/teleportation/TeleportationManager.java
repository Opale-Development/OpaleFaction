package fr.opaleuhc.opalefaction.teleportation;

import fr.opaleuhc.opalefaction.OpaleFaction;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.atomic.AtomicInteger;

public class TeleportationManager {

    public static final Location SPAWN = new Location(Bukkit.getWorld("world"), 0, 81.5, 0, 180, 0);
    public static TeleportationManager INSTANCE;
    private final OpaleFaction plugin;

    public TeleportationManager(OpaleFaction plugin) {
        INSTANCE = this;
        this.plugin = plugin;
    }

    public void teleportToSpawn(Player p, boolean force) {
        if (force) {
            launchTeleportationToSpawn(p, SPAWN, true);
            return;
        }
        final int delay = getTeleportationDelay(p);
        if (delay == 0) {
            launchTeleportationToSpawn(p, SPAWN, false);
            return;
        }
        AtomicInteger elapsed = new AtomicInteger();
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, (task) -> {
            if (p.isOnline()) {
                p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§6Téléportation dans §e" + (delay - elapsed.get()) + " seconde" +
                        (delay - elapsed.get() > 1 ? "s" : "") + "§6..."));
                if (elapsed.get() >= delay) {
                    launchTeleportationToSpawn(p, SPAWN, false);
                    task.cancel();
                }
            } else task.cancel();
            elapsed.getAndIncrement();
        }, 0, 20L);
    }

    public void launchTeleportationToSpawn(Player p, Location to, boolean force) {
        if (force) {
            p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§6Téléportation de force au spawn..."));
            p.teleportAsync(to, PlayerTeleportEvent.TeleportCause.PLUGIN);
            p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§aTéléportation de force au spawn §a§l✓"));
            return;
        }
        p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§6Téléportation au spawn..."));
        p.teleportAsync(to, PlayerTeleportEvent.TeleportCause.PLUGIN);
        p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§aTéléportation au spawn §a§l✓"));
    }

    public void teleportToLocation(Player p, Location to, String locationName, boolean force) {
        if (force) {
            launchTeleportationTo(p, to, true);
            return;
        }
        final int delay = getTeleportationDelay(p);
        if (delay == 0) {
            launchTeleportationTo(p, to, false);
            return;
        }
        AtomicInteger elapsed = new AtomicInteger();
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, (task) -> {
            if (p.isOnline()) {
                p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§6Téléportation à §e" + locationName + "§6 dans §e" + (delay - elapsed.get()) + " seconde" +
                        (delay - elapsed.get() > 1 ? "s" : "") + "§6..."));
                if (elapsed.get() >= delay) {
                    launchTeleportationTo(p, to, false);
                    task.cancel();
                }
            } else task.cancel();
            elapsed.getAndIncrement();
        }, 0, 20L);
    }

    public void launchTeleportationTo(Player p, Location to, boolean force) {
        if (force) {
            p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§6Téléportation de force..."));
            p.teleportAsync(to, PlayerTeleportEvent.TeleportCause.PLUGIN);
            p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§aTéléportation de force §a§l✓"));
            return;
        }
        p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§6Téléportation..."));
        p.teleportAsync(to, PlayerTeleportEvent.TeleportCause.PLUGIN);
        p.sendActionBar(Component.text(OpaleFaction.PREFIX + "§aTéléportation §a§l✓"));
    }

    public int getTeleportationDelay(Player p) {
        if (p.hasPermission("opalefaction.spawn.0")) return 0;
        if (p.hasPermission("opalefaction.spawn.1")) return 1;
        if (p.hasPermission("opalefaction.spawn.2")) return 2;
        if (p.hasPermission("opalefaction.spawn.3")) return 3;
        if (p.hasPermission("opalefaction.spawn.4")) return 4;
        return 5;
    }

    public boolean isSafe(Location loc) {
        Block b = loc.getBlock();
        Block b1 = b.getRelative(0, 1, 0);
        if (b.getType() != Material.AIR) return false;
        return b1.getType() == Material.AIR;
    }

}
