package io.github.foundationgames.sculkconcept.callback;

import com.sun.org.apache.xpath.internal.operations.Bool;
import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class BlockCallbacks {
    public static void blockStateChanged(World world, BlockState oldState, BlockState newState, BlockPos pos) {
        List<Boolean> cases = new ArrayList<>();

        cases.add(oldState.getBlock() instanceof TrapdoorBlock && newState.getBlock() instanceof TrapdoorBlock);
        cases.add(oldState.getBlock() instanceof DoorBlock && newState.getBlock() instanceof DoorBlock);
        cases.add(oldState.getBlock() instanceof LeverBlock && newState.getBlock() instanceof LeverBlock);
        cases.add(oldState.getBlock() instanceof AbstractButtonBlock && newState.getBlock() instanceof AbstractButtonBlock);
        cases.add(oldState.getBlock() instanceof AbstractPressurePlateBlock && newState.getBlock() instanceof AbstractPressurePlateBlock);
        cases.add(oldState.getBlock() instanceof PistonBlock && newState.getBlock() instanceof PistonBlock);
        cases.add(oldState.getBlock() instanceof FenceGateBlock && newState.getBlock() instanceof FenceGateBlock);

        for(boolean c : cases) {
            if(c) {
                vibration(world, pos, 88);
                return;
            }
        }
    }

    private static void vibration(World world, Vec3d pos, int radius) {
        if(!world.isClient()) {
            VibrationListenerState.get((ServerWorld)world).createVibration(new Vec3d(pos.getX(), pos.getY(), pos.getZ()), radius);
        }
    }
    private static void vibration(World world, BlockPos pos, int radius) {
        vibration(world, new Vec3d(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5), radius);
    }
}
