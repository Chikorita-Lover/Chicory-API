package net.chikorita_lover.chicory.api.advancement;

import net.chikorita_lover.chicory.impl.AdvancementEventsImpl;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.List;

public class AdvancementEvents {
    public static final Event<ModifyAll> MODIFY_ALL = EventFactory.createArrayBacked(ModifyAll.class, listeners -> (id, builder, registries) -> {
        for (ModifyAll listener : listeners) {
            listener.modifyAdvancement(id, builder, registries);
        }
    });
    public static final Event<Loaded> ALL_LOADED = EventFactory.createArrayBacked(Loaded.class, listeners -> (resourceManager, entries, registries) -> {
        for (Loaded listener : listeners) {
            listener.onAdvancementsLoaded(resourceManager, entries, registries);
        }
    });

    public static Event<Modify> modifyEvent(String path) {
        return modifyEvent(Identifier.ofVanilla(path));
    }

    public static Event<Modify> modifyEvent(Identifier id) {
        return AdvancementEventsImpl.getOrCreateModifyEvent(id);
    }

    @FunctionalInterface
    public interface Modify {
        void modifyAdvancement(ChicoryAdvancementBuilder builder, RegistryWrapper.WrapperLookup registries);
    }

    @FunctionalInterface
    public interface ModifyAll {
        void modifyAdvancement(Identifier id, ChicoryAdvancementBuilder builder, RegistryWrapper.WrapperLookup registries);
    }

    @FunctionalInterface
    public interface Loaded {
        /**
         * Called when all advancements have been loaded.
         */
        void onAdvancementsLoaded(ResourceManager resourceManager, List<AdvancementEntry> entries, RegistryWrapper.WrapperLookup registries);
    }
}
