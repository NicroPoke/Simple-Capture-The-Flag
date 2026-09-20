package net.surya.ctfmod.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.surya.ctfmod.CtfMod;
import net.surya.ctfmod.block.CtfPointBlock;

public final class ModBlocks {

    private ModBlocks() {
    }

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, CtfMod.MOD_ID);

    public static final RegistryObject<CtfPointBlock> RED_POINT = BLOCKS.register("red_point",
            () -> new CtfPointBlock(true, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .noOcclusion()
                    .strength(2.0F, 1200.0F)
                    .sound(SoundType.STONE)
                    .pushReaction(PushReaction.BLOCK)));

    public static final RegistryObject<CtfPointBlock> BLUE_POINT = BLOCKS.register("blue_point",
            () -> new CtfPointBlock(false, BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLUE)
                    .noOcclusion()
                    .strength(2.0F, 1200.0F)
                    .sound(SoundType.STONE)
                    .pushReaction(PushReaction.BLOCK)));
}
