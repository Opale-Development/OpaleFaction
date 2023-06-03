package fr.opaleuhc.opalefaction.dependencies.fawe;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.*;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.pattern.BlockPattern;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockTypes;
import fr.opaleuhc.opalefaction.OpaleFaction;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class FAWEAPI {

    public static FAWEAPI INSTANCE;
    private OpaleFaction plugin;

    public FAWEAPI(OpaleFaction plugin) {
        INSTANCE = this;
        this.plugin = plugin;
    }

    public void pasteSchem(File file, Location pos) {
        try {
            com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(pos.getWorld());
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            ClipboardReader reader = Objects.requireNonNull(format).getReader(new FileInputStream(file));

            Clipboard clipboard = reader.read();
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld,
                    -1);

            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(BlockVector3.at(pos.getX(), pos.getY(), pos.getZ())).ignoreAirBlocks(true).copyBiomes(true).build();

            Operations.complete(operation);
            editSession.flushSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cutChunk(Location loc) {
        try {
            com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(loc.getWorld());

            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);

            int maxHeight = loc.getWorld().getMaxHeight();
            int minHeight = loc.getWorld().getMinHeight();

            Location pos1 = loc.getChunk().getBlock(0, minHeight, 0).getLocation();
            Location pos2 = loc.getChunk().getBlock(15, maxHeight, 15).getLocation();
            Region region = new CuboidRegion(BlockVector3.at(pos1.getX(), pos1.getY(), pos1.getZ()),
                    BlockVector3.at(pos2.getX(), pos2.getY(), pos2.getZ()));

            Pattern pattern = new BlockPattern(BlockTypes.AIR.getDefaultState());

            editSession.setBlocks(region, pattern);

            Operation operation = new ClipboardHolder(Clipboard.create(region, loc.getWorld().getUID())).createPaste(editSession)
                    .to(BlockVector3.at(pos1.getX(), pos1.getY(), pos1.getZ())).ignoreAirBlocks(true).copyBiomes(false).build();

            try {
                Operations.complete(operation);
                editSession.flushSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pasteSchemWithoutLockingThread(File file, Location pos) {
        CompletableFuture.runAsync(() -> {
            pasteSchem(file, pos);
        });

    }

    public void pasteSchemLockThread(File file, Location pos) {
        CompletableFuture.runAsync(() -> {
            pasteSchem(file, pos);
        }).join();
    }

    /*IslandManager.instance.saveSchem("aaaa", island.getCenter().clone().add(50, -15, 50),
                        island.getCenter().clone().add(-50, 40, -50), island.getCenter().getWorld(), island.getCenter().clone());*/

    public void saveSchem(String filename, Location loc1, Location loc2, World world, Location center) {
        try {
            com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(world);
            BlockVector3 pos1 = BlockVector3.at(loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ());
            BlockVector3 pos2 = BlockVector3.at(loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
            Region cReg = new CuboidRegion(weWorld, pos2, pos1);
            File file = new File(plugin.getDataFolder(), filename + ".schem");
            Clipboard clipboard = Clipboard.create(cReg);
            clipboard.setOrigin(BlockVector3.at(center.getX(), center.getY(), center.getZ()));

            try (ClipboardWriter writer = BuiltInClipboardFormat.FAST.getWriter(new FileOutputStream(file))) {
                writer.write(clipboard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
