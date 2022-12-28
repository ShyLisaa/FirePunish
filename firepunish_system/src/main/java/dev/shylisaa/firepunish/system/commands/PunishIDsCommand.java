package dev.shylisaa.firepunish.system.commands;

import dev.shylisaa.firepunish.api.FirePunishAPI;
import dev.shylisaa.firepunish.api.punish.cache.MessageCache;
import dev.shylisaa.firepunish.api.punish.reason.PunishReason;
import dev.shylisaa.firepunish.system.FirePunishPlugin;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.locks.Condition;

public class PunishIDsCommand extends Command {

    private final MessageCache messageCache;

    public PunishIDsCommand() {
        super("ids");

        this.messageCache = FirePunishAPI.getInstance().getMessageCache();
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer player)) return;
        Audience audience = FirePunishPlugin.getInstance().getAudience(player.getUniqueId());
        Component PREFIX = MiniMessage.miniMessage().deserialize(this.messageCache.getFromMessageCache("prefix").getAsString());

        if (!player.hasPermission("firepunish.command.ids")) {
            Component NO_PERMISSION_MESSAGE = MiniMessage.miniMessage().deserialize(this.messageCache.getFromMessageCache("no-permissions").getAsString())
                    .replaceText(config -> {
                        config.match("%PREFIX%");
                        config.replacement(PREFIX);
                    });

            audience.sendMessage(NO_PERMISSION_MESSAGE);
            return;
        }
        Component component = Component.empty();
        Component message = component.append(MiniMessage.miniMessage().deserialize(this.messageCache.getFromMessageCache("id-list-message").getAsString()));
        for (PunishReason reason : FirePunishAPI.getInstance().getReasonCache().getSortedList()) {
            Component PUNISH_ID = Component.text(reason.id());
            Component PUNISH_REASON = Component.text(reason.name());
            Component PUNISH_TYPE = Component.text(reason.type().name());
            component = component.append(message
                    .replaceText(config -> config.matchLiteral("%PREFIX%").replacement(PREFIX).build())
                    .replaceText(config -> config.matchLiteral("%ID%").replacement(PUNISH_ID).build())
                    .replaceText(config -> config.matchLiteral("%REASON%").replacement(PUNISH_REASON).build())
                    .replaceText(config -> config.matchLiteral("%TYPE%").replacement(PUNISH_TYPE).build()))
                    .appendNewline();
        }

        audience.sendMessage(component);
    }
}
