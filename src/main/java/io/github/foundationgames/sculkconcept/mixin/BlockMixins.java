package io.github.foundationgames.sculkconcept.mixin;

import io.github.foundationgames.sculkconcept.callback.BlockCallbacks;
import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BellBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BlockMixins {
    @Mixin(World.class)
    public static abstract class WorldMixin {
        @Shadow public abstract BlockState getBlockState(BlockPos pos);
        @Shadow public abstract boolean isClient();

        @Inject(method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z", at = @At("HEAD"))
        public void vibrateBlocks(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
            BlockState old = this.getBlockState(pos);
            BlockCallbacks.blockStateChanged((World)(Object)this, old, state, pos);
        }

        @Inject(method = "breakBlock", at = @At("HEAD"))
        public void vibrateBreak(BlockPos pos, boolean drop, Entity breakingEntity, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
            if(!this.getBlockState(pos).isAir() && !this.getBlockState(pos).getBlock().isIn(BlockTags.WOOL) && !this.isClient()) VibrationListenerState.get((ServerWorld)(Object)this).createVibration(new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5), 100);
        }
    }

    @Mixin(BlockItem.class)
    public static class BlockItemMixin {
        @Inject(method = "postPlacement", at = @At("HEAD"))
        public void vibratePlacement(BlockPos pos, World world, PlayerEntity player, ItemStack stack, BlockState state, CallbackInfoReturnable<Boolean> cir) {
            if(!world.isClient()) VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5), 100);
        }
    }

    @Mixin(PistonBlock.class)
    public static class PistonBlockMixin {
        @Inject(method = "onSyncedBlockEvent", at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V",
                shift = At.Shift.AFTER,
                ordinal = 0
        ))
        public void vibrateExtension(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
            if(!world.isClient()) VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5), 100);
        }

        @Inject(method = "onSyncedBlockEvent", at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V",
                shift = At.Shift.AFTER,
                ordinal = 1
        ))
        public void vibrateRetraction(BlockState state, World world, BlockPos pos, int type, int data, CallbackInfoReturnable<Boolean> cir) {
            if(!world.isClient()) VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5), 100);
        }
    }

    @Mixin(Explosion.class)
    public static class ExplosionMixin {
        @Shadow @Final private float power;
        @Shadow @Final private World world;
        @Shadow @Final private double y;
        @Shadow @Final private double x;
        @Shadow @Final private double z;

        @Inject(method = "affectWorld", at = @At("HEAD"))
        public void vibrateWorld(boolean bl, CallbackInfo ci) {
            Vec3d pos = new Vec3d(this.x, this.y, this.z);
            if(!world.isClient()) VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), (int)(50 * power));
        }
    }

    @Mixin(BellBlock.class)
    public static class BellBlockMixin {
        @Inject(method = "ring(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", at = @At("HEAD"))
        public void vibrateBell(World world, BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
            if(!world.isClient()) VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5), 75);
        }
    }
}
