package me.butter.ninjago.roles.list.solos;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.Power;
import me.butter.api.player.Potion;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.EpisodeEvent;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.roles.NinjagoRole;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Skylor extends NinjagoRole {

    AddEffectCommand command;

    public Skylor() {
        super("Skylor", "/roles/solitaires/skylor", Arrays.asList(new AddEffectCommand()));

        for(Power power : getPowers()) {
            if(power instanceof AddEffectCommand) {
                command = (AddEffectCommand) power;
            }
        }
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez Force et speed permanent",
                "A chaque début d'épisode, vous obtenez les effets et le pseudo d'un joueur aléatoire dans un rayon de 100 blocks. ",
                "A chaque kill, vous pouvez choisir 5% d'un effet de votre choix (avec maximum 40% par effet)"
        };
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(20);
        getUHCPlayer().addStrength(20);
    }

    @EventHandler
    public void onNewEpisode(EpisodeEvent event) {
        List<UHCPlayer> uhcPlayers = new ArrayList<>();

        for(Entity entity : getUHCPlayer().getPlayer().getNearbyEntities(100, 100, 100)) {
            if(entity instanceof Player) {
                UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) entity);
                if(uhcPlayer != null && uhcPlayer.getRole() != null && uhcPlayer != getUHCPlayer()){
                    uhcPlayers.add(uhcPlayer);
                }
            }
        }

        if(uhcPlayers.isEmpty()) {
            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Il n'y a personne autour de vous"));
            return;
        }

        Collections.shuffle(uhcPlayers);

        UHCPlayer uhcPlayer = uhcPlayers.get(0);
        getUHCPlayer().sendMessage(ChatUtils.LIST_HEADER.getMessage("Voici les effets de " + uhcPlayer.getName()));
        for(Potion potion : uhcPlayer.getPotionEffects()) {
            getUHCPlayer().sendMessage(ChatUtils.LIST_ELEMENT.getMessage(potion.getEffect().getName()));
        }
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(!event.getKiller().equals(getUHCPlayer())) return;

        command.clicks += 1;
        TextComponent speedMessage = new TextComponent(ChatUtils.LIST_ELEMENT.getMessage("5% de speed"));
        speedMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ni azxdgfr 0 5"));
        getUHCPlayer().sendMessage(speedMessage);

        TextComponent strengthMessage = new TextComponent(ChatUtils.LIST_ELEMENT.getMessage("5% de force"));
        strengthMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ni azxdgfr 1 5"));
        getUHCPlayer().sendMessage(strengthMessage);

        TextComponent resiMessage = new TextComponent(ChatUtils.LIST_ELEMENT.getMessage("5% de résistance"));
        resiMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ni azxdgfr 2 5"));
        getUHCPlayer().sendMessage(resiMessage);
    }

    private static class AddEffectCommand extends CommandPower {

        private int clicks = 0;

        public AddEffectCommand() {
            super("Vous ne devez pas voir ca", "azxdgfr", 0, -1);
        }

        @Override
        public boolean hidePower() {
            return true;
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            if(args.length != 3) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous ne pouvez pas utiliser cette commande !"));
                return false;
            }

            if(clicks < 1) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez déjà choisi un pouvoir !"));
                return false;
            }

            int effect = Integer.parseInt(args[1]);
            int amount = Integer.parseInt(args[2]);

            switch (effect) {
                case 0:
                    if(player.getSpeed() >= 40) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez 40% de vitesse !"));
                        return false;
                    }
                    player.addSpeed(amount);
                case 1:
                    if(player.getStrength() >= 40) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez 40% de force !"));
                        return false;
                    }
                    player.addStrength(amount);
                case 2:
                    if(player.getResi() >= 40) {
                        player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez 40% de resistance !"));
                        return false;
                    }
                    player.addResi(amount);
            }
            clicks -= 1;
            return false;
        }
    }
}
