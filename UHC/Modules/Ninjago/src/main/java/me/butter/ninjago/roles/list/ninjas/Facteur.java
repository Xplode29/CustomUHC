package me.butter.ninjago.roles.list.ninjas;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.List;

public class Facteur extends NinjagoRole {

    LetterCommand letterCommand;

    boolean invisible = false;

    public Facteur() {
        super("Facteur", "/roles/ninjas/facteur", Collections.singletonList(new LetterCommand()));
        for(Power power : getPowers()) {
            if(power instanceof LetterCommand) {
                letterCommand = (LetterCommand) power;
                break;
            }
        }
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
            for(ItemStack item : getUHCPlayer().getPlayer().getInventory().getArmorContents()) {
                if(item.getType() != Material.AIR) {
                    if(invisible) {
                        getUHCPlayer().setNoFall(false);
                        getUHCPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                        invisible = false;
                    }
                    return;
                }
            }
            if(!invisible) {
                getUHCPlayer().setNoFall(true);
                getUHCPlayer().addPotionEffect(PotionEffectType.INVISIBILITY, -1, 1);
                invisible = true;
            }
        }, 0, 10);
    }

    @Override
    public void onDay() {
        letterCommand.messageCount = 0;
        letterCommand.canSendMessage = true;
    }

    @Override
    public void onNight() {
        letterCommand.canSendMessage = false;
    }

    private static class LetterCommand extends TargetCommandPower {

        int messageCount = 0, timeToSend = 2 * 60;
        boolean canSendMessage = true;

        public LetterCommand() {
            super("Lettre", "lettre", 0, -1);
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public String[] getDescription() {
            return new String[]{"A chaque début d'épisode, vous avez 5 minutes pour envoyer 2 messages à n'importe quel joueur de la partie"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            if(canSendMessage && messageCount < 2 && UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer() % UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration() <= timeToSend) {
                if(args.length < 3) {
                    player.sendMessage(ChatUtils.ERROR.getMessage("Usage: /ni lettre <joueur> [message]"));
                    return false;
                }

                List<String> message = Lists.newArrayList(args);
                message.remove(0);
                message.remove(0);

                target.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Facteur: " + Joiner.on(" ").join(message)));

                messageCount ++;
            }
            else if(!canSendMessage) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez dépassé le temps imparti !"));
            }
            else {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez déjà envoyé vos deux lettres !"));
            }
            return false;
        }
    }
}
