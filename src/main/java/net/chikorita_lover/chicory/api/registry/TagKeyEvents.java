package net.chikorita_lover.chicory.api.registry;

import net.chikorita_lover.chicory.impl.TagKeyEventsImpl;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.List;

public class TagKeyEvents {
    public static <T> Event<ModifyEntries<T>> modifyEntriesEvent(TagKey<T> tag) {
        return TagKeyEventsImpl.getOrCreateModifyEntriesEvent(tag);
    }

    @FunctionalInterface
    public interface ModifyEntries<T> {
        void modifyEntries(RegistryWrapper.WrapperLookup registries, List<RegistryEntry<T>> entries);
    }
}
