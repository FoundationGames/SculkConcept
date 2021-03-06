package io.github.foundationgames.sculkconcept.particle;

import io.github.foundationgames.sculkconcept.SculkConcept;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class VibrationParticle extends SpriteBillboardParticle {
    private final Vec3d start;
    private final Vec3d end;
    private int progress;
    private final int duration;
    private int stage;
    private final int stages;
    private final double dist;

    public VibrationParticle(ClientWorld clientWorld, Vec3d start, Vec3d end, int stages) {
        super(clientWorld, start.getX(), start.getY(), start.getZ());
        this.start = start;
        this.end = end;
        this.stage = stages;
        this.stages = Math.min(4, Math.max(stages, 1));
        this.duration = SculkConcept.ticksOfDist(start, end);
        this.maxAge = duration;
        this.dist = start.distanceTo(end);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public void tick() {
        super.tick();

        progress++;

        Vec3d pos = position((float)progress / duration);
        if(pos.distanceTo(end) < 0.05) { markDead(); return; }

        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();

        this.stage = 5 - Math.round(delta((float) progress / duration) * stages);
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d dir = start.subtract(end);
        float yaw = (float)Math.toDegrees(Math.atan(dir.getZ() / dir.getX()));
        //float pitch = (float)Math.toDegrees(Math.atan(dir.getY() / dir.getX()));
        float prog = ((progress + tickDelta) / duration);
        Vec3d pos = position(prog);
        float tilt = (delta(pos) - 0.5f) * 64;
        boolean below = camera.getPos().subtract(pos).y < 0;

        MatrixStack matrices = new MatrixStack();

        matrices.push();

        matrices.translate(pos.x - camera.getPos().x, pos.y - camera.getPos().y, pos.z - camera.getPos().z);
        matrices.scale(1f/18f, 1f/18f, 1f/18f);
        int a = (dir.getX() < 0 ? 90 : -90);
        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw + a));
        matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(tilt));
        matrices.translate(-8, 0, -8);

        VertexConsumerProvider.Immediate i = MinecraftClient.getInstance().getBufferBuilders().getEffectVertexConsumers();
        VertexConsumer consumer = i.getBuffer(RenderLayer.getBeaconBeam(SculkConcept.id("textures/particle/vibration_particle_stages.png"), true));

        int u = (Math.min(3, stage - 1)) * 16;
        if(below) {
            matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180));
            matrices.translate(-16, 0, -3);
        }
        flatQuad(consumer, matrices, new Vec3d(0, 0, 0), 16, 16, 48 - u, 0, 16, 16, 48, 16);
        i.draw();

        matrices.pop();
    }

    private Vec3d position(float delta) {
        double d = MathHelper.lerp(delta, start.x, end.x);
        double e = MathHelper.lerp(delta, start.y, end.y);
        double f = MathHelper.lerp(delta, start.z, end.z);
        return new Vec3d(d, e, f);
    }

    private float delta(float ticks) {
        double d2 = start.distanceTo(position(ticks));
        return (float) (d2 / dist);
    }

    private float delta(Vec3d pos) {
        double d2 = start.distanceTo(pos);
        return (float) (d2 / dist);
    }

    private void flatQuad(VertexConsumer buffer, MatrixStack matrices, Vec3d pos, int width, int height, int u, int v, int uw, int vh, int texW, int texH) {
        float u0 = (float)u / (float)texW;
        float u1 = ((float)u + (float)uw) / (float)texW;
        float v0 = (float)v / (float)texH;
        float v1 = ((float)v + (float)vh) / (float)texH;
        Matrix4f matrix = matrices.peek().getModel();
        int light = 15728864;
        buffer.vertex(matrix, (float)pos.getX(), (float)pos.getY(), (float)pos.getZ()).color(1.0f, 1.0f, 1.0f, 1.0f).texture(u0, v0).overlay(1000).light(light).normal((float)pos.getX(), (float)pos.getY(), (float)pos.getZ()).next();
        buffer.vertex(matrix, (float)pos.getX()+width, (float)pos.getY(), (float)pos.getZ()).color(1.0f, 1.0f, 1.0f, 1.0f).texture(u1, v0).overlay(1000).light(light).normal((float)pos.getX()+width, (float)pos.getY(), (float)pos.getZ()).next();
        buffer.vertex(matrix, (float)pos.getX()+width, (float)pos.getY(), (float)pos.getZ()+height).color(1.0f, 1.0f, 1.0f, 1.0f).texture(u1, v1).overlay(1000).light(light).normal((float)pos.getX()+width, (float)pos.getY(), (float)pos.getZ()+height).next();
        buffer.vertex(matrix, (float)pos.getX(), (float)pos.getY(), (float)pos.getZ()+height).color(1.0f, 1.0f, 1.0f, 1.0f).texture(u0, v1).overlay(1000).light(light).normal((float)pos.getX(), (float)pos.getY(), (float)pos.getZ()+height).next();
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<VibrationParticleEffect> {
        public Factory(SpriteProvider spriteProvider) {}

        @Override
        public Particle createParticle(VibrationParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new VibrationParticle(world, parameters.start, parameters.end, parameters.stages);
        }
    }
}
