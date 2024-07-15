package me.butter.ninjago.roles.list.serpents;

import me.butter.api.UHCAPI;
import me.butter.api.module.power.CommandPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.goldenWeapons.AbstractGoldenWeapon;
import me.butter.ninjago.roles.NinjagoRole;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Pythor extends NinjagoRole {

    private final List<String> list = new ArrayList<>();

    public Pythor() {
        super("Pythor", "/roles/serpent-10/pythor", new EaterPower(), new InvokeCommand());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous possédez §9Speed 1§r permanent et §cForce 1§r la nuit.",
                "Vous connaissez une liste contenant : §5Skales§r, §5Acidicus§r, §5Skalidor§r, §5Fangtom§r, §5Fangdam§r, §5Arcturus§r et §aEd§r.",
                "Attention, §aEd§r ne gagne pas avec les serpents."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Liste des serpents (Le role §aEd§r fire dans la liste !) : \n" + String.join(", ", list));
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addSpeed(20);

        if(!UHCAPI.getInstance().getGameHandler().getGameConfig().isDay()) {
            getUHCPlayer().addStrength(15);
        }
    }

    @Override
    public void onDay() {
        getUHCPlayer().removeStrength(15);
    }

    @Override
    public void onNight() {
        getUHCPlayer().addStrength(15);
    }

    @Override
    public void onDistributionFinished() {
        for (Role role : Ninjago.getInstance().getRolesList()) {
            if (role instanceof NinjagoRole && role.getUHCPlayer() != null) {
                if(((NinjagoRole) role).isInList()) {
                    list.add(role.getUHCPlayer().getName());
                }
            }
        }
    }

    private static class InvokeCommand extends CommandPower {

        private final List<ItemStack> goldenWeapons;

        public InvokeCommand() {
            super("Invocation du Grand Dévoreur", "summon", 0, 1);
            this.goldenWeapons = Ninjago.getInstance().getGoldenWeaponManager().getWeapons().stream().map(AbstractGoldenWeapon::getItemStack).collect(Collectors.toList());
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Si vous obtenez les 4 §6armes d'or§r, vous pouvez exectuer l'invocation du Grand Dévoreur.",
                    "Vous deviendrez alors §eSolitaire§r, vous obtiendrez §7Resistance 1§r et §l3 coeurs§r permanents supplémentaires.",
                    "Cependant, vous perdez §9Speed 1§r et tous les joueurs de la partie seront informés que le Grand Dévoreur a été invoqué.",
                    "Les armes d'or seront alors detruites."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, String[] args) {
            if(player.getPlayer() == null) return false;

            if(!new HashSet<>(player.getInventory()).containsAll(goldenWeapons)) {
                player.sendMessage(ChatUtils.ERROR.getMessage("Vous n'avez pas toutes les armes d'or pour invoquer le Grand Dévoreur !"));
                return false;
            }

            for(AbstractGoldenWeapon weapon : Ninjago.getInstance().getGoldenWeaponManager().getWeapons()) {
                if(!weapon.getHolder().equals(player)) {
                    player.sendMessage(ChatUtils.ERROR.getMessage(
                            "Vous n'avez pas toutes les armes d'or pour invoquer le Grand Dévoreur ! " +
                                    "Si ce n'est pas le cas, essayez de les drop puis de les ramasser."
                    ));
                    return false;
                }
            }

            for(AbstractGoldenWeapon weapon : Ninjago.getInstance().getGoldenWeaponManager().getWeapons()) {
                weapon.onDrop();
                weapon.setHolder(null);
                player.getPlayer().getInventory().remove(weapon.getItemStack());
            }

            player.addMaxHealth(6);
            player.addResi(20);
            player.removeSpeed(20);

            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getAllPlayers()) {
                uhcPlayer.sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Le Grand devoreur a ete invoque !"));
                if(uhcPlayer.getPlayer() != null)
                    uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.ENDERDRAGON_GROWL, 3.0F, 1.0F);
            }

            return true;
        }
    }

    private static class EaterPower extends RightClickItemPower {

        public EaterPower() {
            super("Grand Devoreur", Material.NETHER_STAR, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "À l'activation, vous obtenez 20% de §9Speed§r et de §7Résistance§r supplémentaires pendant 2 minutes."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            player.addResi(20);
            player.addSpeed(20);

            Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.removeResi(20), 2 * 60 * 20);
            Bukkit.getScheduler().runTaskLater(UHCAPI.getInstance(), () -> player.removeSpeed(20), 2 * 60 * 20);

            player.sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez activé votre pouvoir !"));
            return true;
        }
    }
}
