package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ParticleEffects;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.ninjago.goldenNinja.ChatEffectChooser;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

public class Lloyd extends NinjagoRole {
    ChatEffectChooser chatEffectChooser;

    public Lloyd() {
        super("Lloyd", "/roles/ninjas/lloyd");
        addPower(new SpinjitzuPower());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez §2No Fall§r ainsi que §l2 coeurs§r supplémentaires permanents.",
                "A chaque debut d'episode, vous pouvez choisir 10% d'un effet entre §9Speed§r, §cForce§r et §7Resistance§r."
        };
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().setNoFall(true);
        getUHCPlayer().addMaxHealth(4);
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        if(getUHCPlayer().getPlayerState() != PlayerState.IN_GAME) return;

        if(chatEffectChooser != null) {
            switch (chatEffectChooser.getChosen()) {
                case 0:
                    getUHCPlayer().removeSpeed(10);
                    break;
                case 1:
                    getUHCPlayer().removeStrength(10);
                    break;
                case 2:
                    getUHCPlayer().removeResi(10);
                    break;
            }
        }

        chatEffectChooser = new ChatEffectChooser(getUHCPlayer(), 10, -1);
        UHCAPI.getInstance().getClickableChatHandler().sendToPlayer(chatEffectChooser);
    }

    private class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.GREEN + "Spinjitzu", Material.NETHER_STAR, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Repousse tous les joueurs dans un rayon de 10 blocks. Vous obtenez 10% de l'effet choisi au debut d'episode pendant 2 minutes."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            Location center = player.getPlayer().getLocation();

            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
                if(uhcPlayer == null || uhcPlayer == player || uhcPlayer.getPlayer() == null) continue;

                if(uhcPlayer.isNextTo(player, 10)) {
                    double angle = Math.atan2(uhcPlayer.getLocation().getZ() - center.getZ(), uhcPlayer.getLocation().getX() - center.getX());
                    Vector newVelocity = new Vector(
                            1.5 * Math.cos(angle),
                            0.5,
                            1.5 * Math.sin(angle)
                    );
                    uhcPlayer.getPlayer().setVelocity(newVelocity);
                }
            }

            if(chatEffectChooser != null) {
                switch (chatEffectChooser.getChosen()) {
                    case 0:
                        player.addSpeed(10);
                        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.removeSpeed(10), 2 * 60 * 20);
                        break;
                    case 1:
                        player.addStrength(10);
                        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.removeStrength(10), 2 * 60 * 20);
                        break;
                    case 2:
                        player.addResi(10);
                        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.removeResi(10), 2 * 60 * 20);
                        break;
                    default:
                        player.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez pas choisi d'effet, donc vous n'obtenez pas d'effet."));
                        break;
                }
            }

            ParticleEffects.tornadoEffect(player.getPlayer(), Color.GREEN);
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous utilisez votre Spinjitzu !"));
            return true;
        }
    }
}
