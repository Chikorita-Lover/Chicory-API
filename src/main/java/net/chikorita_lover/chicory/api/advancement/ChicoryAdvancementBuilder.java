package net.chikorita_lover.chicory.api.advancement;

import net.minecraft.advancement.*;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.function.LazyContainer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A custom builder for easier modification of advancements.
 *
 * @see AdvancementEvents
 */
public class ChicoryAdvancementBuilder {
    private final Map<String, AdvancementCriterion<?>> criteria;
    private final List<List<String>> requirements;
    private Optional<Identifier> parent;
    private Optional<AdvancementDisplay> display;
    private AdvancementRewards rewards;
    private boolean sendsTelemetryEvent;

    @ApiStatus.Internal
    public ChicoryAdvancementBuilder(Advancement advancement) {
        this.parent = advancement.parent();
        this.display = advancement.display();
        this.rewards = advancement.rewards();
        this.criteria = new HashMap<>(advancement.criteria());
        this.requirements = advancement.requirements().requirements().stream().map(ArrayList::new).collect(Collectors.toCollection(ArrayList::new));
        this.sendsTelemetryEvent = advancement.sendsTelemetryEvent();
    }

    /**
     * Sets the parent advancement based on {@code id}, replacing any previous parent advancement.
     *
     * @param id the identifier of the desired parent advancement
     */
    public ChicoryAdvancementBuilder parent(Identifier id) {
        this.parent = Optional.of(id);
        return this;
    }

    /**
     * Sets the advancement's display properties, replacing any and all previous display properties.
     */
    public ChicoryAdvancementBuilder display(ItemStack icon, Text title, Text description, @Nullable Identifier background, AdvancementFrame frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return this.display(new AdvancementDisplay(icon, title, description, Optional.ofNullable(background), frame, showToast, announceToChat, hidden));
    }

    /**
     * Sets the advancement's display properties, replacing any and all previous display properties.
     */
    public ChicoryAdvancementBuilder display(ItemConvertible icon, Text title, Text description, @Nullable Identifier background, AdvancementFrame frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return this.display(new AdvancementDisplay(new ItemStack(icon.asItem()), title, description, Optional.ofNullable(background), frame, showToast, announceToChat, hidden));
    }

    /**
     * Sets the advancement's display properties, replacing any and all previous display properties.
     *
     * @param display the new display properties
     */
    public ChicoryAdvancementBuilder display(AdvancementDisplay display) {
        this.display = Optional.of(display);
        return this;
    }

    /**
     * Sets the advancement's rewards upon completion, replacing all previous rewards.
     */
    public ChicoryAdvancementBuilder rewards(AdvancementRewards.Builder builder) {
        return this.rewards(builder.build());
    }

    /**
     * Sets the advancement's rewards upon completion, replacing all previous rewards.
     */
    public ChicoryAdvancementBuilder rewards(AdvancementRewards rewards) {
        this.rewards = rewards;
        return this;
    }

    /**
     * Sets the advancement's experience reward upon completion while maintaining other previous rewards. If an experience value was previously set, it will be replaced.
     *
     * @param experience the new experience value
     */
    public ChicoryAdvancementBuilder setExperience(int experience) {
        this.rewards(new AdvancementRewards(experience, this.rewards.loot(), this.rewards.recipes(), this.rewards.function()));
        return this;
    }

    /**
     * Increases the advancement's experience reward upon completion while maintaining all previous rewards.
     *
     * @param experience the experience value to add to the advancement's original experience reward
     */
    public ChicoryAdvancementBuilder addExperience(int experience) {
        this.rewards(new AdvancementRewards(this.rewards.experience() + experience, this.rewards.loot(), this.rewards.recipes(), this.rewards.function()));
        return this;
    }

    /**
     * Adds a new loot table to the advancement's rewards upon completion while maintaining all previous rewards.
     *
     * @param loot the registry key of the loot table to add
     */
    public final ChicoryAdvancementBuilder loot(RegistryKey<LootTable> loot) {
        List<RegistryKey<LootTable>> list = new ArrayList<>(this.rewards.loot());
        list.add(loot);
        this.rewards(new AdvancementRewards(this.rewards.experience(), list, this.rewards.recipes(), this.rewards.function()));
        return this;
    }

    /**
     * Adds a new recipe to the advancement's rewards upon completion while maintaining all previous rewards.
     *
     * @param recipe the identifier of the recipe to add
     */
    public ChicoryAdvancementBuilder recipe(Identifier recipe) {
        List<Identifier> list = new ArrayList<>(this.rewards.recipes());
        list.add(recipe);
        this.rewards(new AdvancementRewards(this.rewards.experience(), this.rewards.loot(), list, this.rewards.function()));
        return this;
    }

    /**
     * Sets the function to run upon completing the advancement while maintaining all other previous rewards. If a function was previously set, it will be replaced.
     *
     * @param id the identifier of the function
     */
    public ChicoryAdvancementBuilder function(Identifier id) {
        this.rewards(new AdvancementRewards(this.rewards.experience(), this.rewards.loot(), this.rewards.recipes(), Optional.of(new LazyContainer(id))));
        return this;
    }

    /**
     * Adds a criterion that must be completed in addition to any other existing criteria in order to complete the advancement.
     */
    public ChicoryAdvancementBuilder andCriterion(final String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        this.requirements.add(List.of(name));
        return this;
    }

    /**
     * Adds a criterion that may be completed in alternative to any other existing criteria in order to complete the advancement.
     */
    public ChicoryAdvancementBuilder orCriterion(String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        this.requirements.forEach(list -> list.add(name));
        return this;
    }

    /**
     * Adds a criterion that may be completed in alternative to other existing criteria in the {@code index} criterion array in order to complete the advancement.
     *
     * @param index the index of the array to add an alternative criterion to
     */
    public ChicoryAdvancementBuilder orCriterion(int index, String name, AdvancementCriterion<?> criterion) {
        this.criteria.put(name, criterion);
        this.requirements.get(index).add(name);
        return this;
    }

    public ChicoryAdvancementBuilder sendsTelemetryEvent(boolean sendTelemetryEvent) {
        this.sendsTelemetryEvent = sendTelemetryEvent;
        return this;
    }

    @ApiStatus.Internal
    public Advancement build() {
        AdvancementRequirements requirements = new AdvancementRequirements(this.requirements);
        return new Advancement(this.parent, this.display, this.rewards, this.criteria, requirements, this.sendsTelemetryEvent);
    }
}
