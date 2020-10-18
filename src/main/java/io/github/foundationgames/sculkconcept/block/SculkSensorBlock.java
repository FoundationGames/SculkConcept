package io.github.foundationgames.sculkconcept.block;

import io.github.foundationgames.sculkconcept.SculkConcept;
import io.github.foundationgames.sculkconcept.block.entity.SculkSensorBlockEntity;
import io.github.foundationgames.sculkconcept.particle.VibrationParticleEffect;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import java.util.Random;

public class SculkSensorBlock extends VibrationReceiverBlock implements BlockEntityProvider {
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final VoxelShape SHAPE = createCuboidShape(0, 0, 0, 16, 8, 16);

    public SculkSensorBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(POWERED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        /*world.setBlockState(pos, state.with(POWERED, !state.get(POWERED)));
        if(!state.get(POWERED)) {
            world.getBlockTickScheduler().schedule(pos, this, 30);
        }*/
        if(world.getBlockEntity(pos) instanceof SculkSensorBlockEntity) ((SculkSensorBlockEntity)world.getBlockEntity(pos)).vibrate();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        return state.get(POWERED) ? 15 : 0;
    }

    @Override
    public void onVibrationReceived(ServerWorld world, Vec3d vPos, BlockPos bpos, int radius) {
        //if(world.isClient()) world.addParticle(new VibrationParticleEffect(vPos.x, vPos.y, vPos.z, pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, 4), vPos.x, vPos.y, vPos.z, 0, 0, 0);
        Vec3d pos = new Vec3d(bpos.getX()+0.5, bpos.getY()+0.5, bpos.getZ()+0.5);
        for(PlayerEntity player : world.getPlayers()) ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, new ParticleS2CPacket(new VibrationParticleEffect(vPos.x, vPos.y, vPos.z, pos.getX(), pos.getY(), pos.getZ(), 4), true, vPos.x, vPos.y, vPos.z, 0f, 0f, 0f, 1f, 1));
        world.getBlockTickScheduler().schedule(bpos, this, SculkConcept.ticksOfDist(vPos, pos));
    }

    @Override
    public void onOccludedVibrationReceived(ServerWorld world, Vec3d vPos, Vec3d wPos, BlockPos bpos, int radius) {
        for(PlayerEntity player : world.getPlayers()) ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, new ParticleS2CPacket(new VibrationParticleEffect(vPos.x, vPos.y, vPos.z, wPos.getX(), wPos.getY(), wPos.getZ(), 4), true, vPos.x, vPos.y, vPos.z, 0f, 0f, 0f, 1f, 1));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new SculkSensorBlockEntity();
    }
}
