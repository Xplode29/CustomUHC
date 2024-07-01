package me.butter.ninjago.goldenNinja;

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

    int power;

    public ChatEffectChooser(UHCPlayer player, int amount, int limit) {
        super(player, "choose", "effet", "amount", "limit");
        this.amount = amount;
        this.limit = limit;

        clicked = false;
    }

    public int getChosen() {
        if(clicked) {
            return power;
        }
        return -1;
    }

    @Override
    public void send() {
        getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Choisissez un effet :"));

        TextComponent speedMsg = new TextComponent(
                ChatUtils.LIST_ELEMENT.getMessage(amount + "% de speed " + (limit > 0 ? "(" + limit + "% max)" : ""))
        );
        speedMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                ClickableChatHandlerImpl.commandPrefix + " " +
                        getArgument() + " 0 " + amount + " " + limit)
        );
        getUHCPlayer().sendMessage(speedMsg);

        TextComponent strengthMsg = new TextComponent(
                ChatUtils.LIST_ELEMENT.getMessage(amount + "% de force " + (limit > 0 ? "(" + limit + "% max)" : ""))
        );
        strengthMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                ClickableChatHandlerImpl.commandPrefix + " " +
                        getArgument() + " 1 " + amount + " " + limit)
        );
        getUHCPlayer().sendMessage(strengthMsg);

        TextComponent resiMsg = new TextComponent(
                ChatUtils.LIST_ELEMENT.getMessage(amount + "% de resistance " + (limit > 0 ? "(" + limit + "% max)" : ""))
        );
        resiMsg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                ClickableChatHandlerImpl.commandPrefix + " " +
                        getArgument() + " 2 " + amount + " " + limit)
        );
        getUHCPlayer().sendMessage(resiMsg);
    }

    @Override
    public boolean onCommand(List<String> args) {
        power = Integer.parseInt(args.get(0));

        switch (power) {
            case 0:
                if(getUHCPlayer().getSpeed() >= limit && limit > 0) {
                    getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous nous pouvez pas choisir plus de " + limit + "% de vitesse !"));
                    return false;
                }
                getUHCPlayer().addSpeed(amount);
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu " + amount + "% de vitesse !"));
                clicked = true;
                return true;
            case 1:
                if(getUHCPlayer().getStrength() >= limit && limit > 0) {
                    getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous nous pouvez pas choisir plus de " + limit + "% de force !"));
                    return false;
                }
                getUHCPlayer().addStrength(amount);
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu " + amount + "% de force !"));
                clicked = true;
                return true;
            case 2:
                if(getUHCPlayer().getResi() >= limit && limit > 0) {
                    getUHCPlayer().sendMessage(ChatUtils.ERROR.getMessage("Vous nous pouvez pas choisir plus de " + limit + "% de resistance !"));
                    return false;
                }
                getUHCPlayer().addResi(amount);
                getUHCPlayer().sendMessage(ChatUtils.PLAYER_INFO.getMessage("Vous avez obtenu " + amount + "% de resistance !"));
                clicked = true;
                return true;
        }
        return false;
    }
}