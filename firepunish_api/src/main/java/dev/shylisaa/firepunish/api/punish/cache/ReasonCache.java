package dev.shylisaa.firepunish.api.punish.cache;

import com.google.common.collect.Lists;
import dev.shylisaa.firepunish.api.punish.reason.PunishReason;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReasonCache {

    private final List<PunishReason> reasons;

    public ReasonCache() {
        this.reasons = Lists.newArrayList();
    }

    public void addReason(PunishReason reason) {
        this.reasons.add(reason);
    }

    public Optional<PunishReason> getReasonByIdentifier(String identifier) {
        return reasons.stream().filter(reason -> reason.name().equalsIgnoreCase(identifier)).findFirst();
    }

    public Optional<PunishReason> getReasonById(int id) {
        return reasons.stream().filter(reason -> reason.id() == id).findFirst();
    }

    public void clearCache() {
        this.reasons.clear();
    }

    public Integer getCacheValueCount() {
        return this.reasons.size();
    }

    public List<PunishReason> getSortedList() {
        return this.reasons.stream().sorted(Comparator.comparingInt(PunishReason::id)).toList();
    }
}
