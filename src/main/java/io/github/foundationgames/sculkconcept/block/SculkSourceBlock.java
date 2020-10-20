package io.github.foundationgames.sculkconcept.block;

import com.google.common.collect.Lists;
import io.github.foundationgames.sculkconcept.SculkConcept;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Random;

public class SculkSourceBlock extends Block {
    public SculkSourceBlock(Settings settings) {
        super(settings);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if(!world.getBlockState(pos.down()).isOf(Blocks.STONE) && !world.getBlockState(pos.down()).isOf(SculkConcept.SCULK_GROWTH_BLOCK) ) return;
        if(!world.getBlockState(pos.down()).isOf(SculkConcept.SCULK_GROWTH_BLOCK)) {
            world.setBlockState(pos.down(), SculkConcept.SCULK_GROWTH_BLOCK.getDefaultState());
            return;
        }
        BlockPos.Mutable mpos = new BlockPos.Mutable();
        mpos.set(pos);
        mpos.set(mpos.add(random.nextInt(12) - 6, -1, random.nextInt(12) - 6));
        while(mpos.toImmutable().equals(pos.down())) {
            mpos.set(mpos.add(random.nextInt(12) - 6, -1, random.nextInt(12) - 6));
        }
        mpos.set(mpos.down(4));
        int i = 0;
        while(!world.getBlockState(mpos).isAir() && !world.getBlockState(mpos).isOf(SculkConcept.SCULK_GROWTH) && i < 8) {
            mpos.set(mpos.up());
            i++;
        }
        if(!world.getBlockState(mpos).isAir() && !world.getBlockState(mpos).isOf(SculkConcept.SCULK_GROWTH)) return;
        if(world.getBlockState(mpos).isOf(SculkConcept.SCULK_GROWTH)) {
            mpos.set(mpos.down());
            world.setBlockState(mpos, SculkConcept.SCULK_GROWTH_BLOCK.getDefaultState());
            world.setBlockState(mpos.up(), Blocks.AIR.getDefaultState());
            return;
        }
        if(world.getBlockState(mpos).isAir()) {
            mpos.set(mpos.down());
            if(
                world.getBlockState(mpos.north()).isOf(SculkConcept.SCULK_GROWTH_BLOCK) ||
                world.getBlockState(mpos.south()).isOf(SculkConcept.SCULK_GROWTH_BLOCK) ||
                world.getBlockState(mpos.east()).isOf(SculkConcept.SCULK_GROWTH_BLOCK) ||
                world.getBlockState(mpos.west()).isOf(SculkConcept.SCULK_GROWTH_BLOCK)
            ) {
                if(world.getBlockState(mpos).isOf(SculkConcept.SCULK_GROWTH_BLOCK)) return;
                world.setBlockState(mpos.up(), SculkConcept.SCULK_GROWTH.getDefaultState());
            }
        }
    }
}
