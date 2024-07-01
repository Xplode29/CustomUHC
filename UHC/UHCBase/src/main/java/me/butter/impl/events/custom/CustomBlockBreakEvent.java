package me.butter.impl.events.custom;

import me.butter.api.player.UHCPlayer;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CustomBlockBreakEvent extends Event implements Cancellable {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private UHCPlayer player;
    private Block blockBroken;
    private int expToDrop;
    private List<ItemStack> drops;

    private BlockBreakEvent originalEvent;
    private boolean cancelled;
    private boolean modified;

    public CustomBlockBreakEvent(BlockBreakEvent originalEvent, UHCPlayer player) {
        this.originalEvent = originalEvent;
        this.player = player;
        this.blockBroken = originalEvent.getBlock();
        this.expToDrop = originalEvent.getExpToDrop();
        this.drops = new ArrayList<>(originalEvent.getBlock().getDrops(originalEvent.getPlayer().getItemInHand()));
        this.cancelled = false;
        this.modified = false;
    }

    public List<ItemStack> getDrops() {
        if(player.getPlayer().getGameMode() == GameMode.SURVIVAL) {
            return drops;
        }
        return new ArrayList<>();
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
        this.modified = true;
    }

    public int getExpToDrop() {
        return expToDrop;
    }

    public void setExpToDrop(int expToDrop) {
        this.expToDrop = expToDrop;
    }

    public UHCPlayer getUhcPlayer() {
        return player;
    }

    public Block getBlockBroken() {
        return blockBroken;
    }

    public BlockBreakEvent getOriginalEvent() {
        return originalEvent;
    }

    public boolean isModified() {
        return modified;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
