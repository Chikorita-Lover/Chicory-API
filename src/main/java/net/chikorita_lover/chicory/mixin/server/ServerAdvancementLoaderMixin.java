package net.chikorita_lover.chicory.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.api.advancement.AdvancementEvents;
import net.chikorita_lover.chicory.api.advancement.ChicoryAdvancementBuilder;
import net.chikorita_lover.chicory.impl.AdvancementEventsImpl;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.advancement.Advancement;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ServerAdvancementLoader.class)
public class ServerAdvancementLoaderMixin {
    @ModifyVariable(method = "method_20723", at = @At(value = "STORE"))
    private Advancement modifyAdvancement(Advancement advancement, @Local(argsOnly = true) Identifier id) {
        Event<AdvancementEvents.Modify> event = AdvancementEventsImpl.getModifyEvent(id);
        if (event != null) {
            ChicoryAdvancementBuilder builder = new ChicoryAdvancementBuilder(advancement);
            try {
                event.invoker().modify(builder);
                advancement = builder.build();
            } catch (Exception exception) {
                ChicoryApi.LOGGER.error("Parsing error modifying advancement {}: {}", id, exception.getMessage());
            }
        }
        return advancement;
    }
}
