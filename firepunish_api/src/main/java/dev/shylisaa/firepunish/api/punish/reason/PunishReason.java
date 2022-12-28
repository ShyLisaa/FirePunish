package dev.shylisaa.firepunish.api.punish.reason;

import dev.shylisaa.firepunish.api.punish.type.PunishType;

public record PunishReason(int id, String name, long minutes, String permission, PunishType type) {
}
