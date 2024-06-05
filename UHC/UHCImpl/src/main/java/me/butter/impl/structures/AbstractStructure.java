package me.butter.impl.structures;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import me.butter.api.structures.Structure;
import org.bukkit.World;

public class AbstractStructure implements Structure {

    String schematicName;
    Clipboard clipboard;
    boolean loaded;

    int x;
    int y;
    int z;
    World world;
    boolean spawned;

    public AbstractStructure(String fileName, int x, int y, int z) {
        this.schematicName = fileName;
        loaded = false;

        this.x = x;
        this.y = y;
        this.z = z;
        spawned = false;
    }

    @Override
    public String getSchematicName() {
        return schematicName;
    }

    @Override
    public Clipboard getClipboard() {
        return clipboard;
    }

    @Override
    public void setClipboard(Clipboard clipboard) {
        this.clipboard = clipboard;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public boolean isSpawned() {
        return spawned;
    }

    @Override
    public void setSpawned(boolean spawned) {
        this.spawned = spawned;
    }
}
