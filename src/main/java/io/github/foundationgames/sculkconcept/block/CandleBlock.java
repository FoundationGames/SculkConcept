package io.github.foundationgames.sculkconcept.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Random;

public class CandleBlock extends Block {
    public static final VoxelShape ONE_SHAPE = createCuboidShape(6, 0, 6, 10, 8, 10);
    public static final VoxelShape TWO_SHAPE = createCuboidShape(5, 0, 5, 11, 8, 11);
    public static final VoxelShape THREE_FOUR_SHAPE = createCuboidShape(4, 0, 4, 12, 8, 12);

    public static final IntProperty COUNT = IntProperty.of("count", 1, 4);

    public CandleBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(COUNT, 1));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if(state.isOf(this)) return getDefaultState().with(COUNT, Math.min(state.get(COUNT)+1, 4));
        return super.getPlacementState(ctx);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        if(world.isClient()) this.randomDisplayTick(state, world, pos, new Random());
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return !world.getBlockState(pos.down()).getCollisionShape(world, pos).getFace(Direction.UP).isEmpty();
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext ctx) {
        return ctx.getStack().getItem() == this.asItem() && state.get(COUNT) < 4;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if(state.get(COUNT) == 1) {
            flame(world, pos, 8, 7, 8);
        } else if(state.get(COUNT) == 2) {
            flame(world, pos, 7, 8, 6);
            flame(world, pos, 9, 7, 10);
        } else if(state.get(COUNT) == 3) {
            flame(world, pos, 7, 8, 7);
            flame(world, pos, 8, 7, 10);
            flame(world, pos, 10, 5, 8);
        } else if(state.get(COUNT) == 4) {
            flame(world, pos, 7, 8, 7);
            flame(world, pos, 7, 7, 10);
            flame(world, pos, 10, 5, 9);
            flame(world, pos, 10, 7, 6);
        }
    }

    private void flame(World world, BlockPos pos, int x, int y, int z) {
        world.addParticle(ParticleTypes.FLAME, pos.getX() + ((float)x / 16), pos.getY() + ((float)y / 16) /*- 0.03125f*/, pos.getZ() + ((float)z / 16), 0.0D, 0.0D, 0.0D);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
        if(world.getBlockState(pos.down()).getCollisionShape(world, pos).getFace(Direction.UP).isEmpty()) world.breakBlock(pos, true);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(COUNT) == 1 ? ONE_SHAPE : state.get(COUNT) == 2 ? TWO_SHAPE : THREE_FOUR_SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(COUNT);
    }
}
