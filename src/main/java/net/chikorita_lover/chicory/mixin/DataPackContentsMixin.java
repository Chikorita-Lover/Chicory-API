package net.chikorita_lover.chicory.mixin;

import net.chikorita_lover.chicory.api.registry.TagKeyEvents;
import net.chikorita_lover.chicory.impl.TagKeyEventsImpl;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.server.DataPackContents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.*;

@Mixin(DataPackContents.class)
public class DataPackContentsMixin {
    @ModifyVariable(method = "repopulateTags", at = @At("STORE"))
    private static <T> Map<TagKey<T>, Collection<RegistryEntry<T>>> modifyTags(Map<TagKey<T>, Collection<RegistryEntry<T>>> tagEntries, DynamicRegistryManager registries, TagManagerLoader.RegistryTags<T> tags) {
        Map<TagKey<T>, Collection<RegistryEntry<T>>> modifiedEntries = new HashMap<>();
        for (Map.Entry<TagKey<T>, Collection<RegistryEntry<T>>> entry : tagEntries.entrySet()) {
            TagKey<T> tag = entry.getKey();
            List<RegistryEntry<T>> entries = new ArrayList<>(entry.getValue());
            Event<TagKeyEvents.ModifyEntries<T>> event = TagKeyEventsImpl.getModifyEntriesEvent(tag);
            if (event != null) {
                event.invoker().modifyEntries(registries, entries);
            }
            modifiedEntries.put(entry.getKey(), entries);
        }
        return modifiedEntries;
    }
}
