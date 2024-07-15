package me.butter.ninjago.goldenWeapons.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.goldenWeapons.AbstractGoldenWeapon;
import me.butter.ninjago.roles.list.ninjas.Cole;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.EntityFallingBlock;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class EarthScyte extends AbstractGoldenWeapon {

    Location center;
    List<UHCPlayer> nearPlayers;

    public EarthScyte() {
        super("Faux de terre",
                new String[] {
                        "Vous attribue 5% de résistance",
                        "(10% si vous êtes Cole ou Lloyd).",
                        "Click droit : Vous immobilisez ",
                        "tous les joueurs (vous inclus)",
                        "autour de vous pendant 10 secondes.",
                        "Cooldown : 10 min"
                },
                Material.DIAMOND_HOE,
                10 * 60
        );

        nearPlayers = new ArrayList<>();
    }

    @Override
    public void onPickup() {
        getHolder().addResi(5);
        if(getHolder().getRole() instanceof Cole || getHolder().getRole() instanceof Lloyd) getHolder().addResi(5);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ramassé la faux de terre !"));
    }

    @Override
    public void onDrop() {
        getHolder().removeResi(5);
        if(getHolder().getRole() instanceof Cole || getHolder().getRole() instanceof Lloyd) getHolder().removeResi(5);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez laché la faux de terre !"));
    }

    @Override
    public boolean onEnable() {
        nearPlayers.clear();
        for (UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(getHolder().isNextTo(player, 20)) {
                nearPlayers.add(player);
                player.setAbleToMove(false);
            }
        }
        getHolder().setAbleToMove(false);

        center = getHolder().getLocation().clone();

        for(int i = 0; i < 12; i++) {
            double x = center.getX() + Math.cos(i * Math.PI / 6) * 3;
            double y = center.getY();
            double z = center.getZ() + Math.sin(i * Math.PI / 6) * 3;

            spawnFallingBlock(new Location(center.getWorld(), x, y, z), Material.DIRT);
        }

        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous utilisez la faux de terre !"));

        new BukkitRunnable() {
            @Override
            public void run() {
                for (UHCPlayer player : nearPlayers) {
                    player.setAbleToMove(true);
                }
                getHolder().setAbleToMove(true);
            }
        }.runTaskLater(UHCAPI.getInstance(), 10 * 20);

        return true;
    }

    public void spawnFallingBlock(Location location, Material material) {
        World world = ((CraftWorld) location.getWorld()).getHandle();

        Block block = Block.getById(material.getId());

        EntityFallingBlock fallingBlock = new EntityFallingBlock(
                world,
                location.getX(),
                location.getY(),
                location.getZ(),
                block.getBlockData()
        );

        fallingBlock.ticksLived = 50;
        ((FallingBlock) fallingBlock.getBukkitEntity()).setDropItem(false); // not

        fallingBlock.getBukkitEntity().setMomentum(new Vector(
                (location.getX() - center.getX()) / 10,
                1.2,
                (location.getZ() - center.getZ()) / 10
        ));

        ((CraftWorld)location.getWorld()).getHandle().addEntity(fallingBlock);
    }

    @EventHandler
    public void onChangeBlock(EntityChangeBlockEvent event) {
        if(event.getTo().isBlock() && event.getTo() == Material.DIRT) {
            event.setCancelled(true);
        }
    }
}
