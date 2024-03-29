package fr.opaleuhc.opalefaction.dependencies.holograms;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import fr.opaleuhc.opalefaction.OpaleFaction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DecentHologramAPI {

    public static DecentHologramAPI INSTANCE;
    public ArrayList<BindedHologram> bindedHolograms = new ArrayList<>();
    private OpaleFaction plugin;

    public DecentHologramAPI(OpaleFaction plugin) {
        INSTANCE = this;
        this.plugin = plugin;

        if (Bukkit.getPluginManager().getPlugin("DecentHolograms") == null) {
            OpaleFaction.INSTANCE.getLogger().warning("DecentHolograms not found, not loading DecentHologramsAPI.");
            return;
        }

        bindClock();

        System.out.println("DecentHologramsAPI loaded.");
    }

    public Hologram spawnHologram(String nameOfHolo, Location loc, List<String> lines) {
        String name = nameOfHolo;
        if (name == null) name = UUID.randomUUID().toString();
        return DHAPI.createHologram(name, loc, lines);
    }

    public void spawnHologramBinded(Location loc, List<String> lines, Entity entity) {
        CompletableFuture.runAsync(() -> {
            String name = "binded" + UUID.randomUUID();
            Hologram hologram = DHAPI.createHologram(name, loc, lines);
            bindHologramToEntity(hologram, entity, loc, -1, false);
        });
    }

    public void bindHologramToEntity(Hologram hologram, Entity entity, Location loc, long expiration, boolean shouldRemoveOnDeath) {
        bindedHolograms.add(new BindedHologram(hologram, entity, loc, expiration, shouldRemoveOnDeath));
    }

    public void removeHologram(Hologram hologram) {
        CompletableFuture.runAsync(() -> {
            DHAPI.removeHologram(hologram.getName());
            hologram.unregister();
        });
    }

    public void bindClock() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            try {
                ArrayList<BindedHologram> newBindedHolograms = new ArrayList<>();
                for (BindedHologram bh : bindedHolograms) {
                    if (bh.getExpiration() > 0 && bh.getExpiration() < System.currentTimeMillis()) {
                        removeHologram(bh.getHologram());
                        continue;
                    }
                    if (bh.shouldRemoveOnDeath() && bh.getEntity().isDead()) {
                        removeHologram(bh.getHologram());
                        continue;
                    }
                    newBindedHolograms.add(bh);
                    if (bh.getDefaultDistance() > bh.getEntity().getLocation().distance(bh.getLoc())) continue;
                    DHAPI.moveHologram(bh.getHologram(), bh.getEntity().getLocation().clone().subtract(bh.getLoc()));
                }
                bindedHolograms = newBindedHolograms;
            } catch (Exception ignored) {
            }
        }, 0, 2);
    }

    public void disable() {
        for (BindedHologram bindedHologram : bindedHolograms) {
            removeHologram(bindedHologram.getHologram());
        }
    }

}
