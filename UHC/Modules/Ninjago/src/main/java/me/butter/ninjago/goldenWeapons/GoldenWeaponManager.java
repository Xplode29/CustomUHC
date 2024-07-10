package me.butter.ninjago.goldenWeapons;

import me.butter.api.UHCAPI;
import me.butter.api.player.PlayerState;
import me.butter.api.player.UHCPlayer;
import me.butter.ninjago.goldenWeapons.list.EarthScyte;
import me.butter.ninjago.goldenWeapons.list.FireSaber;
import me.butter.ninjago.goldenWeapons.list.IceShuriken;
import me.butter.ninjago.goldenWeapons.list.LightingNunchakus;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GoldenWeaponManager implements Listener {

    List<AbstractGoldenWeapon> weapons;
    List<StructChestHolder> chests;

    public FireSaber fireSaber;
    public IceShuriken iceShuriken;
    public LightingNunchakus lightingNunchakus;
    public EarthScyte earthScyte;

    public GoldenWeaponManager() {
        weapons = new ArrayList<>();
        chests = new ArrayList<>();

        addWeapon(fireSaber = new FireSaber());
        addWeapon(iceShuriken = new IceShuriken());
        addWeapon(lightingNunchakus = new LightingNunchakus());
        addWeapon(earthScyte = new EarthScyte());

        Collections.shuffle(weapons);

        Bukkit.getPluginManager().registerEvents(this, UHCAPI.getInstance());
        for(AbstractGoldenWeapon weapon : weapons) {
            if(weapon instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) weapon, UHCAPI.getInstance());
            }
        }
    }

    public void addWeapon(AbstractGoldenWeapon weapon) {
        UHCAPI.getInstance().getItemHandler().addCustomItem(weapon);
        weapons.add(weapon);
    }

    public void addChest(StructChestHolder chest) {
        chest.setWeapon(weapons.get(chests.size()));
        chests.add(chest);
    }

    public List<AbstractGoldenWeapon> getWeapons() {
        return weapons;
    }

    public List<StructChestHolder> getChests() {
        return chests;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        for(StructChestHolder chestHolder : chests) {
            if(chestHolder.isSpawned() && chestHolder.getBlockChest().getLocation().equals(event.getBlock().getLocation())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(player == null) return;
        if(player.getPlayerState() != PlayerState.IN_GAME) return;

        if(event.hasBlock()) {
            for(StructChestHolder chestHolder : chests) {
                if(chestHolder.getWeapon() != null && chestHolder.isSpawned() && chestHolder.getBlockChest().getLocation().equals(event.getClickedBlock().getLocation())) {
                    player.openMenu(new GoldenWeaponChestMenu(chestHolder), false);
                    if(player.getPlayer() != null) player.getPlayer().playSound(player.getLocation(), Sound.CHEST_OPEN, 3.0F, 1.0F);
                    event.setCancelled(true);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(player == null) return;

        for(AbstractGoldenWeapon weapon : weapons) {
            if(weapon.getItemStack().isSimilar(event.getItemDrop().getItemStack())) {
                if(weapon.getHolder() != null && weapon.getHolder().equals(player)) {
                    weapon.onDrop();
                    weapon.setHolder(null);

                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickup(PlayerPickupItemEvent event) {
        UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(player == null) return;

        for(AbstractGoldenWeapon weapon : weapons) {
            if(weapon.getItemStack().isSimilar(event.getItem().getItemStack())) {
                weapon.setHolder(player);
                weapon.onPickup();

                return;
            }
        }
    }
}
