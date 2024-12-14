package net.chikorita_lover.chicory.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.chikorita_lover.chicory.api.NoteBlockSoundRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.NoteBlock;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NoteBlock.class)
public class NoteBlockMixin {
    @ModifyExpressionValue(method = "onSyncedBlockEvent", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/enums/NoteBlockInstrument;getSound()Lnet/minecraft/registry/entry/RegistryEntry;"))
    private RegistryEntry<SoundEvent> modifySound(RegistryEntry<SoundEvent> sound, BlockState state, World world, BlockPos pos) {
        BlockState belowState = world.getBlockState(pos.down());
        return NoteBlockSoundRegistry.INSTANCE.getOptional(belowState.getBlock()).orElse(sound);
    }
}
