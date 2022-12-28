package dev.shylisaa.firepunish.system.events;

import dev.shylisaa.firepunish.api.FirePunishAPI;
import dev.shylisaa.firepunish.api.database.DatabaseExecutor;
import dev.shylisaa.firepunish.api.json.builder.JsonArrayBuilder;
import dev.shylisaa.firepunish.api.punish.Punish;
import dev.shylisaa.firepunish.api.punish.PunishHandler;
import dev.shylisaa.firepunish.api.punish.reason.PunishReason;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;
import java.util.UUID;

public class PreJoinHandler implements Listener {

    private final PunishHandler punishHandler = FirePunishAPI.getInstance().getPunishHandler();
    private final DatabaseExecutor executor = FirePunishAPI.getInstance().getDatabaseExecutor();


    @EventHandler
    public void handle(ServerConnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        UUID uniqueId = player.getUniqueId();

        if (!this.punishHandler.isRegistered(uniqueId)) {
            this.executor.addMoreInTable("firepunish_storage",
                    new String[]{"uniqueId", "banStorage", "muteStorage"},
                    new Object[]{uniqueId.toString(), "NULL", "NULL"});
        }

        Punish punish = new Punish(uniqueId, FirePunishAPI.getInstance().getReasonCache().getReasonById(1).orElse(null), UUID.randomUUID(), System.currentTimeMillis());
        boolean executed = FirePunishAPI.getInstance().getPunishHandler().execute(punish);
        System.out.println(executed);
    }
}
