package me.butter.impl.scenario.list;

import me.butter.impl.scenario.AbstractScenario;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CutCleanScenario extends AbstractScenario {
    private final Map<Material, Material> cookedMats = new HashMap<>();

    public CutCleanScenario() {
        super("Cut Clean", Material.FURNACE);

        //food
        cookedMats.put(Material.PORK, Material.GRILLED_PORK);
        cookedMats.put(Material.RAW_FISH, Material.COOKED_FISH);
        cookedMats.put(Material.RAW_BEEF, Material.COOKED_BEEF);
        cookedMats.put(Material.RAW_CHICKEN, Material.COOKED_CHICKEN);
        cookedMats.put(Material.RABBIT, Material.COOKED_RABBIT);
        cookedMats.put(Material.MUTTON, Material.COOKED_MUTTON);

        //ores
        cookedMats.put(Material.IRON_ORE, Material.IRON_INGOT);
        cookedMats.put(Material.GOLD_ORE, Material.GOLD_INGOT);
        cookedMats.put(Material.QUARTZ_ORE, Material.QUARTZ);

        //nice stone
        cookedMats.put(Material.SANDSTONE, Material.SANDSTONE);
        cookedMats.put(Material.STONE, Material.COBBLESTONE);
        cookedMats.put(Material.COBBLESTONE, Material.COBBLESTONE);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock() == null || event.getBlock().getDrops() == null) {
            return;
        }
        Collection<ItemStack> drops = event.getBlock().getDrops(event.getPlayer().getItemInHand());
        for(ItemStack item : drops) {
            if(cookedMats.containsKey(item.getType())) {
                event.getBlock().setType(Material.AIR);
                Location dropLocation = new Location(event.getBlock().getWorld(), event.getBlock().getX() + 0.5, event.getBlock().getY() + 0.5, event.getBlock().getZ() + 0.5);
                event.getBlock().getWorld().dropItem(dropLocation, new ItemStack(cookedMats.get(item.getType()), 1));
            }
        }
    }
}
