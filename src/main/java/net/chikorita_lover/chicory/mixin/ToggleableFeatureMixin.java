package net.chikorita_lover.chicory.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.api.resource.ToggleableFeatureRegistry;
import net.minecraft.resource.featuretoggle.ToggleableFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ToggleableFeature.class)
public interface ToggleableFeatureMixin {
    @ModifyReturnValue(method = "isEnabled", at = @At("RETURN"))
    private boolean modifyEnabled(boolean enabled) {
        return enabled && ToggleableFeatureRegistry.isEnabled((ToggleableFeature) this);
    }
}
