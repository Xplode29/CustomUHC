package me.butter.impl.listeners;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.Enchant;
import me.butter.api.game.GameState;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ChatUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class GameListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);

        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        Bukkit.broadcastMessage(ChatUtils.JOINED.getMessage(
                player.getDisplayName() + ChatColor.WHITE + " s'est connecté à la partie. [" + UHCAPI.get().getPlayerHandler().getPlayersInGame().size() + ChatColor.WHITE + "/" + ChatColor.GREEN + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.WHITE + "] "
        ));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        Bukkit.broadcastMessage(ChatUtils.LEFT.getMessage(
                player.getDisplayName() + ChatColor.WHITE + " s'est déconnecté de la partie. [" + (UHCAPI.get().getPlayerHandler().getPlayersInGame().size() - 1) + ChatColor.WHITE + "/" + ChatColor.GREEN + UHCAPI.get().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.WHITE + "]"
        ));
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        Player player = event.getPlayer();

        if (player == null) {
            return;
        }

        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(player);

        if (uhcPlayer.canPickItems()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!UHCAPI.get().getGameHandler().getGameConfig().isInvincibility()) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }
        final Block b = event.getBlock();
        final Location loc = new Location(b.getWorld(), b.getLocation().getBlockX() + 0.0, b.getLocation().getBlockY() + 0.0, b.getLocation().getBlockZ() + 0.0);
        if ((new Random()).nextInt(100) <= UHCAPI.get().getGameHandler().getWorldConfig().getAppleDropRate() && b.getType() == Material.LEAVES) {
            b.setType(Material.AIR);
            b.getWorld().dropItemNaturally(loc, new ItemStack(Material.APPLE, 1));
        }
    }

    @EventHandler
    public void onBlockBreakEvent(final BlockBreakEvent e) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        final Block block = e.getBlock();
        final Location loc = new Location(block.getWorld(), (block.getLocation().getBlockX() + 0.0f), (block.getLocation().getBlockY() + 0.0f), (block.getLocation().getBlockZ() + 0.0f));

        if ((new Random()).nextInt(100) <= UHCAPI.get().getGameHandler().getWorldConfig().getAppleDropRate() && block.getType() == Material.LEAVES) {
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(loc, new ItemStack(Material.APPLE));
        }
        if ((new Random()).nextInt(100) <= UHCAPI.get().getGameHandler().getWorldConfig().getFlintDropRate() && block.getType() == Material.GRAVEL) {
            block.setType(Material.AIR);
            block.getWorld().dropItemNaturally(loc, new ItemStack(Material.FLINT));
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        Player player = event.getEnchanter();
        ItemStack result = event.getItem();
        if (player == null || result == null) {
            return;
        }

        for (Enchant enchant : UHCAPI.get().getEnchantHandler().getEnchants()) {
            if (!result.getEnchantments().containsKey(enchant.getEnchantment())) continue;

            if(result.getType().name().contains("DIAMOND")) {
                if(result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getDiamondLimit()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    result.addEnchantment(enchant.getEnchantment(), enchant.getDiamondLimit());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getDiamondLimit() + " sur les items en diamant."));
                }
            }
            else if (result.getType().name().contains("IRON")) {
                if(result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getIronLimit()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    result.addEnchantment(enchant.getEnchantment(), enchant.getIronLimit());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getIronLimit() + " sur les items en fer."));
                }
            }
            else {
                if (result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getMaxLevel()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    result.addEnchantment(enchant.getEnchantment(), enchant.getMaxLevel());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getMaxLevel() + "."));
                }
            }
        }
    }

    @EventHandler
    public void damagePatch(EntityDamageByEntityEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        UHCPlayer uhcVictim = UHCAPI.get().getPlayerHandler().getUHCPlayer(victim);
        UHCPlayer uhcDamager = UHCAPI.get().getPlayerHandler().getUHCPlayer(damager);

        if (uhcDamager.getPotion(PotionEffectType.INCREASE_DAMAGE) != null || uhcDamager.getStrength() != 0) {
            event.setDamage(event.getDamage() / 2.299999952316284D * (1 + (double) uhcDamager.getStrength() / 100));
        }

        if (uhcVictim.getPotion(PotionEffectType.DAMAGE_RESISTANCE) != null || uhcVictim.getResi() != 0) {
            event.setDamage(event.getDamage() / 0.800000011920929D * (1 - (double) uhcVictim.getResi() / 100));
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();
        if (!(inv instanceof AnvilInventory)) {
            return;
        }
        AnvilInventory anvil = (AnvilInventory) inv;
        InventoryView view = event.getView();
        int rawSlot = event.getRawSlot();
        if (rawSlot != view.convertSlot(rawSlot) || rawSlot != 2) {
            return;
        }
        ItemStack result = anvil.getItem(2);
        if (result == null || result.getEnchantments().values().isEmpty()) {
            return;
        }

        for (Enchant enchant : UHCAPI.get().getEnchantHandler().getEnchants()) {
            if (!result.getEnchantments().containsKey(enchant.getEnchantment())) continue;

            if(result.getType().name().contains("DIAMOND")) {
                if(result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getDiamondLimit()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    result.addEnchantment(enchant.getEnchantment(), enchant.getDiamondLimit());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getDiamondLimit() + " sur les items en diamant."));
                }
            }
            else if (result.getType().name().contains("IRON")) {
                if(result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getIronLimit()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    result.addEnchantment(enchant.getEnchantment(), enchant.getIronLimit());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getIronLimit() + " sur les items en fer."));
                }
            }
            else {
                if (result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getMaxLevel()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    result.addEnchantment(enchant.getEnchantment(), enchant.getMaxLevel());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getMaxLevel() + "."));
                }
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (!(event.getEntity() instanceof Enderman)) return;

        event.getDrops().clear();

        final Block b = event.getEntity().getLocation().getBlock();
        final Location loc = new Location(b.getWorld(), b.getLocation().getBlockX() + 0.0, b.getLocation().getBlockY() + 0.0, b.getLocation().getBlockZ() + 0.0);

        if ((new Random()).nextInt(100) <= UHCAPI.get().getGameHandler().getWorldConfig().getEnderPearlDropRate()) {
            b.getWorld().dropItemNaturally(loc, new ItemStack(Material.ENDER_PEARL, 1));
        }
    }

    @EventHandler
    public void onRegainHeal(EntityRegainHealthEvent event) {
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (isOutsideOfBorder(e.getTo())) {
            e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 0, 90, 0));
        }

        UHCPlayer uhcPlayer = UHCAPI.get().getPlayerHandler().getUHCPlayer(e.getPlayer());
        if (!UHCAPI.get().getGameHandler().getGameState().equals(GameState.IN_GAME)) return;

        float speedWhitePercent = (float) (0.2 + (0.2 * uhcPlayer.getSpeed() / 100));
        uhcPlayer.getPlayer().setWalkSpeed(speedWhitePercent);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (UHCAPI.get().getGameHandler().getGameConfig().isPvp()) {
            event.setCancelled(true);
            return;
        }

        if (UHCAPI.get().getGameHandler().getGameConfig().isChatEnabled()) {
            return;
        }

        player.sendMessage(ChatUtils.ERROR.getMessage("Le chat est actuellement désactivé."));
        event.setCancelled(true);
    }

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onWeatherChange(ThunderChangeEvent event) {
        if (event.toThunderState()) {
            event.setCancelled(true);
        }
    }

    public static boolean isOutsideOfBorder(Location location) {
        Location loc = location;
        WorldBorder border = location.getWorld().getWorldBorder();
        double x = loc.getX();
        double z = loc.getZ();
        double size = border.getSize() / 2;
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }
}
