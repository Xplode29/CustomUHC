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
import me.butter.ninjago.roles.list.ninjas.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Pythor extends NinjagoRole {

    EaterPower eaterPower;
    InvokeCommand invokeCommand;

    List<String> list = new ArrayList<>();

    public Pythor() {
        super("Pythor", "/roles/serpent/pythor");
        addPower(eaterPower = new EaterPower());
        addPower(invokeCommand = new InvokeCommand(eaterPower));
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                "Vous possédez Speed 1 permanent et Force 1 la nuit.",
                "Vous connaissez une liste contenant : Skales, Acidicus, Skalidor, Fangtom, Fangdam, Arcturus et Ed.",
                "Attention, Ed ne gagne pas avec les serpents."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Collections.singletonList("Liste des serpents (Le role Ed fire dans la liste !) : \n" + String.join(", ", list));
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

    private static class EaterPower extends RightClickItemPower {
        public EaterPower() {
            super("Grand Devoreur", Material.NETHER_STAR, 20 * 60, 2);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "À l'activation, vous obtenez speed 2 et résistance 1 pendant 2 minutes"
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

    private static class InvokeCommand extends CommandPower {

        List<ItemStack> goldenWeapons;

        EaterPower eaterPower;

        public InvokeCommand(EaterPower eaterPower) {
            super("Invocation du Grand Dévoreur", "summon", 0, 1);

            this.eaterPower = eaterPower;
            goldenWeapons = Ninjago.getInstance().getGoldenWeaponManager().getWeapons().stream().map(AbstractGoldenWeapon::getItemStack).collect(Collectors.toList());
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                "Si vous obtenez les 4 armes d'or, vous pouvez exectuer l'invocation du Grand Dévoreur.",
                "",
                "Vous deviendrez alors solitaire, vous obtiendrez Resistance 1 et 13 coeurs permanents,",
                "Cependant, vous perdez Speed 1 et tous les joueurs de la partie seront informés que le Grand Dévoreur a été invoqué.",
                "Les armes d'or seront alors detruites"
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

            for(UHCPlayer uhcPlayer : UHCAPI.getInstance().getPlayerHandler().getPlayers()) {
                uhcPlayer.sendMessage(ChatUtils.GLOBAL_INFO.getMessage("Le Grand devoreur a ete invoque !"));
                if(uhcPlayer.getPlayer() != null)
                    uhcPlayer.getPlayer().playSound(uhcPlayer.getLocation(), Sound.ENDERDRAGON_GROWL, 3.0F, 1.0F);
            }

            return true;
        }
    }
}
