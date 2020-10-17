package io.github.foundationgames.sculkconcept.block;

import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class VibrationRecieverBlock extends Block {
    public VibrationRecieverBlock(Settings settings) {
        super(settings);
    }

    public abstract void onVibrationRecieved(ServerWorld world, Vec3d vibrationPos, BlockPos pos, int radius);

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if(!state.isOf(oldState.getBlock()) && !world.isClient()) VibrationListenerState.get((ServerWorld)world).add(pos);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        super.onStateReplaced(state, world, pos, newState, moved);
        if(!state.isOf(newState.getBlock()) && !world.isClient()) VibrationListenerState.get((ServerWorld)world).add(pos);
    }
}
