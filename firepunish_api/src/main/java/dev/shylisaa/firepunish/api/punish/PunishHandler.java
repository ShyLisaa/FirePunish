package dev.shylisaa.firepunish.api.punish;

import dev.shylisaa.firepunish.api.FirePunishAPI;
import dev.shylisaa.firepunish.api.database.DatabaseExecutor;
import dev.shylisaa.firepunish.api.punish.type.PunishType;
import org.json.JSONObject;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PunishHandler {

    private final DatabaseExecutor executor = FirePunishAPI.getInstance().getDatabaseExecutor();

    public boolean isRegistered(UUID uniqueId) {
        return this.executor.existsInTable("firepunish_storage", "uniqueId", uniqueId.toString());
    }

    public boolean isBanned(UUID uniqueId) {
        return this.executor.getFromTable("firepunish_storage", "uniqueId", uniqueId.toString(), "banStorage") != null;
    }

    public boolean isMuted(UUID uniqueId) {
        return this.executor.getFromTable("firepunish_storage", "uniqueId", uniqueId.toString(), "muteStorage") != null;
    }

    public JSONObject getBanInformation(UUID uniqueId) {
        String result = this.executor.getFromTable("firepunish_storage", "uniqueId", uniqueId.toString(), "banStorage").toString();
        return new JSONObject(result);
    }

    public JSONObject getMuteInformation(UUID uniqueId) {
        String result = this.executor.getFromTable("firepunish_storage", "uniqueId", uniqueId.toString(), "muteStorage").toString();
        return new JSONObject(result);
    }

    public boolean execute(Punish punish) {
        JSONObject jsonObject = new JSONObject();
        jsonObject
                .put("reason", punish.reason().name())
                .put("operator", punish.operator().toString())
                .put("timeout", (System.currentTimeMillis() + TimeUnit.HOURS.toMillis(punish.reason().minutes())))
                .put("current", punish.currentMillis());
        switch (punish.reason().type()) {
            case BAN -> {
                if (this.executor.getFromTable("banStorage", "firepunish_storage", "uniqueId", punish.uniqueId().toString(), "banStorage") == null) {
                    this.executor.updateInTable("firepunish_storage", "banStorage", jsonObject.toString(2), "uniqueId", punish.uniqueId().toString());
                    return true;
                } else {
                    return false;
                }
            }
            case MUTE -> {
                if (this.executor.getFromTable("muteStorage", "firepunish_storage", "uniqueId", punish.uniqueId().toString(), "muteStorage") == null) {
                    this.executor.updateInTable("firepunish_storage", "muteStorage", jsonObject.toString(2), "uniqueId", punish.uniqueId().toString());
                    return true;
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
    }

    public boolean revoke(UUID uniqueId, PunishType punishType) {
        switch (punishType) {
            case BAN -> {
                if (this.executor.getFromTable("banStorage", "firepunish_storage", "uniqueId", uniqueId.toString(), "banStorage") == null) {
                    this.executor.updateInTable("firepunish_storage", "banStorage", "NULL", "uniqueId", uniqueId.toString());
                    return true;
                } else {
                    return false;
                }
            }
            case MUTE -> {
                if (this.executor.getFromTable("firepunish_storage", "muteStorage", "uniqueId", uniqueId.toString()) != null) {
                    this.executor.updateInTable("firepunish_storage", "muteStorage", "NULL", "uniqueId", uniqueId.toString());
                    return true;
                } else {
                    return false;
                }
            }
            default -> {
                return false;
            }
        }
    }
}
