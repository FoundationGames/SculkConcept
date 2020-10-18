package io.github.foundationgames.sculkconcept.mixin;

import io.github.foundationgames.sculkconcept.callback.BlockCallbacks;
import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BlockMixins {
    @Mixin(World.class)
    public static abstract class WorldMixin {
        @Shadow public abstract BlockState getBlockState(BlockPos pos);

        @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"))
        public void vibrateBlocks(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
            BlockState old = this.getBlockState(pos);
            BlockCallbacks.blockStateChanged((World)(Object)this, old, state, pos);
        }
    }

    @Mixin(BlockItem.class)
    public static class BlockItemMixin {
        @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"))
        public void vibratePlacement(ItemPlacementContext ctx, CallbackInfoReturnable<ActionResult> cir) {
            if(ctx.canPlace() && !ctx.getWorld().isClient()) VibrationListenerState.get((ServerWorld)ctx.getWorld()).createVibration(new Vec3d(ctx.getBlockPos().getX()+0.5, ctx.getBlockPos().getY()+0.5, ctx.getBlockPos().getZ()+0.5), 100);
        }
    }
}
