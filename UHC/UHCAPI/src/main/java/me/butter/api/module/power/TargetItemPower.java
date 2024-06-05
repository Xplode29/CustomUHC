package me.butter.api.module.power;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.GraphicUtils;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;

public abstract class TargetItemPower extends ItemPower {

    int range = 20;

    public TargetItemPower(String name, Material material, int range, int cooldown, int maxUses) {
        super(name, material, cooldown, maxUses);

        this.range = range;
    }

    public UHCPlayer getTargetPlayer(Player player, int range) {
        List<Entity> nearbyE = player.getNearbyEntities(range, range, range);
        ArrayList<LivingEntity> livingE = new ArrayList<>();

        for (Entity e : nearbyE) {
            if (e instanceof LivingEntity) {
                livingE.add((LivingEntity) e);
            }
        }

        BlockIterator bItr = new BlockIterator(player, range);
        Block block;
        Location loc;
        int bx, by, bz;
        double ex, ey, ez;
        // loop through player's line of sight
        while (bItr.hasNext()) {
            block = bItr.next();
            bx = block.getX();
            by = block.getY();
            bz = block.getZ();
            // check for entities near this block in the line of sight
            for (LivingEntity entity : livingE) {
                loc = entity.getLocation();
                ex = loc.getX();
                ey = loc.getY();
                ez = loc.getZ();
                if ((bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)) {
                    // entity is close enough, set target and stop
                    if(entity instanceof Player) {
                        if(UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) entity) != null) {
                            return UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) entity);
                        }
                    }
                }
            }
        }
        return null;
    }

    public void onUsePower(UHCPlayer player, Action clickAction) {
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

        UHCPlayer target = getTargetPlayer(player.getPlayer(), range);
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
