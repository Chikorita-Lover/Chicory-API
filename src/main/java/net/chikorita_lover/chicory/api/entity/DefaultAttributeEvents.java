package net.chikorita_lover.chicory.api.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;

import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class DefaultAttributeEvents {
    public static final Event<ModifyCallback> MODIFY = EventFactory.createArrayBacked(ModifyCallback.class, listeners -> context -> {
        for (ModifyCallback listener : listeners) {
            listener.modify(context);
        }
    });

    @FunctionalInterface
    public interface ModifyCallback {
        void modify(ModifyContext context);
    }

    public interface ModifyContext {
        void modify(Predicate<EntityType<? extends LivingEntity>> entityPredicate, BiConsumer<DefaultAttributeContainer.Builder, EntityType<? extends LivingEntity>> builderConsumer);

        default void modify(EntityType<? extends LivingEntity> entity, final Consumer<DefaultAttributeContainer.Builder> builderConsumer) {
            this.modify(Predicate.isEqual(entity), (builder, entityx) -> builderConsumer.accept(builder));
        }

        default void modify(Collection<EntityType<? extends LivingEntity>> entities, BiConsumer<DefaultAttributeContainer.Builder, EntityType<? extends LivingEntity>> builderConsumer) {
            Objects.requireNonNull(entities);
            this.modify(entities::contains, builderConsumer);
        }
    }
}
