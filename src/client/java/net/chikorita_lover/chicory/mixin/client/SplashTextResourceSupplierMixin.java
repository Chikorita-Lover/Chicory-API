package net.chikorita_lover.chicory.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.ChicoryApi;
import net.chikorita_lover.chicory.api.SplashTextRegistry;
import net.chikorita_lover.chicory.api.SplashTextRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.client.session.Session;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(SplashTextResourceSupplier.class)
public class SplashTextResourceSupplierMixin {
    @Shadow
    @Final
    private static Random RANDOM;
    @Shadow
    @Final
    private List<String> splashTexts;
    @Shadow
    @Final
    private Session session;

    @ModifyReturnValue(method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/List;", at = @At("RETURN"))
    private List<String> appendSplashTexts(List<String> texts) {
        for (Identifier resourceId : SplashTextRegistry.RESOURCE_IDS) {
            try (BufferedReader reader = MinecraftClient.getInstance().getResourceManager().openAsReader(resourceId)) {
                try {
                    texts.addAll(reader.lines().map(String::trim).toList());
                } catch (Throwable throwable) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable throwable2) {
                            throwable.addSuppressed(throwable2);
                        }
                    }
                    throw throwable;
                }
            } catch (IOException ignored) {
                ChicoryApi.LOGGER.error("There was a problem loading splash texts from file {}", resourceId);
            }
        }
        texts.addAll(SplashTextRegistry.SPLASH_TEXTS);
        return texts;
    }

    @ModifyReturnValue(method = "get", at = @At("RETURN"))
    private SplashTextRenderer tryReplace(SplashTextRenderer original) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        AtomicReference<SplashTextRenderer> builder = new AtomicReference<>(original);
        SplashTextRenderEvents.REPLACE.invoker().replaceSplashText(builder, this.splashTexts, calendar, this.session, RANDOM);
        return builder.get();
    }
}
