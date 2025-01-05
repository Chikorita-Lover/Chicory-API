package net.chikorita_lover.chicory;

import net.chikorita_lover.chicory.api.advancement.AdvancementExtensionRegistries;
import net.chikorita_lover.chicory.api.registry.TagKeyEvents;
import net.chikorita_lover.chicory.registry.tag.ChicoryEntityTypeTags;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChicoryApi implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Chicory API");
    public static final String NAMESPACE = "chicory";

    public static Identifier of(String path) {
        return Identifier.of(NAMESPACE, path);
    }

    @Override
    public void onInitialize() {
        AdvancementExtensionRegistries.registerAdvancementEvents();
        TagKeyEvents.modifyEntriesEvent(ChicoryEntityTypeTags.MONSTERS).register((registries, entries) -> {
            Registries.ENTITY_TYPE.streamEntries().filter(entity -> !entity.value().getSpawnGroup().isPeaceful()).forEach(entries::add);
        });
    }
}