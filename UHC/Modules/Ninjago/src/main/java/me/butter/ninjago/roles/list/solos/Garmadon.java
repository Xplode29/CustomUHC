package me.butter.ninjago.roles.list.solos;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import me.butter.api.UHCAPI;
import me.butter.api.menu.Button;
import me.butter.api.module.power.EnchantedItemPower;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.RightClickItemPower;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ItemBuilder;
import me.butter.api.utils.ParticleUtils;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.events.custom.UHCPlayerDeathEvent;
import me.butter.impl.menu.AbstractMenu;
import me.butter.impl.menu.ButtonImpl;
import me.butter.ninjago.Ninjago;
import me.butter.ninjago.roles.CampEnum;
import me.butter.ninjago.roles.NinjagoRole;
import me.butter.ninjago.roles.list.ninjas.Lloyd;
import me.butter.ninjago.roles.list.ninjas.Wu;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Garmadon extends NinjagoRole {
    boolean pacted = false;
    boolean solo = false;

    UHCPlayer lloyd, wu;

    public Garmadon() {
        super("Garmadon", "/roles/hybrides/garmadon");
        addPower(new PactePower());
    }

    @Override
    public String[] getDescription() {
        return new String[] {
                "Vous avez 2 coeurs supplémentaires permanents. ",
                "Vous possedez deux pactes: Gentil ou Mechant."
        };
    }

    @Override
    public List<String> additionalDescription() {
        return Arrays.asList(
                lloyd == null ? "Pas de Lloyd" : "Lloyd : " + lloyd.getName(),
                solo ?  (wu == null ? "Pas de Wu" : "Wu : " + wu.getName()) : ""
        );
    }

    @Override
    public boolean isElementalMaster() {
        return true;
    }

    @Override
    public void onGiveRole() {
        getUHCPlayer().addMaxHealth(4);
    }

    @Override
    public void onDistributionFinished() {
        for(UHCPlayer player : UHCAPI.getInstance().getPlayerHandler().getPlayersInGame()) {
            if(player.getRole() instanceof Lloyd) lloyd = player;
            if(player.getRole() instanceof Wu) wu = player;
        }
    }

    @EventHandler
    public void onKillPlayer(UHCPlayerDeathEvent event) {
        if(event.getKiller() != getUHCPlayer()) return;
        if(!solo) return;

        if(event.getVictim().equals(lloyd)) {
            getUHCPlayer().addResi(20);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez tue Lloyd. Vous gagnez Resistance 1."));
        }

        if(event.getVictim().equals(wu)) {
            StickPower stickPower = new StickPower();
            addPower(stickPower);
            getUHCPlayer().giveItem(stickPower.getItem(), true);

            SpinjitzuPower spinjitzuPower = new SpinjitzuPower();
            addPower(spinjitzuPower);
            getUHCPlayer().giveItem(spinjitzuPower.getItem(), true);

            getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez tue Wu. Vous obtenez le Baton de Wu et le Spinjitzu."));
        }
    }

    private class PactePower extends ItemPower {

        public PactePower() {
            super("Choix de Pacte", Material.CHEST, 0, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "Vous permet de choisir votre pacte"
            };
        }

        @Override
        public boolean hideCooldowns() {
            return true;
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            if(!pacted) {
                player.openMenu(new PacteMenu(), false);
            }
            return false;
        }

        private class PacteMenu extends AbstractMenu {

            public PacteMenu() {
                super("Choix de Pacte", 5 * 9, true);
            }

            @Override
            public Map<Integer, Button> getButtons() {
                Map<Integer, Button> buttons = Maps.newHashMap();

                buttons.put(11, new ButtonImpl() {
                    @Override
                    public ItemStack getIcon() {
                        return new ItemBuilder(Material.INK_SACK, 1, (byte) 5)
                                .setName("§aGentil")
                                .setLore(
                                        ChatUtils.LIST_ELEMENT.prefix + "Vous gagnez avec les Ninjas",
                                        ChatUtils.LIST_ELEMENT.prefix + "Lorsque vous vous trouvez à 20 blocks de Lloyd,",
                                        "vous obtenez Resistance 1",
                                        ChatUtils.LIST_ELEMENT.prefix + "Lorsque le nombre de ninjas en vie est trop important,",
                                        "vous passez en duo avec Lloyd"
                                )
                                .toItemStack();
                    }

                    @Override
                    public void onClick(UHCPlayer player, ClickType clickType) {
                        pacted = true;
                        solo = false;
                        getUHCPlayer().getRole().setCamp(CampEnum.NINJA.getCamp());
                        new LloydRunnable(getOpener());

                        getUHCPlayer().getPlayer().setItemInHand(new ItemStack(Material.AIR));
                        closeMenu();
                    }
                });

                buttons.put(15, new ButtonImpl() {
                    @Override
                    public ItemStack getIcon() {
                        return new ItemBuilder(Material.INK_SACK, 1, (byte) 5)
                                .setName("§eSolitaire")
                                .setLore(
                                        ChatUtils.LIST_ELEMENT.prefix + "Vous gagnez Seul",
                                        ChatUtils.LIST_ELEMENT.prefix + "Vous obtenez Force 1 permanent.",
                                        ChatUtils.LIST_ELEMENT.prefix + "Lorsque vous tuez Lloyd,",
                                        "Vous obtenez Resistance 1 permanent",
                                        ChatUtils.LIST_ELEMENT.prefix + "Vous obtenez le pseudo de Wu.",
                                        "Lorsque vous le tuez, vous obtenez Son baton et",
                                        "le Spinjitzu qui vous octroit Speed 1",
                                        "pendant 1 minute a son activation."
                                )
                                .toItemStack();
                    }

                    @Override
                    public void onClick(UHCPlayer player, ClickType clickType) {
                        pacted = true;
                        solo = true;
                        getUHCPlayer().getRole().setCamp(CampEnum.SOLO.getCamp());
                        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage(wu == null ? "Pas de Wu" : "Wu : " + wu.getName()));
                        getUHCPlayer().addStrength(15);

                        getUHCPlayer().getPlayer().setItemInHand(new ItemStack(Material.AIR));
                        closeMenu();
                    }
                });
                return buttons;
            }
        }

        private class LloydRunnable extends BukkitRunnable {

            UHCPlayer garmadon;
            boolean nextToLloyd = false;

            public LloydRunnable(UHCPlayer garmadon) {
                this.garmadon = garmadon;

                runTaskTimer(Ninjago.getInstance(), 0, 20);
            }

            @Override
            public void run() {
                if(garmadon == null || lloyd == null) {
                    cancel();
                    return;
                }

                if(lloyd.getPlayerState() != PlayerState.IN_GAME) {
                    if(nextToLloyd) {
                        garmadon.removeResi(20);
                        nextToLloyd = false;
                    }
                    return;
                }

                if(garmadon.isNextTo(lloyd, 20) && !nextToLloyd) {
                    garmadon.addResi(20);
                    lloyd.addResi(20);
                    nextToLloyd = true;
                }
                else if(!garmadon.isNextTo(lloyd, 20) && nextToLloyd) {
                    garmadon.removeResi(20);
                    lloyd.removeResi(20);
                    nextToLloyd = false;
                }
            }
        }
    }

    private static class StickPower extends EnchantedItemPower {
        public StickPower() {
            super("§eBaton", Material.DIAMOND_SWORD, ImmutableMap.of(Enchantment.DAMAGE_ALL, 4));
        }

        @Override
        public String[] getDescription() {
            return new String[]{
                    "Une épée en diamant Tranchant 4"
            };
        }
    }

    private static class SpinjitzuPower extends RightClickItemPower {

        public SpinjitzuPower() {
            super(ChatColor.DARK_PURPLE + "Spinjitzu", Material.NETHER_STAR, 20 * 60, -1);
        }

        @Override
        public String[] getDescription() {
            return new String[] {
                    "À l'activation, repousse tous les joueurs dans un rayon de 10 blocks.",
                    "vous obtenez Speed 1 pendant 1 minute."
            };
        }

        @Override
        public boolean onEnable(UHCPlayer player, Action clickAction) {
            Location center = player.getPlayer().getLocation();

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
                }
            }

            player.addSpeed(10);
            Bukkit.getScheduler().runTaskLater(Ninjago.getInstance(), () -> player.removeSpeed(10), 2 * 60 * 20);

            ParticleUtils.tornadoEffect(player.getPlayer(), Color.PURPLE);
            return true;
        }
    }
}
