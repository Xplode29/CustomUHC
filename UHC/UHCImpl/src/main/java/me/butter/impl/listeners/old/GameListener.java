package me.butter.impl.listeners.old;

import me.butter.api.UHCAPI;
import me.butter.api.enchant.Enchant;
import me.butter.api.game.GameState;
import me.butter.api.module.power.ItemPower;
import me.butter.api.module.power.Power;
import me.butter.api.module.roles.Role;
import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.ChatUtils;
import me.butter.impl.events.EventUtils;
import me.butter.impl.events.custom.CustomBlockBreakEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.*;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;
import java.util.Random;

public class GameListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) return;

        Player player = event.getPlayer();
        if (player == null) return;

        Bukkit.broadcastMessage(ChatUtils.JOINED.getMessage(
                player.getDisplayName() + ChatColor.WHITE + " s'est connecté à la partie. [" + UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() + ChatColor.WHITE + "/" + ChatColor.GREEN + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.WHITE + "] "
        ));
    } //Done

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) return;

        Player player = event.getPlayer();

        if (player == null) return;

        Bukkit.broadcastMessage(ChatUtils.LEFT.getMessage(
                player.getDisplayName() + ChatColor.WHITE + " s'est déconnecté de la partie. [" + (UHCAPI.getInstance().getPlayerHandler().getPlayersInGame().size() - 1) + ChatColor.WHITE + "/" + ChatColor.GREEN + UHCAPI.getInstance().getGameHandler().getGameConfig().getMaxPlayers() + ChatColor.WHITE + "]"
        ));
    } //Done

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (isOutsideOfBorder(e.getTo())) {
            e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 0, 90, 0));
        }

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(e.getPlayer());
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) return;

        float speedWhitePercent = (float) (0.2 + (0.2 * uhcPlayer.getSpeed() / 100));
        uhcPlayer.getPlayer().setWalkSpeed(speedWhitePercent);
    } //Done

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(event.getPlayer());
        if(uhcPlayer == null) return;

        Role role = uhcPlayer.getRole();
        if(role == null) return;

        for(Power power : role.getPowers()) {
            if(power instanceof ItemPower) {
                if(((ItemPower) power).getItem().isSimilar(event.getItemDrop().getItemStack())) {
                    event.setCancelled(true);
                    return;
                }
            }
        }
    } //Done

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) return;

        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if (!uhcPlayer.canPickItems()) {
            event.setCancelled(true);
        }
    } //Done

    @EventHandler
    public void onPlayerUseItem(PlayerInteractEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        Player player = event.getPlayer();
        if(player == null || event.getItem() == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return;

        Role role = uhcPlayer.getRole();
        if(role == null) return;

        for(Power power : role.getPowers()) {
            if(power instanceof ItemPower) {
                if(((ItemPower) power).getItem().isSimilar(event.getItem())) {
                    ((ItemPower) power).onUsePower(uhcPlayer, event.getAction());

                    if(((ItemPower) power).doesCancelEvent()) {
                        event.setCancelled(true);
                    }
                    return;
                }
            }
        }

        Material itemType = event.getItem().getType();
        if(itemType == Material.ENDER_PEARL && !UHCAPI.getInstance().getGameHandler().getItemConfig().isEnderPearl()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Les pearls sont actuellement désactivées."));
            event.setCancelled(true);
            return;
        }
        if(itemType == Material.BOW && !UHCAPI.getInstance().getGameHandler().getItemConfig().isBow()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Les arcs sont actuellement désactivés."));
            event.setCancelled(true);
            return;
        }
        if((itemType == Material.EGG || itemType == Material.SNOW_BALL) && !UHCAPI.getInstance().getGameHandler().getItemConfig().isProjectile()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Les projectiles sont actuellement désactivées."));
            event.setCancelled(true);
            return;
        }
        if(itemType == Material.FISHING_ROD && !UHCAPI.getInstance().getGameHandler().getItemConfig().isRod()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Les cannes à pèches sont actuellement désactivées."));
            event.setCancelled(true);
            return;
        }
        if(itemType == Material.LAVA_BUCKET && !UHCAPI.getInstance().getGameHandler().getItemConfig().isLavaBucket()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Les seaux de lave sont actuellement désactivées."));
            event.setCancelled(true);
            return;
        }
        if(itemType == Material.FLINT_AND_STEEL && !UHCAPI.getInstance().getGameHandler().getItemConfig().isFlintAndSteel()) {
            player.sendMessage(ChatUtils.ERROR.getMessage("Les briquets sont actuellement désactivés."));
            event.setCancelled(true);
            return;
        }
    } //Done

    @EventHandler(priority = EventPriority.LOWEST)
    public void overrideBlockBreak(BlockBreakEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) return;
        if(event.getBlock() == null) return;
        if(event.getBlock().getType() == Material.AIR) return;

        Player player = event.getPlayer();
        if (player == null) return;

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return;

        CustomBlockBreakEvent blockBreakEvent = new CustomBlockBreakEvent(event, uhcPlayer);
        EventUtils.callEvent(blockBreakEvent);

        if (blockBreakEvent.isCancelled()) {
            event.setCancelled(true);
            return;
        }

        event.setExpToDrop(blockBreakEvent.getExpToDrop());

        if(blockBreakEvent.isModified()) {
            event.getBlock().setType(Material.AIR);
            Location dropLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX() + 0.5, event.getBlock().getY() + 0.5, event.getBlock().getZ() + 0.5);
            for(ItemStack item : blockBreakEvent.getDrops()) {
                event.getBlock().getWorld().dropItem(dropLocation, item);
            }
        }
    } //Done

    @EventHandler
    public void onBlockBreak(CustomBlockBreakEvent event) {
        UHCPlayer uhcPlayer = event.getUhcPlayer();
        final Block block = event.getBlockBroken();

        if(block.getType() == Material.DIAMOND_ORE) {
            if(uhcPlayer.getDiamondMined() < UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit()) {
                uhcPlayer.setDiamondMined(uhcPlayer.getDiamondMined() + 1);
                uhcPlayer.sendActionBar("§c§lDiamants minés : §7" + uhcPlayer.getDiamondMined() + " / " + UHCAPI.getInstance().getGameHandler().getWorldConfig().getDiamondLimit());
            }
            else {
                event.setDrops(Collections.singletonList(new ItemStack(Material.GOLD_INGOT, 1)));
            }
        }

        if ((new Random()).nextInt(100) <= UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() && (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2)) {
            event.setDrops(Collections.singletonList(new ItemStack(Material.APPLE, 1)));
        }
        if ((new Random()).nextInt(100) <= UHCAPI.getInstance().getGameHandler().getWorldConfig().getFlintDropRate() && block.getType() == Material.GRAVEL) {
            event.setDrops(Collections.singletonList(new ItemStack(Material.FLINT, 1)));
        }

        event.setExpToDrop((int) (event.getExpToDrop() * (1 + (UHCAPI.getInstance().getGameHandler().getWorldConfig().getExpBoost() / 100f))));
    } //Done

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }
        final Block b = event.getBlock();
        final Location loc = new Location(b.getWorld(), b.getLocation().getBlockX() + 0.0, b.getLocation().getBlockY() + 0.0, b.getLocation().getBlockZ() + 0.0);
        if ((new Random()).nextInt(100) <= UHCAPI.getInstance().getGameHandler().getWorldConfig().getAppleDropRate() && b.getType() == Material.LEAVES) {
            b.setType(Material.AIR);
            b.getWorld().dropItemNaturally(loc, new ItemStack(Material.APPLE, 1));
        }
    } //Done

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getClickedInventory();

        UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(player);
        if(uhcPlayer == null) return;

        Role role = uhcPlayer.getRole();
        if(role != null && (uhcPlayer.getPlayer().getOpenInventory().getTopInventory().getType() != InventoryType.CRAFTING)) {
            for(Power power : role.getPowers()) {
                if(power instanceof ItemPower) {
                    if(((ItemPower) power).getItem().isSimilar(event.getCurrentItem())) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }

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

        for (Enchant enchant : UHCAPI.getInstance().getEnchantHandler().getEnchants()) {
            if (!result.getEnchantments().containsKey(enchant.getEnchantment())) continue;

            if(result.getType().name().contains("DIAMOND")) {
                if(result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getDiamondLevel()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    if(enchant.getDiamondLevel() > 0)
                        result.addEnchantment(enchant.getEnchantment(), enchant.getDiamondLevel());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getDiamondLevel() + " sur les items en diamant."));
                }
            }
            else if (result.getType().name().contains("IRON")) {
                if(result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getIronLevel()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    if(enchant.getIronLevel() > 0)
                        result.addEnchantment(enchant.getEnchantment(), enchant.getIronLevel());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getIronLevel() + " sur les items en fer."));
                }
            }
            else {
                if (result.getEnchantmentLevel(enchant.getEnchantment()) > enchant.getMaxLevel()) {
                    result.removeEnchantment(enchant.getEnchantment());
                    if(enchant.getMaxLevel() > 0)
                        result.addEnchantment(enchant.getEnchantment(), enchant.getMaxLevel());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getMaxLevel() + "."));
                }
            }
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        Player player = event.getEnchanter();
        ItemStack result = event.getItem();

        if (player == null || result == null) {
            return;
        }

        for (Enchant enchant : UHCAPI.getInstance().getEnchantHandler().getEnchants()) {
            if (!event.getEnchantsToAdd().containsKey(enchant.getEnchantment())) continue;
            int level = event.getEnchantsToAdd().get(enchant.getEnchantment());

            if(result.getType().name().contains("DIAMOND")) {
                if(level > enchant.getDiamondLevel()) {
                    event.getEnchantsToAdd().remove(enchant.getEnchantment());
                    if(enchant.getDiamondLevel() > 0)
                        event.getEnchantsToAdd().put(enchant.getEnchantment(), enchant.getDiamondLevel());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getDiamondLevel() + " sur les items en diamant."));
                }
            }
            else if (result.getType().name().contains("IRON")) {
                if(level > enchant.getIronLevel()) {
                    event.getEnchantsToAdd().remove(enchant.getEnchantment());
                    if(enchant.getIronLevel() > 0)
                        event.getEnchantsToAdd().put(enchant.getEnchantment(), enchant.getIronLevel());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getIronLevel() + " sur les items en fer."));
                }
            }
            else {
                if(level > enchant.getMaxLevel()) {
                    event.getEnchantsToAdd().remove(enchant.getEnchantment());
                    if(enchant.getMaxLevel() > 0)
                        event.getEnchantsToAdd().put(enchant.getEnchantment(), enchant.getMaxLevel());
                    player.sendMessage(ChatUtils.ERROR.getMessage("L'enchantement " + enchant.getName() + " est désactivé au dessus du niveau " + enchant.getMaxLevel() + "."));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamagedByPlayer(EntityDamageByEntityEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        UHCPlayer uhcVictim = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(victim);
        UHCPlayer uhcDamager = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(damager);

        if (uhcDamager.getPotion(PotionEffectType.INCREASE_DAMAGE) != null || uhcDamager.getStrength() != 0) {
            event.setDamage(event.getDamage() / 2.299999952316284D * (1 + (double) uhcDamager.getStrength() / 100));
        }

        if (uhcVictim.getPotion(PotionEffectType.DAMAGE_RESISTANCE) != null || uhcVictim.getResi() != 0) {
            event.setDamage(event.getDamage() / 0.800000011920929D * (1 - (double) uhcVictim.getResi() / 100));
        }
    } //Done

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (!UHCAPI.getInstance().getGameHandler().getGameConfig().isInvincibility()) {
            return;
        }

        event.setCancelled(true);
    } //Done

    @EventHandler
    public void onRegainHeal(EntityRegainHealthEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED || event.getRegainReason() == EntityRegainHealthEvent.RegainReason.EATING) {
            event.setCancelled(true);
        }
    } //Done

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!UHCAPI.getInstance().getGameHandler().getGameState().equals(GameState.IN_GAME)) {
            return;
        }

        if (!(event.getEntity() instanceof Enderman)) return;

        event.getDrops().clear();

        final Block b = event.getEntity().getLocation().getBlock();
        final Location loc = new Location(b.getWorld(), b.getLocation().getBlockX() + 0.0, b.getLocation().getBlockY() + 0.0, b.getLocation().getBlockZ() + 0.0);

        if ((new Random()).nextInt(100) <= UHCAPI.getInstance().getGameHandler().getWorldConfig().getEnderPearlDropRate()) {
            b.getWorld().dropItemNaturally(loc, new ItemStack(Material.ENDER_PEARL, 1));
        }
    } //Done

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (UHCAPI.getInstance().getGameHandler().getGameConfig().isPvp()) {
            event.setCancelled(true);
            return;
        }

        if (UHCAPI.getInstance().getGameHandler().getGameConfig().isChatEnabled()) {
            return;
        }

        player.sendMessage(ChatUtils.ERROR.getMessage("Le chat est actuellement désactivé."));
        event.setCancelled(true);
    } //Done

    @EventHandler
    private void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    } //Done

    @EventHandler
    private void onWeatherChange(ThunderChangeEvent event) {
        if (event.toThunderState()) {
            event.setCancelled(true);
        }
    } //Done

    public static boolean isOutsideOfBorder(Location location) {
        Location loc = location;
        WorldBorder border = location.getWorld().getWorldBorder();
        double x = loc.getX();
        double z = loc.getZ();
        double size = border.getSize() / 2;
        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    } //Done
}
