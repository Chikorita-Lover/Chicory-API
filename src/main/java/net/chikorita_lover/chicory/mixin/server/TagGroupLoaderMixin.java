package net.chikorita_lover.chicory.mixin.server;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.chikorita_lover.chicory.api.registry.TagKeyEvents;
import net.chikorita_lover.chicory.impl.TagKeyEventsImpl;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagGroupLoader;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(TagGroupLoader.class)
public class TagGroupLoaderMixin {
    @ModifyExpressionValue(method = "startReload(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/registry/Registry;)Ljava/util/Optional;", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/tag/TagGroupLoader;toTagKeyedMap(Lnet/minecraft/registry/RegistryKey;Ljava/util/Map;)Ljava/util/Map;"))
    private static <T> Map<TagKey<T>, List<RegistryEntry<T>>> modifyTags(Map<TagKey<T>, List<RegistryEntry<T>>> tags) {
        Map<TagKey<T>, List<RegistryEntry<T>>> modifiedEntries = new HashMap<>();
        for (Map.Entry<TagKey<T>, List<RegistryEntry<T>>> entry : tags.entrySet()) {
            TagKey<T> tag = entry.getKey();
            List<RegistryEntry<T>> entries = new ArrayList<>(entry.getValue());
            Event<TagKeyEvents.ModifyEntries<T>> event = TagKeyEventsImpl.getModifyEntriesEvent(tag);
            if (event != null) {
                event.invoker().modifyEntries(entries);
            }
            modifiedEntries.put(entry.getKey(), entries);
        }
        return modifiedEntries;
    }
}
