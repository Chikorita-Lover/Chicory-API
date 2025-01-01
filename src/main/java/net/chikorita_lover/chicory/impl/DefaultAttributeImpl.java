package net.chikorita_lover.chicory.impl;

import net.chikorita_lover.chicory.api.DefaultAttributeEvents;
import net.chikorita_lover.chicory.mixin.DefaultAttributeContainerAccessor;
import net.fabricmc.fabric.mixin.object.builder.DefaultAttributeRegistryAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.DefaultAttributeRegistry;
import net.minecraft.registry.Registries;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class DefaultAttributeImpl {
    public static void modifyAttributes() {
        DefaultAttributeEvents.MODIFY.invoker().modify(ModifyContextImpl.INSTANCE);
    }

    static class ModifyContextImpl implements DefaultAttributeEvents.ModifyContext {
        private static final ModifyContextImpl INSTANCE = new ModifyContextImpl();

        private ModifyContextImpl() {
        }

        public void modify(Predicate<EntityType<? extends LivingEntity>> entityPredicate, BiConsumer<DefaultAttributeContainer.Builder, EntityType<? extends LivingEntity>> builderConsumer) {
            for (EntityType<?> entity : Registries.ENTITY_TYPE) {
                EntityType<? extends LivingEntity> livingEntity;
                if (entity.getBaseClass().isAssignableFrom(LivingEntity.class) && entityPredicate.test(livingEntity = (EntityType<? extends LivingEntity>) entity)) {
                    DefaultAttributeContainer attributes = DefaultAttributeRegistry.get(livingEntity);
                    final DefaultAttributeContainer.Builder builder = DefaultAttributeContainer.builder();
                    ((DefaultAttributeContainerAccessor) attributes).getInstances().forEach((entry, attribute) -> builder.add(entry, attribute.getBaseValue()));
                    builderConsumer.accept(builder, livingEntity);
                    DefaultAttributeRegistryAccessor.getRegistry().put(livingEntity, builder.build());
                }
            }
        }
    }
}
