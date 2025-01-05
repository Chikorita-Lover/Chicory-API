package net.chikorita_lover.chicory.api.advancement;

import net.chikorita_lover.chicory.impl.AdvancementEventsImpl;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.advancement.Advancement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

public class AdvancementEvents {
    public static Event<Modify> modifyEvent(String path) {
        return modifyEvent(Identifier.ofVanilla(path));
    }

    public static Event<Modify> modifyEvent(Identifier id) {
        return AdvancementEventsImpl.getOrCreateModifyEvent(id);
    }

    @FunctionalInterface
    public interface Modify {
        void modify(ChicoryAdvancementBuilder builder, RegistryWrapper.WrapperLookup registries);
    }
}
