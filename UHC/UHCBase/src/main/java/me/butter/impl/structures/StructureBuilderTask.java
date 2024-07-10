package me.butter.impl.structures;

import me.butter.api.UHCAPI;
import me.butter.api.structures.Structure;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class StructureBuilderTask extends BukkitRunnable {

    List<Structure> structures;

    double currentStructure;
    int maxStructures;

    public StructureBuilderTask() {
        this.structures = new ArrayList<>(UHCAPI.getInstance().getStructureHandler().getStructures());

        currentStructure = 0.0;
        maxStructures = structures.size();

        this.runTaskTimer(UHCAPI.getInstance(), 0, 20);
    }

    @Override
    public void run() {
        if(structures.isEmpty()) {
            UHCAPI.getInstance().getGameHandler().getWorldConfig().setPregenDone(true);
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Le monde a été pregen avec succès !"));
            cancel();
            return;
        }

        Structure structure = structures.get(0);
        structure.setWorld(UHCAPI.getInstance().getWorldHandler().getWorld());
        Block block = structure.getWorld().getHighestBlockAt(structure.getX(), structure.getZ());
        structure.setY(block.getY() + 1);
        UHCAPI.getInstance().getStructureHandler().spawnStructure(structure);

        if(currentStructure < maxStructures) {
            UHCAPI.getInstance().getPlayerHandler().getPlayersConnected().forEach(p -> p.sendActionBar(
                    "§8[§6§lPregen§8] §7Creation des structures : " + (int) ((currentStructure / maxStructures) * 100) + "%"
            ));
        }

        currentStructure += 1;
        structures.remove(0);
    }
}
