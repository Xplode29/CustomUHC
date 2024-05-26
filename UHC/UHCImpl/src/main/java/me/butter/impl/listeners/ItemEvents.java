package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.game.GameState;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class ItemEvents implements Listener {

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(uhcPlayer == null) return;

        if(UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            Role role = uhcPlayer.getRole();
            if(role != null) {
                for(Power power : role.getPowers()) {
                    if(power instanceof ItemPower) {
                        if(((ItemPower) power).getItem().isSimilar(event.getItemDrop().getItemStack())) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if (uhcPlayer == null) return;

        if (!uhcPlayer.canPickItems()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onUseItem(PlayerInteractEvent event) {
        if(event.getItem() == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(uhcPlayer == null) return;

        if (UHCAPI.getInstance().getGameHandler().getGameState() == GameState.IN_GAME) {
            Role role = uhcPlayer.getRole();
            if(role != null) {
                for(Power power : role.getPowers()) {
                    if(power instanceof ItemPower) {
                        if(((ItemPower) power).getItem().isSimilar(event.getItem())) {
                            ((ItemPower) power).onUsePower(uhcPlayer, event.getAction());
                            if(((ItemPower) power).doesCancelEvent()) {
                                event.setCancelled(true);
                            }
                            return;
                        }
                    }
                }
            }

            Material itemType = event.getItem().getType();
            if(itemType == Material.ENDER_PEARL && !UHCAPI.getInstance().getGameHandler().getItemConfig().isEnderPearl()) {
                uhcPlayer.sendMessage(ChatUtils.ERROR.getMessage("Les pearls sont actuellement désactivées."));
                event.setCancelled(true);
                return;
            }
            if(itemType == Material.BOW && !UHCAPI.getInstance().getGameHandler().getItemConfig().isBow()) {
                uhcPlayer.sendMessage(ChatUtils.ERROR.getMessage("Les arcs sont actuellement désactivés."));
                event.setCancelled(true);
                return;
            }
            if((itemType == Material.EGG || itemType == Material.SNOW_BALL) && !UHCAPI.getInstance().getGameHandler().getItemConfig().isProjectile()) {
                uhcPlayer.sendMessage(ChatUtils.ERROR.getMessage("Les projectiles sont actuellement désactivées."));
                event.setCancelled(true);
                return;
            }
            if(itemType == Material.FISHING_ROD && !UHCAPI.getInstance().getGameHandler().getItemConfig().isRod()) {
                uhcPlayer.sendMessage(ChatUtils.ERROR.getMessage("Les cannes à pèches sont actuellement désactivées."));
                event.setCancelled(true);
                return;
            }
            if(itemType == Material.LAVA_BUCKET && !UHCAPI.getInstance().getGameHandler().getItemConfig().isLavaBucket()) {
                uhcPlayer.sendMessage(ChatUtils.ERROR.getMessage("Les seaux de lave sont actuellement désactivées."));
                event.setCancelled(true);
                return;
            }
            if(itemType == Material.FLINT_AND_STEEL && !UHCAPI.getInstance().getGameHandler().getItemConfig().isFlintAndSteel()) {
                uhcPlayer.sendMessage(ChatUtils.ERROR.getMessage("Les briquets sont actuellement désactivés."));
                event.setCancelled(true);
                return;
            }
        }
    }
}
