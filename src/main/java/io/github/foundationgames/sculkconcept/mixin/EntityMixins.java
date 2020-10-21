package io.github.foundationgames.sculkconcept.mixin;

import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class EntityMixins {
    @Mixin(LivingEntity.class)
    public abstract static class LivingEntityMixin {
        private boolean wasGrounded = false;
        private int walkTick = 0;
        private Vec3d lastPos = ((LivingEntity)(Object)this).getPos();

        @Inject(method = "tick", at = @At("TAIL"))
        public void stepVibrations(CallbackInfo ci) {
            if(!((LivingEntity)(Object)this).isSneaking()) {
                if(this.wasGrounded != ((LivingEntity)(Object)this).isOnGround()) {
                    if(((LivingEntity)(Object)this).isOnGround()) {
                        vibration(72);
                    }
                }
                Vec3d vel = ((LivingEntity)(Object)this).getVelocity();
                if((LivingEntity)(Object)this instanceof ServerPlayerEntity) vel = lastPos.subtract(((LivingEntity)(Object)this).getPos());
                if(vel.getX() != 0 && vel.getZ() != 0 && ((LivingEntity)(Object)this).isOnGround()) {
                    walkTick++;
                    if(walkTick > 6) {
                        walkTick = 0;
                        vibration(60);
                    }
                }
            }
            this.wasGrounded = ((LivingEntity)(Object)this).isOnGround();
            this.lastPos = ((LivingEntity)(Object)this).getPos();
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
            if(!((ProjectileEntity)(Object)this instanceof PersistentProjectileEntity)) {
                World world = ((ProjectileEntity)(Object)this).world;
                Vec3d pos = ((ProjectileEntity)(Object)this).getPos();
                if(!world.isClient()) {
                    VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), 69);
                }
            }
        }
    }

    @Mixin(PersistentProjectileEntity.class)
    public static class PersistentProjectileEntityMixin {
        @Inject(method = "<init>(Lnet/minecraft/entity/EntityType;DDDLnet/minecraft/world/World;)V", at = @At("TAIL"))
        public void spawnVibration(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, CallbackInfo ci) {
            Vec3d pos = ((ProjectileEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), 69);
            }
        }

        @Inject(method = "onBlockHit", at = @At("TAIL"))
        public void blockVibration(BlockHitResult blockHitResult, CallbackInfo ci) {
            World world = ((ProjectileEntity)(Object)this).world;
            Vec3d pos = ((ProjectileEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), 69);
            }
        }

        @Inject(method = "onEntityHit", at = @At("TAIL"))
        public void entityVibration(EntityHitResult entityHitResult, CallbackInfo ci) {
            World world = ((ProjectileEntity)(Object)this).world;
            Vec3d pos = ((ProjectileEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), 69);
            }
        }
    }

    @Mixin(LightningEntity.class)
    public static class LightningEntityMixin {
        @Inject(method = "tick", at = @At("HEAD"))
        public void vibrateLightningStrike(CallbackInfo ci) {
            World world = ((LightningEntity)(Object)this).world;
            Vec3d pos = ((LightningEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), 220);
            }
        }
    }
}
