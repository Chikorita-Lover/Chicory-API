package net.chikorita_lover.chicory.impl;

import net.chikorita_lover.chicory.api.registry.TagKeyEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class TagKeyEventsImpl {
    private static final Map<TagKey<?>, Event<? extends TagKeyEvents.ModifyEntries<?>>> TAG_KEY_EVENT_MAP = new HashMap<>();

    public static <T> Event<TagKeyEvents.ModifyEntries<T>> getOrCreateModifyEntriesEvent(TagKey<T> tag) {
        return (Event<TagKeyEvents.ModifyEntries<T>>) TAG_KEY_EVENT_MAP.computeIfAbsent(tag, TagKeyEventsImpl::createModifyEvent);
    }

    public static <T> @Nullable Event<TagKeyEvents.ModifyEntries<T>> getModifyEntriesEvent(TagKey<T> tag) {
        return (Event<TagKeyEvents.ModifyEntries<T>>) TAG_KEY_EVENT_MAP.get(tag);
    }

    private static <T> Event<TagKeyEvents.ModifyEntries<T>> createModifyEvent(TagKey<T> tag) {
        return EventFactory.createArrayBacked(TagKeyEvents.ModifyEntries.class, callbacks -> (registries, entries) -> {
            for (TagKeyEvents.ModifyEntries<T> callback : callbacks) {
                callback.modifyEntries(registries, entries);
            }
        });
    }
}
