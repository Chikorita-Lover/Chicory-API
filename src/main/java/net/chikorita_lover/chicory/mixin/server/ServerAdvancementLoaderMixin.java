package net.chikorita_lover.chicory.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.api.advancement.AdvancementEvents;
import net.chikorita_lover.chicory.api.advancement.ChicoryAdvancementBuilder;
import net.chikorita_lover.chicory.impl.AdvancementEventsImpl;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.advancement.Advancement;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerAdvancementLoader.class)
public class ServerAdvancementLoaderMixin {
    @Shadow
    @Final
    private RegistryWrapper.WrapperLookup registryLookup;

    @ModifyVariable(method = "method_20723", at = @At(value = "STORE"))
    private Advancement modifyAdvancement(Advancement advancement, @Local(argsOnly = true) Identifier id) {
        ChicoryAdvancementBuilder builder = new ChicoryAdvancementBuilder(advancement);
        Event<AdvancementEvents.Modify> event = AdvancementEventsImpl.getModifyEvent(id);
        try {
            AdvancementEvents.MODIFY_ALL.invoker().modifyAdvancement(id, builder, this.registryLookup);
            if (event != null) {
                event.invoker().modifyAdvancement(builder, this.registryLookup);
            }
            advancement = builder.build();
        } catch (Exception exception) {
            ChicoryApi.LOGGER.error("Parsing error modifying advancement {}: {}", id, exception.getMessage());
        }
        return advancement;
    }
}
