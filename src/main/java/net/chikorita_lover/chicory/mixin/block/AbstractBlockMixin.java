package net.chikorita_lover.chicory.mixin.block;

import net.chikorita_lover.chicory.api.BlockSettingsHolder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.sound.BlockSoundGroup;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(AbstractBlock.class)
public class AbstractBlockMixin implements BlockSettingsHolder {
    @Mutable
    @Shadow
    @Final
    protected boolean collidable;
    @Shadow
    @Nullable
    protected RegistryKey<LootTable> lootTableKey;
    @Mutable
    @Shadow
    @Final
    protected float resistance;
    @Mutable
    @Shadow
    @Final
    protected boolean randomTicks;
    @Mutable
    @Shadow
    @Final
    protected BlockSoundGroup soundGroup;
    @Mutable
    @Shadow
    @Final
    protected float slipperiness;
    @Mutable
    @Shadow
    @Final
    protected float velocityMultiplier;
    @Mutable
    @Shadow
    @Final
    protected float jumpVelocityMultiplier;
    @Mutable
    @Shadow
    @Final
    protected boolean dynamicBounds;
    @Mutable
    @Shadow
    @Final
    protected FeatureSet requiredFeatures;
    @Shadow
    @Final
    protected AbstractBlock.Settings settings;

    @Override
    public void chicory$updateSettings() {
        this.collidable = settings.collidable;
        this.lootTableKey = settings.lootTableKey;
        this.resistance = settings.resistance;
        this.randomTicks = settings.randomTicks;
        this.soundGroup = settings.soundGroup;
        this.slipperiness = settings.slipperiness;
        this.velocityMultiplier = settings.velocityMultiplier;
        this.jumpVelocityMultiplier = settings.jumpVelocityMultiplier;
        this.dynamicBounds = settings.dynamicBounds;
        this.requiredFeatures = settings.requiredFeatures;
    }

    @Mixin(AbstractBlock.AbstractBlockState.class)
    static abstract class AbstractBlockStateMixin implements BlockSettingsHolder {
        @Mutable
        @Shadow
        @Final
        private int luminance;
        @Mutable
        @Shadow
        @Final
        private boolean hasSidedTransparency;
        @Mutable
        @Shadow
        @Final
        private boolean isAir;
        @Mutable
        @Shadow
        @Final
        private boolean burnable;
        @Mutable
        @Shadow
        @Final
        @Deprecated
        private boolean liquid;
        @Mutable
        @Shadow
        @Final
        private PistonBehavior pistonBehavior;
        @Mutable
        @Shadow
        @Final
        private MapColor mapColor;
        @Mutable
        @Shadow
        @Final
        private float hardness;
        @Mutable
        @Shadow
        @Final
        private boolean toolRequired;
        @Mutable
        @Shadow
        @Final
        private boolean opaque;
        @Mutable
        @Shadow
        @Final
        private AbstractBlock.ContextPredicate solidBlockPredicate;
        @Mutable
        @Shadow
        @Final
        private AbstractBlock.ContextPredicate suffocationPredicate;
        @Mutable
        @Shadow
        @Final
        private AbstractBlock.ContextPredicate blockVisionPredicate;
        @Mutable
        @Shadow
        @Final
        private AbstractBlock.ContextPredicate postProcessPredicate;
        @Mutable
        @Shadow
        @Final
        private AbstractBlock.ContextPredicate emissiveLightingPredicate;
        @Mutable
        @Shadow
        @Final
        @Nullable
        private AbstractBlock.Offsetter offsetter;
        @Mutable
        @Shadow
        @Final
        private boolean blockBreakParticles;
        @Mutable
        @Shadow
        @Final
        private NoteBlockInstrument instrument;
        @Mutable
        @Shadow
        @Final
        private boolean replaceable;

        @Shadow
        public abstract Block getBlock();

        @Shadow
        protected abstract BlockState asBlockState();

        @Override
        public void chicory$updateSettings() {
            AbstractBlock.Settings settings = this.getBlock().getSettings();
            this.luminance = settings.luminance.applyAsInt(this.asBlockState());
            this.hasSidedTransparency = this.getBlock().hasSidedTransparency(this.asBlockState());
            this.isAir = settings.isAir;
            this.burnable = settings.burnable;
            this.liquid = settings.liquid;
            this.pistonBehavior = settings.pistonBehavior;
            this.mapColor = settings.mapColorProvider.apply(this.asBlockState());
            this.hardness = settings.hardness;
            this.toolRequired = settings.toolRequired;
            this.opaque = settings.opaque;
            this.solidBlockPredicate = settings.solidBlockPredicate;
            this.suffocationPredicate = settings.suffocationPredicate;
            this.blockVisionPredicate = settings.blockVisionPredicate;
            this.postProcessPredicate = settings.postProcessPredicate;
            this.emissiveLightingPredicate = settings.emissiveLightingPredicate;
            this.offsetter = settings.offsetter;
            this.blockBreakParticles = settings.blockBreakParticles;
            this.instrument = settings.instrument;
            this.replaceable = settings.replaceable;
        }
    }
}
