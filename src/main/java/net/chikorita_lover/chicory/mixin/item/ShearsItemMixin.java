package net.chikorita_lover.chicory.mixin.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.chikorita_lover.chicory.registry.tag.ChicoryBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.component.type.ToolComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ShearsItem.class)
public class ShearsItemMixin {
    @ModifyExpressionValue(method = "createToolComponent", at = @At(value = "INVOKE", target = "Ljava/util/List;of(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/List;"))
    private static List<ToolComponent.Rule> appendRules(List<ToolComponent.Rule> list) {
        List<ToolComponent.Rule> rules = new ArrayList<>(list);
        rules.add(ToolComponent.Rule.of(ChicoryBlockTags.SHEARS_MINEABLE, 5.0F));
        return rules;
    }

    @ModifyReturnValue(method = "postMine", at = @At("RETURN"))
    private boolean isUsed(boolean used, ItemStack stack, World world, BlockState state) {
        return used || state.isIn(ChicoryBlockTags.SHEARS_MINEABLE);
    }
}
