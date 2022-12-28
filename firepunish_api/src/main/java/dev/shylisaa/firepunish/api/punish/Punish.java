package dev.shylisaa.firepunish.api.punish;

import dev.shylisaa.firepunish.api.punish.reason.PunishReason;

import java.util.UUID;

public record Punish(UUID uniqueId, PunishReason reason, UUID operator, Long currentMillis) {


}
