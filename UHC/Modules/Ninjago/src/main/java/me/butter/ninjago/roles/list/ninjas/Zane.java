package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zane extends NinjagoRole {

    static boolean gelZonePlaced;
    static Location gelZoneCoords;

    boolean inZone;

    public Zane() {
        super("Zane", "doc", Arrays.asList(
                new FaconCommand(),
                new FreezePower(),
                new SpinjitzuPower()
        ));
    }

    @Override
    public String[] getDescription() {
        return new String[]{"Vous n'avez pas d'effets particuliers"};
    }

    @Override
    public boolean isNinja() {
        return true;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        UHCPlayer damager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(!damager.equals(getUHCPlayer())) return;

        if(gelZonePlaced && gelZoneCoords != null) {
            Location playerLoc = event.getPlayer().getLocation();

            if (Math.abs(playerLoc.getX() - gelZoneCoords.getX()) <= 15 && Math.abs(playerLoc.getZ() - gelZoneCoords.getZ()) <= 15 && !inZone) {
                getUHCPlayer().addStrength(20);
                inZone = true;
            }
            if (Math.abs(playerLoc.getX() - gelZoneCoords.getX()) > 15 && Math.abs(playerLoc.getZ() - gelZoneCoords.getZ()) > 15 && inZone) {
                getUHCPlayer().removeStrength(20);
                inZone = false;
            }
        }
    }

    private static class FaconCommand extends TargetCommandPower {
        public FaconCommand() {
            super("Faucon", "faucon", 5 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"A l'activation, vous obtenez le nombre de pommes en or ainsi que le nombre de kills du joueur ciblé"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            int gapples = 0;
            for(ItemStack item : target.getInventory()) {
                if(item != null) {
                    if(item.getType() == Material.GOLDEN_APPLE) {
                        gapples += item.getAmount();
                    }
                }
            }

            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                    "Le joueur " + target.getName() + " a " + gapples + " pommes d'or et a tué " + target.getKilledPlayers().size() + " joueurs."
            ));

            return true;
        }
    }

    private static class FreezePower extends RightClickItemPower {
        final int zoneTimer = 2 * 60;
        FreezeZoneCooldown freezeZoneRunnable;
        List<Block> icedBlocks;

        public FreezePower() {
            super(ChatColor.BLUE + "Déluge", Material.PACKED_ICE, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"À l'activation, vous recouvrez de glace une zone de 30x30 où vous possédez Force 1"};
        }

        @Override
        public boolean onEnable(UHCPlayer player) {
            if(gelZonePlaced) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez déjà placé votre zone !"));
                return false;
            }

            if(freezeZoneRunnable == null) {
                freezeZoneRunnable = new FreezeZoneCooldown();
            }
            else {
                freezeZoneRunnable.reset();
            }

            gelZoneCoords = player.getLocation();

            icedBlocks = new ArrayList<>();
            for(int x = -14; x <= 14; x++) {
                for(int z = -14; z <= 14; z++) {
                    icedBlocks.add(gelZoneCoords.getWorld().getHighestBlockAt((int) (gelZoneCoords.getX() + x), (int) (gelZoneCoords.getZ() + z)).getLocation().add(0, -1, 0).getBlock());
                    gelZoneCoords.getWorld().getHighestBlockAt((int) (gelZoneCoords.getX() + x), (int) (gelZoneCoords.getZ() + z)).getLocation().add(0, -1, 0).getBlock().setType(Material.PACKED_ICE);
                }
            }
            gelZonePlaced = true;

            return true;
        }

        private class FreezeZoneCooldown extends BukkitRunnable {
            int timer;
            boolean isRunning;

            public FreezeZoneCooldown() {
                timer = 0;
                isRunning = true;
                this.runTaskTimer(Ninjago.getInstance(), 0, 20);
            }

            @Override
            public void run() {
                if(isRunning) {
                    if(timer > zoneTimer) {
                        for(Block block : icedBlocks) {
                            if(
                                    block.getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR ||
                                            block.getLocation().add(0, -1, 0).getBlock().getType() == Material.LEAVES ||
                                            block.getLocation().add(0, -1, 0).getBlock().getType() == Material.LEAVES_2 ||
                                            block.getLocation().add(0, -1, 0).getBlock().getType() == Material.LOG ||
                                            block.getLocation().add(0, -1, 0).getBlock().getType() == Material.LOG_2
                            ) {
                                block.setType(Material.LEAVES);
                            }
                            else {
                                block.setType(Material.GRASS);
                            }
                        }
                        isRunning = false;
                        gelZonePlaced = false;
                    }
                    timer++;
                }
            }

            public void reset() {
                this.timer = 0;
                this.isRunning = true;
            }
        }
    }

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.WHITE + "Spinjitzu", Material.NETHER_STAR, 5 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {"À l'activation, repousse de 5 blocks tous les joueurs dans un rayon de 4 blocks"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            List<Entity> nearbyEntities = player.getPlayer().getNearbyEntities(4, 2, 4);
            Location center = player.getPlayer().getLocation();
            for(Entity entity : nearbyEntities) {
                double angle = Math.atan2(entity.getLocation().getZ() - center.getZ(), entity.getLocation().getX() - center.getX());
                Vector newVelocity = new Vector(
                        1.5 * Math.cos(angle),
                        0.5, //* Math.signum(entity.getLocation().getY() - center.getY()),
                        1.5 * Math.sin(angle)
                );
                entity.setVelocity(newVelocity);
            }

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.WHITE);

            return true;
        }
    }
}
