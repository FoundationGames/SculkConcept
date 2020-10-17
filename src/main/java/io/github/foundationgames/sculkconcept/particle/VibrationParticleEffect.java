package io.github.foundationgames.sculkconcept.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.foundationgames.sculkconcept.SculkConceptClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.Vec3d;

public class VibrationParticleEffect implements ParticleEffect {

    public final Vec3d start;
    public final Vec3d end;
    public final int stages;

    public static final Codec<VibrationParticleEffect> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.DOUBLE.fieldOf("sx").forGetter((particleEffect) -> particleEffect.start.x),
            Codec.DOUBLE.fieldOf("sy").forGetter((particleEffect) -> particleEffect.start.y),
            Codec.DOUBLE.fieldOf("sz").forGetter((particleEffect) -> particleEffect.start.z),
            Codec.DOUBLE.fieldOf("ex").forGetter((particleEffect) -> particleEffect.end.x),
            Codec.DOUBLE.fieldOf("ey").forGetter((particleEffect) -> particleEffect.end.y),
            Codec.DOUBLE.fieldOf("ez").forGetter((particleEffect) -> particleEffect.end.z),
            Codec.INT.fieldOf("stages").forGetter((particleEffect) -> particleEffect.stages))
    .apply(instance, VibrationParticleEffect::new));

    public VibrationParticleEffect(double sx, double sy, double sz, double ex, double ey, double ez, int stages){
        this.start = new Vec3d(sx, sy, sz);
        this.end = new Vec3d(ex, ey, ez);
        this.stages = stages;
    }

    @Override
    public ParticleType<?> getType() {
        return SculkConceptClient.VIBRATION_PARTICLE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeDouble(this.start.x);
        buf.writeDouble(this.start.y);
        buf.writeDouble(this.start.z);
        buf.writeDouble(this.end.x);
        buf.writeDouble(this.end.y);
        buf.writeDouble(this.end.z);
        buf.writeInt(this.stages);
    }

    @Override
    public String asString() {
        return "VibrationParticle {} i was too lazy to make an asString";
    }

    public static final Factory<VibrationParticleEffect> FACTORY = new Factory<VibrationParticleEffect>() {

        @Override
        public VibrationParticleEffect read(ParticleType<VibrationParticleEffect> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            float sx = (float)reader.readDouble();
            reader.expect(' ');
            float sy = (float)reader.readDouble();
            reader.expect(' ');
            float sz = (float)reader.readDouble();
            reader.expect(' ');
            float ex = (float)reader.readDouble();
            reader.expect(' ');
            float ey = (float)reader.readDouble();
            reader.expect(' ');
            float ez = (float)reader.readDouble();
            reader.expect(' ');
            int stages = reader.readInt();
            return new VibrationParticleEffect(sx, sy, sz, ex, ey, ez, stages);
        }

        @Override
        public VibrationParticleEffect read(ParticleType<VibrationParticleEffect> type, PacketByteBuf buf) {
            return new VibrationParticleEffect(
                    buf.readDouble(), buf.readDouble(), buf.readDouble(),
                    buf.readDouble(), buf.readDouble(), buf.readDouble(), buf.readInt()
            );
        }
    };

    @Environment(EnvType.CLIENT)
    public Vec3d getStartPos() {
        return start;
    }

    @Environment(EnvType.CLIENT)
    public Vec3d getEndPos() {
        return end;
    }
}
