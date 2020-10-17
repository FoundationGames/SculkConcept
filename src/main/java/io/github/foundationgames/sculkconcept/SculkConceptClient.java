package io.github.foundationgames.sculkconcept;

import io.github.foundationgames.sculkconcept.block.entity.render.SculkSensorBlockEntityRenderer;
import io.github.foundationgames.sculkconcept.particle.VibrationParticle;
import io.github.foundationgames.sculkconcept.particle.VibrationParticleEffect;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

public class SculkConceptClient implements ClientModInitializer {

    public static final ParticleType<VibrationParticleEffect> VIBRATION_PARTICLE = FabricParticleTypes.complex(VibrationParticleEffect.FACTORY);

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(SculkConcept.SCULK_SENSOR_ENTITY, SculkSensorBlockEntityRenderer::new);
        Registry.register(Registry.PARTICLE_TYPE, SculkConcept.id("vibration"), VIBRATION_PARTICLE);
        ParticleFactoryRegistry.getInstance().register(VIBRATION_PARTICLE, VibrationParticle.Factory::new);
    }
}
