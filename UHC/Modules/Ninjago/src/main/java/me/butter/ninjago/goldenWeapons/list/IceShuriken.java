package me.butter.ninjago.goldenWeapons.list;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.WorldUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.goldenWeapons.AbstractGoldenWeapon;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import me.butter.ninjago.roles.list.ninjas.Zane;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class IceShuriken extends AbstractGoldenWeapon implements Listener {

    Snowball shuriken;
    List<Block> blocks = new ArrayList<>();

    boolean active = false;

    public IceShuriken() {
        super(
                "Shurikens de glace",
                new String[] {
                        "Vous attribue 1 coeur supplémentaire",
                        "(2 si vous êtes Zane ou Lloyd).",
                        "Click droit : Vous enfermez ",
                        "le joueur touché dans la glace.",
                        "Cooldown : 5 min"
                },
                Material.SNOW_BALL,
                5 * 60
        );
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack shuriken = super.getItemStack();
        shuriken.setAmount(2);
        return shuriken;
    }

    @Override
    public void onPickup() {
        getHolder().addMaxHealth(2);
        if(getHolder().getRole() instanceof Zane || getHolder().getRole() instanceof Lloyd) getHolder().addMaxHealth(2);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez ramassé les shurikens de glace !"));
    }

    @Override
    public void onDrop() {
        getHolder().removeMaxHealth(2);
        if(getHolder().getRole() instanceof Zane || getHolder().getRole() instanceof Lloyd) getHolder().removeMaxHealth(2);
        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez laché les shurikens de glace !"));
    }

    @Override
    public boolean onEnable() {
        Player player = getHolder().getPlayer();
        if(player == null) return false;
        if(active) return false;

        shuriken = player.launchProjectile(Snowball.class, player.getEyeLocation().getDirection().multiply(1.5));

        getHolder().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous utilisez les shurikens de glace !"));
        return true;
    }

    @EventHandler
    public void onPlayerHitBySnowball(EntityDamageByEntityEvent event) {
        if(shuriken == null || event.getDamager() != shuriken) return;
        if(getHolder() == null || !getHolder().getPlayer().equals(shuriken.getShooter())) return;

        if(!(event.getEntity() instanceof Player)) return;
        UHCPlayer target = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer((Player) event.getEntity());
        if(target == null) return;

        blocks = WorldUtils.getSphere(target.getLocation(), 5, false);

        for(Block block : blocks) {
            if(block.getType() == Material.AIR) {
                block.setType(Material.ICE);
            }
        }
        active = true;

        Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> {
            for(Block block : blocks) {
                if(block.getType() == Material.ICE) {
                    block.setType(Material.AIR);
                }
            }
            active = false;
        }, 10 * 20);

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if(active && blocks.contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(active && blocks.contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }
}
