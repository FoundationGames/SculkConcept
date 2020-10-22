package io.github.foundationgames.sculkconcept;

import io.github.foundationgames.sculkconcept.block.CandleBlock;
import io.github.foundationgames.sculkconcept.block.SculkGrowthBlock;
import io.github.foundationgames.sculkconcept.block.SculkSensorBlock;
import io.github.foundationgames.sculkconcept.block.SculkSourceBlock;
import io.github.foundationgames.sculkconcept.block.entity.SculkSensorBlockEntity;
import io.github.foundationgames.sculkconcept.callback.BlockCallbacks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;

public class SculkConcept implements ModInitializer {

    public static final Block SCULK_SENSOR = register("sculk_sensor", new SculkSensorBlock(FabricBlockSettings.copy(Blocks.STONE)), ItemGroup.REDSTONE);
    public static final Block SCULK_GROWTH = register("sculk_growth", new SculkGrowthBlock(FabricBlockSettings.copy(Blocks.GRASS).sounds(BlockSoundGroup.NETHER_WART)), ItemGroup.DECORATIONS);
    public static final Block SCULK_GROWTH_BLOCK = register("sculk_growth_block", new Block(FabricBlockSettings.copy(Blocks.STONE).sounds(BlockSoundGroup.CORAL)), ItemGroup.DECORATIONS);
    public static final Block SCULK_SOURCE = register("sculk_source", new SculkSourceBlock(FabricBlockSettings.copy(Blocks.STONE)), ItemGroup.DECORATIONS);

    public static final Block SLATE = register("slate", new Block(FabricBlockSettings.copy(Blocks.STONE)), ItemGroup.BUILDING_BLOCKS);

    public static final Block WHITE_CANDLE = register("white_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block RED_CANDLE = register("red_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block ORANGE_CANDLE = register("orange_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block YELLOW_CANDLE = register("yellow_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block LIME_CANDLE = register("lime_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block GREEN_CANDLE = register("green_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block BLUE_CANDLE = register("blue_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block CYAN_CANDLE = register("cyan_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block LIGHT_BLUE_CANDLE = register("light_blue_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block PINK_CANDLE = register("pink_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block MAGENTA_CANDLE = register("magenta_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block PURPLE_CANDLE = register("purple_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block BROWN_CANDLE = register("brown_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block BLACK_CANDLE = register("black_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block GRAY_CANDLE = register("gray_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);
    public static final Block LIGHT_GRAY_CANDLE = register("light_gray_candle", new CandleBlock(FabricBlockSettings.copy(Blocks.TORCH).lightLevel(state -> 6 + state.get(CandleBlock.COUNT) * 2)), ItemGroup.DECORATIONS);


    public static SoundEvent SCULK_SENSOR_TRILL = Registry.register(Registry.SOUND_EVENT, id("sculk_sensor_trill"), new SoundEvent(id("sculk_sensor_trill")));

    public static final BlockEntityType<SculkSensorBlockEntity> SCULK_SENSOR_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, id("sculk_sensor"), BlockEntityType.Builder.create(SculkSensorBlockEntity::new, SCULK_SENSOR).build(null));

    @Override
    public void onInitialize() {
        BlockCallbacks.init();
        //Registry.register(Registry.ITEM, id("sculk_sensor"), new BlockItem(SCULK_SENSOR, new Item.Settings().group(ItemGroup.REDSTONE)));
    }

    public static Identifier id(String path) {
        return new Identifier("sculkconcept", path);
    }
    public static int ticksOfDist(Vec3d start, Vec3d end) {
        return (int)(start.distanceTo(end) * 1.73);
    }

    private static Block register(String name, Block block, ItemGroup group) {
        Registry.register(Registry.ITEM, id(name), new BlockItem(block, new Item.Settings().group(group)));
        return Registry.register(Registry.BLOCK, id(name), block);
    }
}
