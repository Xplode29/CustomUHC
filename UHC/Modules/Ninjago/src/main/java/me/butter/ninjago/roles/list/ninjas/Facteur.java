package me.butter.ninjago.roles.list.ninjas;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.power.TargetCommandPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.List;

public class Facteur extends NinjagoRole {
    static int messageCount = 0;

    public Facteur() {
        super("Facteur", "soon", Arrays.asList(
                new LetterCommand()
        ));
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
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getWhoClicked());

        if(!uhcPlayer.equals(getUHCPlayer())) return;

        if (event.getClickedInventory().getType() == InventoryType.PLAYER)
        {
            if (event.getSlotType() == InventoryType.SlotType.ARMOR)
            {
                for(ItemStack item : getUHCPlayer().getPlayer().getInventory().getArmorContents()) {
                    if(item.getType() != Material.AIR) {
                        getUHCPlayer().setNoFall(false);
                        return;
                    }
                }

                getUHCPlayer().setNoFall(true);
                getUHCPlayer().getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                getUHCPlayer().getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 2 * 20, 0, false, false));
            }
        }
    }

    @Override
    public void onDay() {
        messageCount = 0;
    }

    private static class LetterCommand extends TargetCommandPower {

        boolean canSendMessage = false;

        public LetterCommand() {
            super("Lettre", "lettre", 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{"A chaque début d'épisode, vous avez 5 minutes pour envoyer 2 messages à n'importe quel joueur de la partie"};
        }

        @Override
        public boolean onEnable(UHCPlayer player, UHCPlayer target, String[] args) {
            canSendMessage = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer() % UHCAPI.getInstance().getGameHandler().getGameConfig().getEpisodeDuration() <= 5 * 60;
            if(canSendMessage && messageCount < 2) {
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
