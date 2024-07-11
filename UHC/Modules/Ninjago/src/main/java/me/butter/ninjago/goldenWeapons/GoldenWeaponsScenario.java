package me.butter.ninjago.goldenWeapons;

import me.butter.api.UHCAPI;
import me.butter.api.menu.Menu;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.scenario.AbstractScenario;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.goldenWeapons.configMenu.GoldenWeaponConfigMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class GoldenWeaponsScenario extends AbstractScenario {

    boolean startedSpawn = false;

    int startTimer = 40 * 60;
    int timeBetweenSpawn = 5 * 60;

    public GoldenWeaponsScenario() {
        super("Apparition des Armes d'or", Material.GOLD_SWORD);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Les 4 armes d'or apparaissent",
            "a §e" + GraphicUtils.convertToAccurateTime(startTimer) + " §7de jeu dans des structures, ",
            "toutes les §e" + GraphicUtils.convertToAccurateTime(timeBetweenSpawn) + "§r."
        };
    }

    public int getStartTimer() {
        return startTimer;
    }

    public void setStartTimer(int startTimer) {
        this.startTimer = startTimer;
    }

    public int getTimeBetweenSpawn() {
        return timeBetweenSpawn;
    }

    public void setTimeBetweenSpawn(int timeBetweenSpawn) {
        this.timeBetweenSpawn = timeBetweenSpawn;
    }

    @Override
    public Menu getConfigMenu() {
        return new GoldenWeaponConfigMenu(this);
    }

    @Override
    public void onUpdate(int timer) {
        if(timer >= startTimer && !startedSpawn) {
            new SpawnRunnable();
            startedSpawn = true;
        }
    }

    private class SpawnRunnable extends BukkitRunnable {
        public SpawnRunnable() {
            this.runTaskTimer(Ninjago.getInstance(), 0, timeBetweenSpawn * 20L);
        }

        @Override
        public void run() {
            if(Ninjago.getInstance().getGoldenWeaponManager().getChests().size() >= Ninjago.getInstance().getGoldenWeaponManager().getWeapons().size()) {
                cancel();
                return;
            }

            int maxRadius = Math.min((int) (UHCAPI.getInstance().getWorldHandler().getWorld().getWorldBorder().getSize() / 2) - 10, 400);
            int minRadius = maxRadius > 250 ? 250 : 0;

            int radius = new Random().nextInt(maxRadius - minRadius) + minRadius;

            double angle = new Random().nextFloat() * 2 * Math.PI;

            int x = (int) (radius * Math.cos(angle));
            int z = (int) (radius * Math.sin(angle));

            World world = UHCAPI.getInstance().getWorldHandler().getWorld();
            if(world == null) return;

            Block block = world.getHighestBlockAt(x, z);
            int y = block.getY() + 1;

            StructChestHolder structure = new StructChestHolder(x, y, z, world);

            UHCAPI.getInstance().getStructureHandler().addStructure(structure);
            UHCAPI.getInstance().getStructureHandler().spawnStructure(structure);

            Ninjago.getInstance().getGoldenWeaponManager().addChest(structure);

            Bukkit.broadcastMessage(ChatUtils.LINE.prefix);
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Une arme d'or est apparue entre " + minRadius + " et " + maxRadius + " blocks du centre !"));
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(ChatUtils.LINE.prefix);
        }
    }
}
