package me.butter.impl.world;

import me.butter.api.UHCAPI;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.api.world.OrePopulator;
import me.butter.api.world.WorldHandler;
import me.butter.impl.UHCBase;
import me.butter.impl.listeners.WorldListener;
import me.butter.impl.world.modify.BiomeSwapper;
import me.butter.impl.world.modify.WorldGenCavesPatched;
import me.butter.impl.world.pregen.PregenTask;
import org.apache.commons.io.FileUtils;
import org.bukkit.*;

import java.io.File;

public class WorldHandlerImpl implements WorldHandler {

    private final String worldName;
    private World world;
    private OrePopulator orePopulator;

    private PregenTask pregenTask;

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
        UHCAPI.getInstance().getGameHandler().getWorldConfig().setWorldGenerated(false);

        deleteWorld(worldName);

        Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Le monde est en cours de création..."));
        Bukkit.getScheduler().runTaskLater(UHCBase.getInstance(), () -> {
            WorldCreator creator = new WorldCreator(worldName);

            this.setWorld(creator.createWorld());
            this.getWorld().getPopulators().add(this.orePopulator);

            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Le monde a bien été créé avec succès !"));

            try {
                WorldGenCavesPatched.load(this.getWorld(), 2);
            }catch (Exception e){
                e.printStackTrace();
            }

            this.world.getWorldBorder().setCenter(new Location(this.world, 0.0D, 0.0D, 0.0D));
            this.world.getWorldBorder().setSize(UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize());
            this.world.getWorldBorder().setDamageAmount(0.0D);

            UHCAPI.getInstance().getGameHandler().getWorldConfig().setWorldGenerated(true);
        }, 20);
    }

    @Override
    public void deleteWorld(String worldName) {
        if(Bukkit.getWorlds().contains(Bukkit.getWorld(worldName))) {
            Bukkit.getWorld(worldName).getPlayers().forEach(player -> player.teleport(new Location(Bukkit.getWorld("world"), 0, 205, 0)));
        }

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

        getWorld().getPlayers().forEach(player -> player.teleport(new Location(Bukkit.getWorld("world"), 0, 205, 0)));

        Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Le monde est en cours de prégénération..."));
        UHCAPI.getInstance().getGameHandler().getWorldConfig().setPregenDone(false);

        if(pregenTask != null) {
            pregenTask.cancel();
            pregenTask = null;
        }

        UHCAPI.getInstance().getStructureHandler().loadStructures();

        pregenTask = new PregenTask(getWorld(), (UHCAPI.getInstance().getGameHandler().getWorldConfig().getStartingBorderSize() + 100));
        pregenTask.runTaskTimer(UHCBase.getInstance(), 0, 20);
    }

    @Override
    public OrePopulator getOrePopulator() {
        return this.orePopulator;
    }
}
