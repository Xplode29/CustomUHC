package me.butter.impl.structures;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.butter.api.structures.Structure;
import me.butter.api.structures.StructureHandler;
import me.butter.impl.UHCImpl;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class StructureHandlerImpl implements StructureHandler, Listener {

    List<Structure> structures;

    File schematicFolder;
    WorldEdit worldEdit;

    public StructureHandlerImpl() {
        this.structures = new ArrayList<>();

        worldEdit = WorldEdit.getInstance();

        File schematicFolder = new File(UHCImpl.getInstance().getDataFolder(), "schematics");
        if(!schematicFolder.exists()) schematicFolder.mkdirs();
        this.schematicFolder = schematicFolder;

        Bukkit.getPluginManager().registerEvents(this, UHCImpl.getInstance());
    }

    @Override
    public Structure getStructure(Class<? extends Structure> structClass, World world, double x, double y, double z) {
        return structures.stream().filter(structure -> structure.getClass() == structClass && structure.getX() == x && structure.getY() == y && structure.getZ() == z).findAny().orElse(null);
    }

    @Override
    public void addStructure(Structure structure) {
        if(structure == null) return;
        if(structures.contains(structure)) return;
        structures.add(structure);
    }

    @Override
    public List<Structure> getStructures() {
        return structures;
    }

    @Override
    public void loadStructures() {
        for(Structure structure : structures) {
            if(structure.isLoaded()) continue;
            EditSession session = worldEdit.getEditSessionFactory().getEditSession(new BukkitWorld(structure.getWorld()), -1);

            File file = new File(this.schematicFolder, structure.getSchematicName());
            if(!file.exists()) continue;

            try {
                ClipboardFormat format = ClipboardFormat.findByFile(file);
                ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()));
                Clipboard clipboard = reader.read(session.getWorld().getWorldData());
                structure.setClipboard(clipboard);

                structure.setLoaded(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void spawnStructure(Structure structure) {
        if(structure == null || !structures.contains(structure)) return;
        if(structure.isSpawned()) return;

        if(!structure.isLoaded()) {
            EditSession session = worldEdit.getEditSessionFactory().getEditSession(new BukkitWorld(structure.getWorld()), -1);

            File file = new File(this.schematicFolder, structure.getSchematicName());
            if(!file.exists()) return;

            try {
                ClipboardFormat format = ClipboardFormat.findByFile(file);
                ClipboardReader reader = format.getReader(Files.newInputStream(file.toPath()));
                Clipboard clipboard = reader.read(session.getWorld().getWorldData());
                structure.setClipboard(clipboard);

                structure.setLoaded(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try
        {
            EditSession session = worldEdit.getEditSessionFactory().getEditSession(new BukkitWorld(structure.getWorld()), -1);
            Operation operation = new ClipboardHolder(structure.getClipboard(), session.getWorld().getWorldData())
                    .createPaste(session, session.getWorld().getWorldData())
                    .to(BlockVector.toBlockPoint(structure.getX(), structure.getY(), structure.getZ()))
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);

            structure.setSpawned(true);
            structure.onSpawn();
        }
        catch (WorldEditException e)
        {
            Bukkit.broadcastMessage("Error: " + structure);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearStructure(Class<? extends Structure> structClass, World world, double x, double y, double z) {
        Structure structure = getStructure(structClass, world, x, y, z);
        if(structure == null) return;
        if(!structure.isSpawned()) return;
    }
}
