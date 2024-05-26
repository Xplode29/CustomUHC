package me.butter.ninjago.roles.list.ninjas;

import me.butter.api.module.power.Power;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.ArrayList;

public class Mitsake extends NinjagoRole {

    public Mitsake() {
        super("Mitsake", "doc", new ArrayList<>());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez faiblesse la nuit.",
                "A l'annonce des roles, vous obtenez trois potions: poison, slow, weakness."
        };
    }

    @Override
    public void onGiveRole() {
        ItemStack heal = new Potion(PotionType.INSTANT_HEAL, 2, true).toItemStack(3);
        ItemStack poison = new Potion(PotionType.POISON, 1, true).toItemStack(1);
        ItemStack speed = new Potion(PotionType.SPEED, 0, true).toItemStack(1);

        getUHCPlayer().giveItem(heal, true);
        getUHCPlayer().giveItem(poison, true);
        getUHCPlayer().giveItem(speed, true);
    }

    @Override
    public void onDay() {
        getUHCPlayer().removePotionEffect(PotionEffectType.WEAKNESS);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addPotionEffect(PotionEffectType.WEAKNESS, -1, 1);
    }
}
