package net.chikorita_lover.chicory.impl;

import net.chikorita_lover.chicory.api.advancement.AdvancementEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class AdvancementEventsImpl {
    private static final Map<Identifier, Event<AdvancementEvents.Modify>> ADVANCEMENT_EVENT_MAP = new HashMap<>();

    public static Event<AdvancementEvents.Modify> getOrCreateModifyEvent(Identifier id) {
        return ADVANCEMENT_EVENT_MAP.computeIfAbsent(id, AdvancementEventsImpl::createModifyEvent);
    }

    public static @Nullable Event<AdvancementEvents.Modify> getModifyEvent(Identifier id) {
        return ADVANCEMENT_EVENT_MAP.get(id);
    }

    private static Event<AdvancementEvents.Modify> createModifyEvent(Identifier id) {
        return EventFactory.createArrayBacked(AdvancementEvents.Modify.class, callbacks -> builder -> {
            for (AdvancementEvents.Modify callback : callbacks) {
                callback.modify(builder);
            }
        });
    }
}
