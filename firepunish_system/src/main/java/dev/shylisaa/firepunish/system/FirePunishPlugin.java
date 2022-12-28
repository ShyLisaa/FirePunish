package dev.shylisaa.firepunish.system;

import dev.shylisaa.firepunish.api.FirePunishAPI;
import dev.shylisaa.firepunish.system.commands.PunishIDsCommand;
import dev.shylisaa.firepunish.system.events.PreJoinHandler;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.UUID;

public class FirePunishPlugin extends Plugin {

    private static FirePunishPlugin instance;
    private BungeeAudiences audiences;

    @Override
    public void onEnable() {
        instance = this;

        this.audiences = BungeeAudiences.create(this);
        new FirePunishAPI();

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new PunishIDsCommand());

        ProxyServer.getInstance().getPluginManager().registerListener(this, new PreJoinHandler());
    }

    @Override
    public void onDisable() {

    }

    public static FirePunishPlugin getInstance() {
        return instance;
    }

    public Audience getAudience(UUID uniqueId) {
        return audiences.player(uniqueId);
    }
}
