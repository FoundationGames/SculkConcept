package io.github.foundationgames.sculkconcept.util;

import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class MathUtil {
    public static Vec3i v3dTov3i(Vec3d vec) {
        return new Vec3i(Math.floor(vec.getX()), Math.floor(vec.getY()), Math.floor(vec.getZ()));
    }

    public static Vec3d getWoolPos(World world, Vec3d start, Vec3d end, int precision) {
        Vec3d length = start.subtract(end);
        Vec3d inc = length.multiply(-0.4 * (1f / length.length()));
        Vec3d pos = new Vec3d(0, 0, 0);
        while(pos.length() < length.length()) {
            pos = pos.add(inc);
            Vec3d apos = pos.add(start);
            //for(PlayerEntity player : world.getPlayers()) ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, new ParticleS2CPacket(new DustParticleEffect(0f, 0.5f, 1.0f, 1.0f), true, apos.x, apos.y, apos.z, 0f, 0f, 0f, 1f, 1));
            BlockPos p = new BlockPos(v3dTov3i(apos));
            if(world.getBlockState(p).getBlock().isIn(BlockTags.WOOL)) return apos;
        }
        return null;
    }

    public static boolean nearZero(double number, double precision) {
        return (number < Math.abs(precision) && number > -Math.abs(precision));
    }
}
