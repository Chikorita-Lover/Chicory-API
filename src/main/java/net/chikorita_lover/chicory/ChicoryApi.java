package net.chikorita_lover.chicory;

import net.chikorita_lover.chicory.api.advancement.AdvancementExtensionRegistries;
import net.chikorita_lover.chicory.api.block.SkullTypeRegistry;
import net.chikorita_lover.chicory.api.loot.LootModificationUtils;
import net.chikorita_lover.chicory.api.registry.TagKeyEvents;
import net.chikorita_lover.chicory.impl.resource.ConfigValuesMatchResourceCondition;
import net.chikorita_lover.chicory.mixin.CombinedEntryAccessor;
import net.chikorita_lover.chicory.network.ChicoryRecipeBookSettingsS2CPacket;
import net.chikorita_lover.chicory.network.SyncConfigS2CPacket;
import net.chikorita_lover.chicory.registry.tag.ChicoryEntityTypeTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.*;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ChicoryApi implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Chicory API");
    public static final String NAMESPACE = "chicory";

    public static Identifier of(String path) {
        return Identifier.of(NAMESPACE, path);
    }

    @Override
    public void onInitialize() {
        AdvancementExtensionRegistries.registerAdvancementEvents();
        PayloadTypeRegistry.playS2C().register(ChicoryRecipeBookSettingsS2CPacket.PACKET_ID, ChicoryRecipeBookSettingsS2CPacket.PACKET_CODEC);
        PayloadTypeRegistry.playS2C().register(SyncConfigS2CPacket.PACKET_ID, SyncConfigS2CPacket.PACKET_CODEC);
        TagKeyEvents.modifyEntriesEvent(ChicoryEntityTypeTags.MONSTERS).register((entries) -> {
            Registries.ENTITY_TYPE.streamEntries().filter(entity -> !entity.value().getSpawnGroup().isPeaceful()).forEach(entries::add);
        });
        ResourceConditions.register(ConfigValuesMatchResourceCondition.TYPE);

        LootTableEvents.MODIFY.register((registryKey, builder, lootTableSource, registries) -> {
            final RegistryWrapper.Impl<EntityType<?>> entityTypes = registries.getOrThrow(RegistryKeys.ENTITY_TYPE);
            if (registryKey == LootTables.ROOT_CHARGED_CREEPER) {
                builder.modifyPools(poolBuilder -> {
                    LootModificationUtils.modifyPoolEntries(poolBuilder, entries -> {
                        if (entries.getFirst() instanceof AlternativeEntry alternativeEntry) {
                            CombinedEntryAccessor accessor = (CombinedEntryAccessor) alternativeEntry;
                            final List<LootPoolEntry> alternatives = new ArrayList<>(accessor.getChildren());
                            SkullTypeRegistry.getEntityTypes().forEach(entityType -> {
                                LeafEntry.Builder<?> entry = ItemEntry.builder(SkullTypeRegistry.getSkull(entityType)).conditionally(EntityPropertiesLootCondition.builder(LootContext.EntityReference.THIS, EntityPredicate.Builder.create().type(entityTypes, entityType)));
                                alternatives.add(entry.build());
                            });
                            accessor.setChildren(alternatives);
                        }
                    });
                });
            }
        });
    }
}