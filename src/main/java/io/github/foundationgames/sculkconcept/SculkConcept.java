package io.github.foundationgames.sculkconcept;

import io.github.foundationgames.sculkconcept.block.ParticleTestBlock;
import io.github.foundationgames.sculkconcept.block.SculkSensorBlock;
import io.github.foundationgames.sculkconcept.block.entity.SculkSensorBlockEntity;
import io.github.foundationgames.sculkconcept.callback.BlockCallbacks;
import io.github.foundationgames.sculkconcept.world.VibrationListenerState;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

public class SculkConcept implements ModInitializer {

    public static final Block SCULK_SENSOR = Registry.register(Registry.BLOCK, id("sculk_sensor"), new SculkSensorBlock(FabricBlockSettings.copy(Blocks.STONE)));
    public static final PointOfInterestType SCULK_SENSOR_POI = PointOfInterestHelper.register(id("sculk_sensor"), 0, 20, SCULK_SENSOR);

    public static final BlockEntityType<SculkSensorBlockEntity> SCULK_SENSOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("sculk_sensor"), BlockEntityType.Builder.create(SculkSensorBlockEntity::new, SCULK_SENSOR).build(null));

    @Override
    public void onInitialize() {
        BlockCallbacks.init();
        Registry.register(Registry.ITEM, id("sculk_sensor"), new BlockItem(SCULK_SENSOR, new Item.Settings().group(ItemGroup.REDSTONE)));
    }

    public static Identifier id(String path) {
        return new Identifier("sculkconcept", path);
    }
    public static int ticksOfDist(Vec3d start, Vec3d end) {
        return (int)(start.distanceTo(end) * 1.73);
    }
}
