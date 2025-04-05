package net.chikorita_lover.chicory.api.config;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public final class ConfigEvents {
    public static final Event<Load> CONFIG_LOAD = EventFactory.createArrayBacked(Load.class, listeners -> config -> {
        for (Load listener : listeners) {
            listener.onLoad(config);
        }
    });

    @FunctionalInterface
    public interface Load {
        void onLoad(Config config);
    }
}
