package io.github.foundationgames.sculkconcept;

import com.google.common.collect.Maps;
import com.swordglowsblue.artifice.api.Artifice;
import io.github.foundationgames.sculkconcept.block.entity.render.SculkSensorBlockEntityRenderer;
import io.github.foundationgames.sculkconcept.particle.VibrationParticle;
import io.github.foundationgames.sculkconcept.particle.VibrationParticleEffect;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SculkConceptClient implements ClientModInitializer {

    public static final ParticleType<VibrationParticleEffect> VIBRATION_PARTICLE = FabricParticleTypes.complex(VibrationParticleEffect.FACTORY);

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(SculkConcept.SCULK_SENSOR_ENTITY, SculkSensorBlockEntityRenderer::new);
        BlockRenderLayerMap.INSTANCE.putBlock(SculkConcept.SCULK_GROWTH, RenderLayer.getCutout());
        Registry.register(Registry.PARTICLE_TYPE, SculkConcept.id("vibration"), VIBRATION_PARTICLE);
        ParticleFactoryRegistry.getInstance().register(VIBRATION_PARTICLE, VibrationParticle.Factory::new);
        registerAssets();
    }

    private static void registerAssets() {
        Artifice.registerAssets(SculkConcept.id("sculk_concept_assets"), pack -> {
            for(DyeColor color : DyeColor.values()) {
                String cs = color.getName();
                System.out.println(cs);
                pack.addBlockModel(SculkConcept.id(cs+"_candles_single"), builder -> builder.parent(SculkConcept.id("block/candles_single_base")).texture("candle", SculkConcept.id("block/"+cs+"_candle")));
                pack.addBlockModel(SculkConcept.id(cs+"_candles_double"), builder -> builder.parent(SculkConcept.id("block/candles_double_base")).texture("candle", SculkConcept.id("block/"+cs+"_candle")));
                pack.addBlockModel(SculkConcept.id(cs+"_candles_triple"), builder -> builder.parent(SculkConcept.id("block/candles_triple_base")).texture("candle", SculkConcept.id("block/"+cs+"_candle")));
                pack.addBlockModel(SculkConcept.id(cs+"_candles_quadruple"), builder -> builder.parent(SculkConcept.id("block/candles_quadruple_base")).texture("candle", SculkConcept.id("block/"+cs+"_candle")));
                pack.addBlockState(SculkConcept.id(cs+"_candle"), builder -> {
                    builder.variant("count=1", variant -> variant.model(SculkConcept.id("block/"+cs+"_candles_single")));
                    builder.variant("count=2", variant -> variant.model(SculkConcept.id("block/"+cs+"_candles_double")));
                    builder.variant("count=3", variant -> variant.model(SculkConcept.id("block/"+cs+"_candles_triple")));
                    builder.variant("count=4", variant -> variant.model(SculkConcept.id("block/"+cs+"_candles_quadruple")));
                });
                pack.addItemModel(SculkConcept.id(cs+"_candle"), builder -> builder.parent(SculkConcept.id("block/"+cs+"_candles_single")));
            }
        });
    }
}
