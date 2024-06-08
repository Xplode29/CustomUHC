package me.butter.impl.clickablechat.list;

import me.butter.api.player.UHCPlayer;
import me.butter.api.utils.chat.ChatUtils;
import me.butter.impl.clickablechat.AbstractClickableChatCommand;
import me.butter.impl.clickablechat.ClickableChatHandlerImpl;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

public class ChatEffectChooser extends AbstractClickableChatCommand {


    boolean clicked;
    int amount, limit;

    public ChatEffectChooser(UHCPlayer player, int amount, int limit) {
        super(player, "choose", "effet", "amount", "limit");
        this.amount = amount;
        this.limit = limit;

        clicked = false;
    }

    @Override
    public void send() {
        TextComponent speedMsg = new TextComponent(amount + "% de speed (" + limit + "% max)");
        speedMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                ClickableChatHandlerImpl.commandPrefix + " " +
                        getArgument() + " 0 " + amount + " " + limit)
        );
        getUHCPlayer().sendMessage(speedMsg);

        TextComponent strengthMsg = new TextComponent(amount + "% de force (" + limit + "% max)");
        strengthMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                ClickableChatHandlerImpl.commandPrefix + " " +
                        getArgument() + " 1 " + amount + " " + limit)
        );
        getUHCPlayer().sendMessage(strengthMsg);

        TextComponent resiMsg = new TextComponent(amount + "% de resistance (" + limit + "% max)");
        resiMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                ClickableChatHandlerImpl.commandPrefix + " " +
                        getArgument() + " 2 " + amount + " " + limit)
        );
        getUHCPlayer().sendMessage(resiMsg);
    }

    @Override
    public void onCommand(List<String> args) {
        int effect = Integer.parseInt(args.get(0));

        switch (effect) {
            case 0:
                if(getUHCPlayer().getSpeed() >= limit && limit > 0) {
                    getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous nous pouvez pas choisir plus de " + limit + "% de vitesse !"));
                    return;
                }
                getUHCPlayer().addSpeed(amount);
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu " + amount + "% de vitesse !"));
                break;
            case 1:
                if(getUHCPlayer().getStrength() >= limit && limit > 0) {
                    getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous nous pouvez pas choisir plus de " + limit + "% de force !"));
                    return;
                }
                getUHCPlayer().addStrength(amount);
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu " + amount + "% de force !"));
                break;
            case 2:
                if(getUHCPlayer().getResi() >= limit && limit > 0) {
                    getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous nous pouvez pas choisir plus de " + limit + "% de resistance !"));
                    return;
                }
                getUHCPlayer().addResi(amount);
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu " + amount + "% de resistance !"));
                break;
        }
    }
}