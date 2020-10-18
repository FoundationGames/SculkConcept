package io.github.foundationgames.sculkconcept.mixin;

import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class EntityMixins {
    @Mixin(LivingEntity.class)
    public abstract static class LivingEntityMixin {
        private boolean wasGrounded = false;
        private int walkTick = 0;

        @Inject(method = "tick", at = @At("TAIL"))
        public void stepVibrations(CallbackInfo ci) {
            if(!((LivingEntity)(Object)this).isSneaking() && !((LivingEntity)(Object)this instanceof ServerPlayerEntity)) {
                if(this.wasGrounded != ((LivingEntity)(Object)this).isOnGround()) {
                    this.wasGrounded = ((LivingEntity)(Object)this).isOnGround();
                    if(((LivingEntity)(Object)this).isOnGround()) {
                        vibration(72);
                    }
                }
                Vec3d vel = ((LivingEntity)(Object)this).getVelocity();
                if(vel.getX() != 0 && vel.getZ() != 0 && ((LivingEntity)(Object)this).isOnGround()) {
                    walkTick++;
                    if(walkTick > 6) {
                        walkTick = 0;
                        vibration(50);
                    }
                }
            }
        }

        private void vibration(int radius) {
            World world = ((LivingEntity)(Object)this).getEntityWorld();
            Vec3d pos = ((LivingEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), radius);
            }
        }
    }

    @Mixin(ProjectileEntity.class)
    public static class ProjectileEntityMixin {
        @Inject(method = "onCollision", at = @At("TAIL"))
        public void vibration(HitResult hitResult, CallbackInfo ci) {
            World world = ((ProjectileEntity)(Object)this).world;
            Vec3d pos = ((ProjectileEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), 69);
            }
        }
    }

    @Mixin(ServerPlayerEntity.class)
    public abstract static class ServerPlayerEntityMixin {
        private boolean wasGrounded = false;
        private int walkTick = 0;

        @Inject(method = "tick", at = @At("TAIL"))
        public void stepVibrations(CallbackInfo ci) {
            if(!((ServerPlayerEntity)(Object)this).isSneaking()) {
                if(this.wasGrounded != ((ServerPlayerEntity)(Object)this).isOnGround()) {
                    this.wasGrounded = ((ServerPlayerEntity)(Object)this).isOnGround();
                    if(((ServerPlayerEntity)(Object)this).isOnGround()) {
                        vibration(72);
                    }
                }
                Vec3d vel = ((ServerPlayerEntity)(Object)this).getVelocity();
                if(vel.getX() != 0 && vel.getZ() != 0 && ((ServerPlayerEntity)(Object)this).isOnGround()) {
                    walkTick++;
                    if(walkTick > 6) {
                        walkTick = 0;
                        vibration(50);
                    }
                }
            }
        }

        private void vibration(int radius) {
            World world = ((ServerPlayerEntity)(Object)this).getEntityWorld();
            Vec3d pos = ((ServerPlayerEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), radius);
            }
        }
    }
}
