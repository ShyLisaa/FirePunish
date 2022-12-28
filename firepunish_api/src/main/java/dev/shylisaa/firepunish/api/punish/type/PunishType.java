package dev.shylisaa.firepunish.api.punish.type;

import java.util.Arrays;

public enum PunishType {

    BAN,
    MUTE;

    public static PunishType getByIdentifier(String identifier){
        return Arrays.stream(values()).filter(type -> type.name().equalsIgnoreCase(identifier)).findFirst().orElse(null);
    }
}
