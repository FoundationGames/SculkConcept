package io.github.foundationgames.sculkconcept.block.entity;

import io.github.foundationgames.sculkconcept.SculkConcept;
import io.github.foundationgames.sculkconcept.block.SculkSensorBlock;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Tickable;

public class SculkSensorBlockEntity extends BlockEntity implements Tickable, BlockEntityClientSerializable {
    private boolean powered = false;
    private int animationProgress = 0;
    public final int animationLength;

    public SculkSensorBlockEntity() {
        super(SculkConcept.SCULK_SENSOR_ENTITY);
        animationLength = 15;
    }

    public int getAnimationProgress() {
        return (animationLength + 1) - animationProgress;
    }

    public boolean isPowered() {
        return powered;
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        powered = tag.getBoolean("powered");
        animationProgress = tag.getInt("animationProgress");
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.putBoolean("powered", powered);
        tag.putInt("animationProgress", animationProgress);
        return tag;
    }

    @Override
    public void tick() {
        if(animationProgress > 0) animationProgress--;
        if(world.getBlockState(pos).get(SculkSensorBlock.POWERED) != powered) {
            powered = world.getBlockState(pos).get(SculkSensorBlock.POWERED);
            if(powered) {
                animationProgress = animationLength;
                if(!world.isClient()) for(PlayerEntity player : world.getPlayers()) ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, new PlaySoundS2CPacket(SoundEvents.ENTITY_STRIDER_AMBIENT, SoundCategory.BLOCKS, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 1.0f, 1.0f));
                //world.playSound(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, SoundEvents.ENTITY_STRIDER_AMBIENT, SoundCategory.BLOCKS, 1.0f, 1.0f, false);
            } else {
                if(!world.isClient()) for(PlayerEntity player : world.getPlayers()) ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, new PlaySoundS2CPacket(SoundEvents.BLOCK_NETHER_WART_BREAK, SoundCategory.BLOCKS, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 0.35f, 0.5f));
            }
        }
    }

    //--------------

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(world.getBlockState(pos), compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        return toTag(compoundTag);
    }
}
