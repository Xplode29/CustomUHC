package me.butter.impl.world;

import me.butter.api.UHCAPI;
import me.butter.api.utils.ChatUtils;
import me.butter.api.world.OrePopulator;
import me.butter.api.world.WorldHandler;
import me.butter.impl.UHCImpl;
import me.butter.impl.world.modify.BiomeSwapper;
import me.butter.impl.world.modify.WorldGenCavesPatched;
import me.butter.impl.world.modify.WorldListener;
import me.butter.impl.world.pregen.PregenTask;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

public class WorldHandlerImpl implements WorldHandler {

    private final String worldName;
    private World world;
    private OrePopulator orePopulator;

    public WorldHandlerImpl() {
        this.worldName = "arena";

        BiomeSwapper.init();
        Bukkit.getPluginManager().registerEvents(new WorldListener(), UHCAPI.getInstance());

        this.orePopulator = new OrePopulator();

        orePopulator.addRule(new OrePopulator.Rule(Material.COAL_ORE, 2, 0, 128, 17));
        orePopulator.addRule(new OrePopulator.Rule(Material.IRON_ORE, 5, 0, 64, 9));
        orePopulator.addRule(new OrePopulator.Rule(Material.GOLD_ORE, 2, 0, 32, 9));
        orePopulator.addRule(new OrePopulator.Rule(Material.REDSTONE_ORE, 3, 0, 16, 8));
        orePopulator.addRule(new OrePopulator.Rule(Material.DIAMOND_ORE, 1, 0, 16, 8));
        orePopulator.addRule(new OrePopulator.Rule(Material.LAPIS_ORE, 3, 0, 32, 7));

        this.createWorld(this.worldName);
    }

    @Override
    public String getWorldName() {
        return this.worldName;
    }

    @Override
    public World getWorld() {
        return this.world;
    }

    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public void createWorld(String worldName) {
        Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(
                "Le monde arena est en cours de création..."
        ));
        Bukkit.getScheduler().runTaskLater(UHCImpl.getInstance(), () -> {
            deleteWorld(worldName);
            WorldCreator creator = new WorldCreator(worldName);

            this.setWorld(creator.createWorld());
            this.getWorld().getPopulators().add(this.orePopulator);

            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage(
                    "Le monde arena a bien été créé avec succès !"
            ));

            try {
                WorldGenCavesPatched.load(this.getWorld(), 2);
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 10);
    }

    @Override
    public void deleteWorld(String worldName) {
        Bukkit.unloadWorld(worldName, false);
        File worldContainer = new File(Bukkit.getWorldContainer() + "/" + worldName + "/");
        if (worldContainer.exists()) {
            try {
                FileUtils.deleteDirectory(worldContainer);
            } catch (Exception var4) {
                worldContainer.delete();
            }
        }
    }

    @Override
    public void loadWorld() {
        getWorld().setGameRuleValue("naturalRegeneration", "false");
        getWorld().setGameRuleValue("doFireTick", "false");
        getWorld().setGameRuleValue("doDaylightCycle ", "false");

        new PregenTask(getWorld(), (UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() + 100))
                .runTaskTimer(UHCImpl.getInstance(), 0, 5);
    }

    @Override
    public OrePopulator getOrePopulator() {
        return this.orePopulator;
    }
}
