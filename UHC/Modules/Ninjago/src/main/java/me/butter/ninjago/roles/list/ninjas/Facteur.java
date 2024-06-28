package me.butter.ninjago.roles.list.ninjas;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class Facteur extends NinjagoRole {

    int messageCount = 0, timeToSend = 2 * 60;
    boolean canSendMessage = true;

    public Facteur() {
        super("Facteur", "/roles/ninjas/facteur");
        addPower(new LetterCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous avez speed 1 permanent. ",
                "Lorsque vous enlevez votre armure, vous obtenez invisibilité et no fall. "
        };
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(20);

        Bukkit.getScheduler().runTaskTimer(Ninjago.getInstance(), () -> {
            if(getUHCPlayer().getPlayer() == null) return;

            boolean invisible = true;
            for(ItemStack item : getUHCPlayer().getPlayer().getInventory().getArmorContents()) {
                if(item.getType() != Material.AIR) {
                    invisible = false;
                    break;
                }
            }

            if(!invisible && getUHCPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                getUHCPlayer().setNoFall(false);
                getUHCPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            }
            if(invisible && !getUHCPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                getUHCPlayer().setNoFall(true);
                getUHCPlayer().addPotionEffect(PotionEffectType.INVISIBILITY, -1, 1);
            }
        }, 0, 10);
    }

    @Override
    public void onDay() {
        messageCount = 0;
        canSendMessage = true;
    }

    @Override
    public void onNight() {
        canSendMessage = false;
    }

    private class LetterCommand extends TargetCommandPower {

        public LetterCommand() {
            super("Lettre", "lettre", 0, -1);
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public String[] getDescription() {
            return new String[]{"A chaque début d'épisode, vous avez 2 minutes pour envoyer 2 messages à n'importe quel joueur de la partie"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            if(args.length < 3) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Usage: /n lettre <joueur> [message]"));
                return false;
            }

            if(messageCount >= 2) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez déjà envoyé vos deux lettres !"));
            }
            if(!canSendMessage || UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer() % UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration() > timeToSend) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez dépassé le temps imparti !"));
                return false;
            }

            List<String> message = Lists.newArrayList(args);
            message.remove(0);
            message.remove(0);

            target.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Facteur: " + Joiner.on(" ").join(message)));
            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez envoyé '" + Joiner.on(" ").join(message) + "' to " + target.getName()));

            messageCount ++;
            return false;
        }
    }
}
