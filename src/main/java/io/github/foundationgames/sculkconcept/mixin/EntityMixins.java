package io.github.foundationgames.sculkconcept.mixin;

import io.github.foundationgames.sculkconcept.callback.ItemCallbacks;
import io.github.foundationgames.sculkconcept.util.MathUtil;
import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.minecraft.block.RailBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
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
        private double walkTick = 0;
        private Vec3d lastPos = ((LivingEntity)(Object)this).getPos();

        @Inject(method = "tick", at = @At("TAIL"))
        public void stepVibrations(CallbackInfo ci) {
            if(!((LivingEntity)(Object)this).isSneaking()) {
                if(this.wasGrounded != ((LivingEntity)(Object)this).isOnGround()) {
                    if(((LivingEntity)(Object)this).isOnGround()) {
                        vibration(72);
                        this.walkTick = 0;
                    }
                }
                Vec3d vel = ((LivingEntity)(Object)this).getVelocity();
                if((LivingEntity)(Object)this instanceof ServerPlayerEntity) vel = lastPos.subtract(((LivingEntity)(Object)this).getPos());
                if(vel.getX() != 0 && vel.getZ() != 0 && ((LivingEntity)(Object)this).isOnGround()) {
                    walkTick += (vel.length() * 4.5);
                    if(walkTick >= 7) {
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
            if(!((ProjectileEntity)(Object)this instanceof PersistentProjectileEntity) && !((ProjectileEntity)(Object)this instanceof FishingBobberEntity)) {
                World world = ((ProjectileEntity)(Object)this).world;
                Vec3d pos = ((ProjectileEntity)(Object)this).getPos();
                if(!world.isClient()) {
                    VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), 69);
                }
            }
        }

        @Inject(method = "onBlockHit", at = @At("TAIL"))
        public void fishingBobber(BlockHitResult blockHitResult, CallbackInfo ci) {
            if(((ProjectileEntity)(Object)this instanceof FishingBobberEntity)) {
                World world = ((ProjectileEntity)(Object)this).world;
                Vec3d pos = ((ProjectileEntity)(Object)this).getPos();
                if(!world.isClient()) {
                    VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), 67);
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

    @Mixin(ServerPlayerEntity.class)
    public static class ServerPlayerEntityMixin {
        @Inject(method = "tick", at = @At("TAIL"))
        public void vibration(CallbackInfo ci) {
            ServerPlayerEntity self = ((ServerPlayerEntity)(Object)this);
            if(self.isUsingItem() && self.getItemUseTimeLeft() > 0) {
                Hand active = self.getActiveHand();
                ItemCallbacks.itemUsing(self, self.getEntityWorld(), active, self.getStackInHand(active), self.getItemUseTime(), self.getItemUseTimeLeft());
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

    @Mixin(AbstractMinecartEntity.class)
    public static class AbstractMinecartEntityMixin {
        private boolean wasGrounded = false;
        private int moveTick = 0;

        @Inject(method = "tick", at = @At("HEAD"))
        public void vibrateMinecartMove(CallbackInfo ci) {
            AbstractMinecartEntity self = ((AbstractMinecartEntity)(Object)this);
            boolean ground = (self.isOnGround() || self.world.getBlockState(self.getBlockPos()).getBlock() instanceof RailBlock);
            if(this.wasGrounded != ground) {
                if(ground) {
                    vibration(88);
                }
            }
            Vec3d vel = self.getVelocity();
            if(!MathUtil.nearZero(vel.getX(), 0.02) && !MathUtil.nearZero(vel.getZ(), 0.02) && ground) {
                moveTick++;
                if(moveTick > 4) {
                    moveTick = 0;
                    vibration(76);
                }
            }
            this.wasGrounded = ground;
        }

        private void vibration(int radius) {
            World world = ((AbstractMinecartEntity)(Object)this).getEntityWorld();
            Vec3d pos = ((AbstractMinecartEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), radius);
            }
        }
    }

    @Mixin(BoatEntity.class)
    public static class BoatEntityMixin {
        private boolean wasGrounded = false;
        private int moveTick = 0;
        private Vec3d lastPos = ((BoatEntity)(Object)this).getPos();

        @Inject(method = "tick", at = @At("HEAD"))
        public void vibrateBoatMove(CallbackInfo ci) {
            if(this.wasGrounded != ((BoatEntity)(Object)this).isOnGround()) {
                if(((BoatEntity)(Object)this).isOnGround()) {
                    vibration(88);
                }
            }
            Vec3d vel = ((BoatEntity)(Object)this).getVelocity();
            if(!((BoatEntity)(Object)this).world.isClient()) vel = lastPos.subtract(((BoatEntity)(Object)this).getPos());
            if(vel.getX() != 0 && vel.getZ() != 0) {
                moveTick++;
                if(moveTick > 9) {
                    moveTick = 0;
                    vibration(69);
                }
            }
            this.wasGrounded = ((BoatEntity)(Object)this).isOnGround();
            this.lastPos = ((BoatEntity)(Object)this).getPos();
        }

        private void vibration(int radius) {
            World world = ((BoatEntity)(Object)this).getEntityWorld();
            Vec3d pos = ((BoatEntity)(Object)this).getPos();
            if(!world.isClient()) {
                VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), radius);
            }
        }
    }
}
