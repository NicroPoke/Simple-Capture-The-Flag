package com.example.examplemod.registry;

import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.example.examplemod.SimpleCaptureTheFlag;
import com.example.examplemod.block.CtfPointBlock;

public final class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SimpleCaptureTheFlag.MODID);

    public static final DeferredBlock<CtfPointBlock> RED_POINT = BLOCKS.register("red_point",
            () -> new CtfPointBlock(true, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noOcclusion()
                    .strength(2.0F, 1200.0F)
                    .sound(SoundType.STONE)
                    .pushReaction(PushReaction.BLOCK)));

    public static final DeferredBlock<CtfPointBlock> BLUE_POINT = BLOCKS.register("blue_point",
            () -> new CtfPointBlock(false, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .noOcclusion()
                    .strength(2.0F, 1200.0F)
                    .sound(SoundType.STONE)
                    .pushReaction(PushReaction.BLOCK)));

    private ModBlocks() {
    }
}
