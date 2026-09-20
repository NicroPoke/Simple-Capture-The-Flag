package net.surya.ctfmod.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.surya.ctfmod.CtfMod;
import net.surya.ctfmod.blockentity.CtfPointBlockEntity;

public final class ModBlockEntities {

    private ModBlockEntities() {
    }

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CtfMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<CtfPointBlockEntity>> CTF_POINT =
            BLOCK_ENTITIES.register("ctf_point", () -> BlockEntityType.Builder
                    .of(CtfPointBlockEntity::new, ModBlocks.RED_POINT.get(), ModBlocks.BLUE_POINT.get())
                    .build(null));
}
