package net.chikorita_lover.chicory.mixin;

import net.chikorita_lover.chicory.api.config.Config;
import net.chikorita_lover.chicory.impl.BlockSettingsImpl;
import net.chikorita_lover.chicory.impl.DefaultAttributeImpl;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registries.class)
public abstract class RegistriesMixin {
    @Inject(method = "freezeRegistries", at = @At("HEAD"))
    private static void finalizeRegistries(CallbackInfo ci) {
        Config.freezeConfigs();
        Config.reloadConfigs();
        BlockSettingsImpl.modifyBlockSettings();
        DefaultAttributeImpl.modifyAttributes();
    }
}
