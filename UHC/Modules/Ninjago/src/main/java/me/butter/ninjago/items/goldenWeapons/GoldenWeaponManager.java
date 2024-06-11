package me.butter.ninjago.items.goldenWeapons;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.ninjago.structures.StructChestHolder;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;
import java.util.List;

public class GoldenWeaponManager implements Listener {

    List<AbstractGoldenWeapon> weapons;
    List<StructChestHolder> chests = new ArrayList<>();

    public GoldenWeaponManager() {
        weapons = new ArrayList<>();
        chests = new ArrayList<>();

        addWeapon(new FireSaber());
        addWeapon(new IceShuriken());
        addWeapon(new LightingNunchakus());
        addWeapon(new EarthScyte());

        Bukkit.getPluginManager().registerEvents(this, UHCAPI.getInstance());
        for(AbstractGoldenWeapon weapon : weapons) {
            if(weapon instanceof Listener) {
                Bukkit.getPluginManager().registerEvents((Listener) weapon, UHCAPI.getInstance());
            }
        }
    }

    public void addWeapon(AbstractGoldenWeapon weapon) {
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

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        UHCPlayer player = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(player == null) return;

        for(AbstractGoldenWeapon weapon : weapons) {
            if(weapon.getItemStack().isSimilar(event.getItem())) {
                if(weapon.getHolder() != null && weapon.getHolder().equals(player)) {
                    weapon.onUse();
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
