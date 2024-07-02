package me.butter.ninjago.roles.powers;


import me.butter.api.UHCAPI;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.ParticleEffects;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SpinjitzuPower extends RightClickItemPower {

    List<String> description;
    ChatColor color;

    public SpinjitzuPower(ChatColor color, Material material, int cooldown, int maxUses, String[] additionalDescription) {
        super(color + "Spinjitzu", material, cooldown, maxUses);

        description = new ArrayList<>(Collections.singletonList("Repousse tous les joueurs dans un rayon de 10 blocks."));
        description.addAll(Arrays.asList(additionalDescription));

        this.color = color;
    }

    @Override
    public String[] getDescription() {
        return description.toArray(new String[0]);
    }

    @Override
    public boolean onEnable(UHCPlayer player, Action clickAction) {
        Location center = player.getPlayer().getLocation();

        List<UHCPlayer> targets = new ArrayList<>();
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

                targets.add(uhcPlayer);
            }
        }

        applyEffects(player, targets);

        ParticleEffects.tornadoEffect(player.getPlayer(), GraphicUtils.convertToColor(color));
        player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous utilisez votre Spinjitzu !"));
        return true;
    }

    public void applyEffects(UHCPlayer player, List<UHCPlayer> players) {

    }
}
