package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Wu extends NinjagoRole {

    boolean hadNinja = false;

    public Wu() {
        super("Wu", "/roles/ninjas/wu", Collections.singletonList(new StickPower()));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Lorsqu'un ninja meurt, vous obtenez 7% de speed",
                "À la mort du premier ninja, vous obtenez le pseudo de l'un des 5 autres ninjas (Lloyd, Kai, Cole, Jay, Zane)"
        };
    }

    @EventHandler
    public void onPlayerDie(UHCPlayerDeathEvent event) {
        UHCPlayer killed = event.getVictim();
        if(!(killed.getRole() instanceof NinjagoRole)) return;

        if(((NinjagoRole) killed.getRole()).isNinja()) {
            if(!hadNinja) {
                List<String> pseudos = getAliveNinjas();

                if(pseudos.isEmpty()) {
                    getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                            "Il n'y a pas de ninja en vie"
                    ));
                    return;
                }
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage(
                        pseudos.get((new Random()).nextInt(pseudos.size())) + " est un ninja"
                ));

                hadNinja = true;
            }

            getUHCPlayer().addSpeed(7);
        }
    }

    private List<String> getAliveNinjas() {
        List<String> pseudos = new ArrayList<>();
        for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(uhcPlayer.getRole() instanceof NinjagoRole && ((NinjagoRole) uhcPlayer.getRole()).isNinja()) {
                pseudos.add(uhcPlayer.getPlayer().getName());
            }
        }
        return pseudos;
    }

    private static class StickPower extends RightClickItemPower {

        public StickPower() {
            super("§eBaton", Material.DIAMOND_SWORD, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Une épée en diamant sharpness 4"
            };
        }

        @Override
        public boolean doesCancelEvent() {
            return false;
        }

        @Override
        public boolean hidePower() {
            return true;
        }

        @Override
        public ItemStack getItem() {
            ItemStack goldenSword = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta itemMeta = goldenSword.getItemMeta();

            itemMeta.spigot().setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 4, true);
            itemMeta.setDisplayName("§r" + getName());
            goldenSword.setItemMeta(itemMeta);
            return goldenSword;
        }
    }
}
