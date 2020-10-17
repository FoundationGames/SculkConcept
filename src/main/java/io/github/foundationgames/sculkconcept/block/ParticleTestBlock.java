package io.github.foundationgames.sculkconcept.block;

import io.github.foundationgames.sculkconcept.particle.VibrationParticleEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ParticleTestBlock extends Block {
    public ParticleTestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(world.isClient()) {
            world.addParticle(new VibrationParticleEffect(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, player.getX(), player.getY(), player.getZ(), 4), pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, 0, 0, 0);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }
}
