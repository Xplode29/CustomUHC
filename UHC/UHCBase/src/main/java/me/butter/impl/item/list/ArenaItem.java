package me.butter.impl.item.list;

import me.butter.api.UHCAPI;
import me.butter.api.game.Preset;
import me.butter.api.player.UHCPlayer;
import me.butter.impl.game.invPresets.MeetupPreset;
import me.butter.impl.item.AbstractItem;
import me.butter.impl.listeners.JoinEvents;
import org.bukkit.Material;

public class ArenaItem extends AbstractItem {
    public ArenaItem() {
        super(Material.IRON_DOOR, "Arena");
    }

    @Override
    public void onClick(UHCPlayer uhcPlayer) {
        Preset preset = UHCAPI.getInstance().getGameHandler().getInventoriesConfig().getPreset(MeetupPreset.class);
        if (preset == null) {
            return;
        }

        uhcPlayer.getPlayer().teleport(JoinEvents.spawnArena);
        UHCAPI.getInstance().getItemHandler().removeLobbyItems(uhcPlayer);

        uhcPlayer.clearInventory();
        uhcPlayer.clearStash();
        uhcPlayer.setArmor(preset.getStartingArmor());
        uhcPlayer.setInventory(preset.getStartingInventory());
        uhcPlayer.saveInventory();
    }
}
