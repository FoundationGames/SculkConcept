package io.github.foundationgames.sculkconcept.block;

import io.github.foundationgames.sculkconcept.SculkConcept;
import io.github.foundationgames.sculkconcept.block.entity.SculkSensorBlockEntity;
import io.github.foundationgames.sculkconcept.particle.VibrationParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

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
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if(state.get(POWERED)) createRedstoneParticles(world, pos);
    }

    @Environment(EnvType.CLIENT)
    private void createRedstoneParticles(World world, BlockPos pos) {
        float a, b, c, d;
        Random rand = new Random();
        float scale = 0.66f;
        for(PlayerEntity player : world.getPlayers()) {
            a = rand.nextFloat();
            b = rand.nextFloat();
            c = rand.nextFloat();
            d = rand.nextFloat();
            world.addParticle(new DustParticleEffect(0.83f, 0.0f, 0.0f, scale), pos.getX() + a, pos.getY() + 0.06f + ((a + b) / 4), pos.getZ() - 0.07, 0, 0, 0);
            world.addParticle(new DustParticleEffect(0.83f, 0.0f, 0.0f, scale), pos.getX() + b, pos.getY() + 0.06f + ((b + c) / 4), pos.getZ() + 1.07, 0, 0, 0);
            world.addParticle(new DustParticleEffect(0.83f, 0.0f, 0.0f, scale), pos.getX() - 0.07, pos.getY() + 0.06f + ((c + d) / 4), pos.getZ() + c, 0, 0, 0);
            world.addParticle(new DustParticleEffect(0.83f, 0.0f, 0.0f, scale), pos.getX() + 1.07, pos.getY() + 0.06f + ((d + a) / 4), pos.getZ() + d, 0, 0, 0);
        }
    }

    @Override
    public void onVibrationReceived(ServerWorld world, Vec3d vPos, BlockPos bpos, int radius) {
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
