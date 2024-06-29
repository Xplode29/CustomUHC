package me.butter.impl.tab;

import me.butter.api.UHCAPI;
import me.butter.api.player.UHCPlayer;
import me.butter.api.tab.CustomTab;
import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractTab implements CustomTab {

    private final List<String> header;
    private final List<String> footer;

    List<UUID> playersToSend;

    public AbstractTab(List<String> header, List<String> footer) {
        this.header = header;
        this.footer = footer;

        playersToSend = new ArrayList<>();
    }

    @Override
    public void addPlayer(UHCPlayer uhcPlayer) {
        if(!playersToSend.contains(uhcPlayer.getUniqueId()))
            playersToSend.add(uhcPlayer.getUniqueId());
    }

    @Override
    public void removePlayer(UHCPlayer uhcPlayer) {
        playersToSend.remove(uhcPlayer.getUniqueId());
    }

    @Override
    public void updateTab() {
        if(header.isEmpty() && footer.isEmpty()) return;

        for(UUID uuid : playersToSend) {
            UHCPlayer uhcPlayer = UHCAPI.getInstance().getPlayerHandler().getUHCPlayer(uuid);
            if(uhcPlayer == null) continue;
            Player player = uhcPlayer.getPlayer();
            if(player == null) continue;

            sendTabToPlayer(uhcPlayer, modifyHeader(uhcPlayer, header), modifyFooter(uhcPlayer, footer));
        }
    }

    @Override
    public List<String> modifyHeader(UHCPlayer uhcPlayer,List<String> header) {
        return header;
    }

    @Override
    public List<String> modifyFooter(UHCPlayer uhcPlayer,List<String> footer) {
        return footer;
    }

    private void sendTabToPlayer(UHCPlayer uhcPlayer, List<String> header, List<String> footer) {
        try {
            PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, new ChatComponentText(Strings.join(header, "\n")));

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, new ChatComponentText(Strings.join(footer, "\n")));

            ((CraftPlayer) uhcPlayer.getPlayer()).getHandle().playerConnection.sendPacket(packet);
        }
        catch (Exception ignored) {}
    }
}
