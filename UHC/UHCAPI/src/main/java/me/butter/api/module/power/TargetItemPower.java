package me.butter.api.module.power;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.Vector;

public abstract class TargetItemPower extends ItemPower {

    int range;

    public TargetItemPower(String name, Material material, int range, int cooldown, int maxUses) {
        super(name, material, cooldown, maxUses);

        this.range = range;
    }
    private boolean getLookingAt(Player player, Player player1) {
        Location eye = player.getEyeLocation().clone().add(0, -1.0, 0);
        Vector toEntity = player1.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.9D;
    }

    @Override
    public void onUsePower(UHCPlayer player, Action clickAction) {
        player.sendMessage(clickAction.name());

        if(uses >= maxUses && maxUses != -1) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Vous avez déjà utilisé " + uses + " fois ce pouvoir."));
            return;
        }
        if(lastTimeUsed + cooldown > UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Vous devez attendre " +
                    GraphicUtils.convertToAccurateTime(cooldown + lastTimeUsed - UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer()) +
                    " avant d'utiliser ce pouvoir."));
            return;
        }

        UHCPlayer target = null;
        for(UHCPlayer player1 : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(getLookingAt(player.getPlayer(), player1.getPlayer()) && player.isNextTo(player1, range)) {
                target = player1;
                break;
            }
        }
        if(target == null) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Aucun joueur n'est visé."));
            return;
        }

        if(onEnable(player, target, clickAction)) {
            uses++;
            lastTimeUsed = UHCAPI.getInstance().getGameHandler().getGameConfig().getTimer();
        }
    }

    public boolean onEnable(UHCPlayer player, UHCPlayer target, Action clickAction) {
        return false;
    }
}
