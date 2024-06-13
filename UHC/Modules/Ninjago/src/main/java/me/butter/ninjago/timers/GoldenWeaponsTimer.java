package me.butter.ninjago.timers;

import me.butter.api.UHCAPI;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.timer.AbstractTimer;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.structures.StructChestHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class GoldenWeaponsTimer extends AbstractTimer {

    public GoldenWeaponsTimer() {
        super("Apparition des Armes d'or", Material.GOLD_SWORD, 40 * 60);
    }

    @Override
    public void onTimerDone() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if(Ninjago.getInstance().getGoldenWeaponManager().getChests().size() >= Ninjago.getInstance().getGoldenWeaponManager().getWeapons().size()) {
                    cancel();
                    return;
                }

                int maxRadius = Math.min((int) (UHCAPI.getInstance().getWorldHandler().getWorld().getWorldBorder().getSize() / 2) - 50, 450);

                int radius;
                if(maxRadius < 250) radius = new Random().nextInt((int) (UHCAPI.getInstance().getWorldHandler().getWorld().getWorldBorder().getSize() / 2));
                else radius = new Random().nextInt(maxRadius - 250) + 250;

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

                Bukkit.broadcastMessage(ChatUtils.SEPARATOR.prefix);
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatUtils.GLOBAL_INFO.getMessage("Une arme d'or est apparue entre " + (maxRadius > 250 ? "250" : "0") + " et " + maxRadius + " blocks du centre !"));
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatUtils.SEPARATOR.prefix);
            }
        }.runTaskTimer(Ninjago.getInstance(), 0, 150 * 20);
    }

    @Override
    public void onUpdate(int timer) {

    }
}
