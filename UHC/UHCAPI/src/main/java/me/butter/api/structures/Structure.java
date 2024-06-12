package me.butter.api.structures;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import org.bukkit.World;

public interface Structure {

    String getSchematicName();

    Clipboard getClipboard(); void setClipboard(Clipboard clipboard);

    boolean isLoaded(); void setLoaded(boolean loaded);

    boolean isSpawnedAtStart();

    int getX(); int getY(); int getZ();
    void setY(int y);
    World getWorld();
    void setWorld(World world);
    boolean isSpawned(); void setSpawned(boolean spawned);

    void onSpawn();
}
