package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.items.SpinjitzuPower;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Zane extends NinjagoRole {

    FreezePower freezePower;

    boolean inZone;

    public Zane() {
        super("Zane", "/roles/ninjas/zane", Arrays.asList(
                new FalconCommand(),
                new FreezePower(),
                new SpinjitzuPower(ChatColor.WHITE)
        ));
        for(Power power : getPowers()) {
            if(power instanceof FreezePower) {
                freezePower = (FreezePower) power;
                break;
            }
        }
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
        UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(!player.equals(getUHCPlayer())) return;

        if(freezePower.gelZonePlaced && freezePower.gelZoneCoords != null) {
            Location playerLoc = event.getPlayer().getLocation();

            if (Math.abs(playerLoc.getX() - freezePower.gelZoneCoords.getX()) <= 15 && Math.abs(playerLoc.getZ() - freezePower.gelZoneCoords.getZ()) <= 15 && !inZone) {
                getUHCPlayer().addStrength(20);
                inZone = true;
            }
            if (Math.abs(playerLoc.getX() - freezePower.gelZoneCoords.getX()) > 15 && Math.abs(playerLoc.getZ() - freezePower.gelZoneCoords.getZ()) > 15 && inZone) {
                getUHCPlayer().removeStrength(20);
                inZone = false;
            }
        }
    }

    private static class FalconCommand extends TargetCommandPower {
        public FalconCommand() {
            super("Faucon", "faucon", 5 * 60, 4);
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

        boolean gelZonePlaced;
        Location gelZoneCoords;

        public FreezePower() {
            super(ChatColor.BLUE + "Déluge", Material.PACKED_ICE, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"À l'activation, vous recouvrez de glace une zone de 30x30 où vous possédez Force 1"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(gelZonePlaced) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez déjà placé votre zone !"));
                return false;
            }

            if(freezeZoneRunnable != null) {
                freezeZoneRunnable.cancel();
                freezeZoneRunnable = null;
            }
            freezeZoneRunnable = new FreezeZoneCooldown();
            freezeZoneRunnable.runTaskLater(Ninjago.getInstance(), zoneTimer * 20);

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
            @Override
            public void run() {
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
                gelZonePlaced = false;
            }
        }
    }
}
