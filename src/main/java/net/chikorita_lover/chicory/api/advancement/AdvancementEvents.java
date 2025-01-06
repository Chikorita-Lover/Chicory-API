package net.chikorita_lover.chicory.api.advancement;

import net.chikorita_lover.chicory.impl.AdvancementEventsImpl;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class AdvancementEvents {
    public static final Event<ModifyAll> MODIFY_ALL = EventFactory.createArrayBacked(ModifyAll.class, listeners -> (id, builder, registries) -> {
        for (ModifyAll listener : listeners) {
            listener.modifyAdvancement(id, builder, registries);
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
}
