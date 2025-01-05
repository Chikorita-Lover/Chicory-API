package net.chikorita_lover.chicory.api.advancement;

import net.minecraft.advancement.criterion.*;
import net.minecraft.block.Block;
import net.minecraft.data.server.advancement.vanilla.VanillaHusbandryTabAdvancementGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.predicate.BlockPredicate;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LocationPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Contains collections of blocks, items, etc. that are used in extending vanilla advancement criteria.
 * For example, custom seeds can be added to {@link #SEEDS} to extend the criteria of the A Seedy Place advancement.
 */
public final class AdvancementExtensionRegistries {
    /**
     * A collection of biomes that must be visited in order to complete the Hot Tourist Destinations advancement.
     */
    public static final ExtensionRegistry<RegistryKey<Biome>> NETHER_BIOMES = ExtensionRegistry.create();
    /**
     * A collection of biomes that must be visited in order to complete the Adventuring Time advancement.
     */
    public static final ExtensionRegistry<RegistryKey<Biome>> OVERWORLD_BIOMES = ExtensionRegistry.create();
    /**
     * A collection of blocks that may be placed to complete the A Seedy Place advancement.
     * Note this includes only crops planted with seeds; for example, wheat seeds would be included, but carrots would not.
     */
    public static final ExtensionRegistry<Block> SEEDS = ExtensionRegistry.create();
    /**
     * A collection of blocks that may be placed to complete the Planting the Past advancement.
     */
    public static final ExtensionRegistry<Block> SNIFFER_SEEDS = ExtensionRegistry.create();
    /**
     * A collection of blocks that may be interacted with using an axe to complete the Lighten Up advancement.
     * Note that any blocks added must contain {@link Properties#LIT}.
     * In vanilla, this includes all copper bulbs that are at least partially oxidized, regardless of if they are waxed.
     */
    public static final ExtensionRegistry<Block> TRIAL_CHAMBER_LIGHT_BLOCKS = ExtensionRegistry.create();
    /**
     * A collection of blocks that may be interacted with using an axe to complete the Wax Off advancement.
     */
    public static final ExtensionRegistry<Block> WAXABLE_BLOCKS = ExtensionRegistry.create();
    /**
     * A collection of blocks that may be interacted with using honeycomb to complete the Wax On advancement.
     */
    public static final ExtensionRegistry<Block> WAXED_BLOCKS = ExtensionRegistry.create();
    /**
     * A collection of entity types that must be bred in order to complete the Two by Two advancement.
     */
    public static final ExtensionRegistry<EntityType<?>> BREEDABLE_ANIMALS = ExtensionRegistry.create();
    /**
     * A collection of entity types that may be killed to complete the Monster Hunter advancement and must be killed in order to complete the Monsters Hunted advancement.
     * In vanilla, this includes all monsters except for the warden and any unused entities (e.g. the illusioner).
     */
    public static final ExtensionRegistry<EntityType<?>> MONSTERS = ExtensionRegistry.create();
    /**
     * A collection of identifiers of recipes that may be crafted to complete the Crafting a New Look advancement.
     */
    public static final ExtensionRegistry<Identifier> ARMOR_PATTERN_RECIPES = ExtensionRegistry.create();
    /**
     * A collection of identifiers of recipes that must be crafted in order to complete the Smithing with Style advancement.
     */
    public static final ExtensionRegistry<Identifier> EXCLUSIVE_ARMOR_PATTERN_RECIPES = ExtensionRegistry.create();
    /**
     * A collection of items that may be acquired through using a bucket to complete the Tactical Fishing advancement.
     * Note this does not include {@link Items#AXOLOTL_BUCKET} or {@link Items#TADPOLE_BUCKET} as those items have their own unique advancements.
     */
    public static final ExtensionRegistry<Item> FISH_BUCKET_ITEMS = ExtensionRegistry.create();
    /**
     * A collection of items that may be hooked by a fishing rod to complete the Fishy Business advancement.
     */
    public static final ExtensionRegistry<Item> FISH_ITEMS = ExtensionRegistry.create();
    /**
     * A collection of items that must be consumed in order to complete the Balanced Diet advancement.
     */
    public static final ExtensionRegistry<Item> FOOD_ITEMS = ExtensionRegistry.create();
    /**
     * A collection of loot tables that may be generated to complete the Respecting the Remnants advancement.
     * In vanilla, this includes loot tables that contain pottery sherds.
     */
    public static final ExtensionRegistry<RegistryKey<LootTable>> ARCHAEOLOGY_LOOT_TABLES = ExtensionRegistry.create();

    @ApiStatus.Internal
    public static void registerAdvancementEvents() {
        AdvancementEvents.modifyEvent("adventure/adventuring_time").register((builder, registries) -> {
            final RegistryWrapper.Impl<Biome> biomes = registries.getWrapperOrThrow(RegistryKeys.BIOME);
            OVERWORLD_BIOMES.getEntries().forEach(biome -> {
                builder.andCriterion(biome.getValue().toString(), TickCriterion.Conditions.createLocation(LocationPredicate.Builder.createBiome(biomes.getOrThrow(biome))));
            });
        });
        AdvancementEvents.modifyEvent("adventure/kill_a_mob").register((builder, registries) -> {
            MONSTERS.getEntries().forEach(type -> {
                builder.orCriterion(type.getRegistryEntry().getIdAsString(), OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(type)));
            });
        });
        AdvancementEvents.modifyEvent("adventure/kill_all_mobs").register((builder, registries) -> {
            MONSTERS.getEntries().forEach(type -> {
                builder.andCriterion(type.getRegistryEntry().getIdAsString(), OnKilledCriterion.Conditions.createPlayerKilledEntity(EntityPredicate.Builder.create().type(type)));
            });
        });
        AdvancementEvents.modifyEvent("adventure/lighten_up").register((builder, registries) -> {
            LocationPredicate.Builder locationPredicate = LocationPredicate.Builder.create().block(BlockPredicate.Builder.create().blocks(TRIAL_CHAMBER_LIGHT_BLOCKS.getEntries()).state(StatePredicate.Builder.create().exactMatch(Properties.LIT, true)));
            builder.orCriterion("chicory:lighten_up", ItemCriterion.Conditions.createItemUsedOnBlock(locationPredicate, ItemPredicate.Builder.create().items(VanillaHusbandryTabAdvancementGenerator.AXE_ITEMS)));
        });
        AdvancementEvents.modifyEvent("adventure/salvage_sherd").register((builder, registries) -> {
            ARCHAEOLOGY_LOOT_TABLES.getEntries().forEach(lootTable -> {
                Identifier id = lootTable.getValue();
                String path = id.getPath();
                String name = id.getNamespace() + ':' + path.substring(path.indexOf('/') + 1);
                builder.orCriterion(0, name, PlayerGeneratesContainerLootCriterion.Conditions.create(lootTable));
            });
        });
        AdvancementEvents.modifyEvent("adventure/trim_with_all_exclusive_armor_patterns").register((builder, registries) -> {
            EXCLUSIVE_ARMOR_PATTERN_RECIPES.getEntries().forEach(id -> {
                builder.andCriterion("armor_trimmed_" + id.toString(), RecipeCraftedCriterion.Conditions.create(id));
            });
        });
        AdvancementEvents.modifyEvent("adventure/trim_with_any_armor_pattern").register((builder, registries) -> {
            ARMOR_PATTERN_RECIPES.getEntries().forEach(id -> {
                builder.orCriterion("armor_trimmed_" + id.toString(), RecipeCraftedCriterion.Conditions.create(id));
            });
        });
        AdvancementEvents.modifyEvent("husbandry/balanced_diet").register((builder, registries) -> {
            FOOD_ITEMS.getEntries().forEach(item -> {
                builder.andCriterion(item.getRegistryEntry().getIdAsString(), ConsumeItemCriterion.Conditions.item(item));
            });
        });
        AdvancementEvents.modifyEvent("husbandry/bred_all_animals").register((builder, registries) -> {
            BREEDABLE_ANIMALS.getEntries().forEach(type -> {
                builder.andCriterion(type.getRegistryEntry().getIdAsString(), BredAnimalsCriterion.Conditions.create(EntityPredicate.Builder.create().type(type)));
            });
        });
        AdvancementEvents.modifyEvent("husbandry/fishy_business").register((builder, registries) -> {
            FISH_ITEMS.getEntries().forEach(item -> {
                builder.orCriterion(item.getRegistryEntry().getIdAsString(), FishingRodHookedCriterion.Conditions.create(Optional.empty(), Optional.empty(), Optional.of(ItemPredicate.Builder.create().items(item).build())));
            });
        });
        AdvancementEvents.modifyEvent("husbandry/plant_any_sniffer_seed").register((builder, registries) -> {
            createPlacedBlockCriteria(builder, SNIFFER_SEEDS.getEntries());
        });
        AdvancementEvents.modifyEvent("husbandry/plant_seed").register((builder, registries) -> {
            createPlacedBlockCriteria(builder, SEEDS.getEntries());
        });
        AdvancementEvents.modifyEvent("husbandry/tactical_fishing").register((builder, registries) -> {
            FISH_BUCKET_ITEMS.getEntries().forEach(item -> {
                builder.orCriterion(item.getRegistryEntry().getIdAsString(), FilledBucketCriterion.Conditions.create(ItemPredicate.Builder.create().items(item)));
            });
        });
        AdvancementEvents.modifyEvent("husbandry/wax_off").register((builder, registries) -> {
            LocationPredicate.Builder locationPredicate = LocationPredicate.Builder.create().block(BlockPredicate.Builder.create().blocks(WAXED_BLOCKS.getEntries()));
            builder.orCriterion("chicory:wax_off", ItemCriterion.Conditions.createItemUsedOnBlock(locationPredicate, ItemPredicate.Builder.create().items(VanillaHusbandryTabAdvancementGenerator.AXE_ITEMS)));
        });
        AdvancementEvents.modifyEvent("husbandry/wax_on").register((builder, registries) -> {
            LocationPredicate.Builder locationPredicate = LocationPredicate.Builder.create().block(BlockPredicate.Builder.create().blocks(WAXABLE_BLOCKS.getEntries()));
            builder.orCriterion("chicory:wax_on", ItemCriterion.Conditions.createItemUsedOnBlock(locationPredicate, ItemPredicate.Builder.create().items(Items.HONEYCOMB)));
        });
        AdvancementEvents.modifyEvent("nether/explore_nether").register((builder, registries) -> {
            final RegistryWrapper.Impl<Biome> biomes = registries.getWrapperOrThrow(RegistryKeys.BIOME);
            NETHER_BIOMES.getEntries().forEach(biome -> {
                builder.andCriterion(biome.getValue().toString(), TickCriterion.Conditions.createLocation(LocationPredicate.Builder.createBiome(biomes.getOrThrow(biome))));
            });
        });
    }

    /**
     * Adds alternative placed block criteria to the provided {@link ChicoryAdvancementBuilder} for each provided block.
     *
     * @param builder the advancement builder
     * @param blocks  the blocks to add criteria for
     */
    private static void createPlacedBlockCriteria(final ChicoryAdvancementBuilder builder, Collection<Block> blocks) {
        blocks.forEach(block -> {
            builder.orCriterion(block.getRegistryEntry().getIdAsString(), ItemCriterion.Conditions.createPlacedBlock(block));
        });
    }

    public static class ExtensionRegistry<T> {
        private final List<T> entries = new ArrayList<>();

        private static <T> ExtensionRegistry<T> create() {
            return new ExtensionRegistry<>();
        }

        /**
         * {@return the entries in this {@link ExtensionRegistry} added by mods}
         */
        public List<T> getEntries() {
            return this.entries;
        }

        @SafeVarargs
        public final void add(T... entries) {
            this.add(List.of(entries));
        }

        public final void add(Collection<T> entries) {
            this.entries.addAll(entries);
        }
    }
}
