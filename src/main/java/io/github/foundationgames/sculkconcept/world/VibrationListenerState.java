package io.github.foundationgames.sculkconcept.world;

import io.github.foundationgames.sculkconcept.Util.MathUtil;
import io.github.foundationgames.sculkconcept.block.VibrationReceiverBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.PersistentState;
import net.minecraft.world.RaycastContext;

import java.util.ArrayList;
import java.util.List;

public class VibrationListenerState extends PersistentState {
    private final List<Long> positions = new ArrayList<>();
    private final ServerWorld world;

    public VibrationListenerState(ServerWorld world) {
        super("vibration_listeners");
        this.world = world;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        positions.clear();
        long[] larr = tag.getLongArray("Positions");
        for(long l : larr) positions.add(l);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        long[] arr = new long[positions.size()];
        for (int i = 0; i < positions.size(); i++) arr[i] = positions.get(i);
        tag.putLongArray("Positions", arr);
        return tag;
    }

    public void add(BlockPos pos) {
        if(!positions.contains(pos)) positions.add(pos.asLong());
        setDirty(true);
    }
    public void remove(BlockPos pos) {
        positions.remove(pos.asLong());
        setDirty(true);
    }
    public List<Long> getAll() {
        return positions;
    }
    public void createVibration(Vec3d pos, int squaredRadius) {
        BlockPos.Mutable m = new BlockPos.Mutable();
        for(long l : positions) {
            m.set(l);
            Vec3d mvec = new Vec3d(m.getX()+0.5, m.getY()+0.5, m.getZ()+0.5);
            if(pos.squaredDistanceTo(mvec) <= squaredRadius && world.getBlockState(m).getBlock() instanceof VibrationReceiverBlock) {
                VibrationReceiverBlock reciever = (VibrationReceiverBlock)world.getBlockState(m).getBlock();
                Vec3d wpos = MathUtil.getWoolPos(world, pos, mvec, 0);
                if(wpos == null) reciever.onVibrationReceived(world, pos, m, squaredRadius);
                else reciever.onOccludedVibrationReceived(world, pos, wpos, m, squaredRadius);
                //BlockHitResult h = world.raycastBlock(pos, new Vec3d(m.getX()+0.5, m.getY()+0.5, m.getZ()+0.5), m.toImmutable(), VoxelShapes.UNBOUNDED, world.getBlockState(m.toImmutable()));
            }
        }
    }

    public static VibrationListenerState get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(() -> new VibrationListenerState(world), "vibration_listeners");
    }
}
