package com.example.examplemod.registry;

import java.util.Set;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.example.examplemod.SimpleCaptureTheFlag;
import com.example.examplemod.blockentity.CtfPointBlockEntity;

public final class ModBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SimpleCaptureTheFlag.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CtfPointBlockEntity>> CTF_POINT =
            BLOCK_ENTITIES.register("ctf_point", () -> new BlockEntityType<>(
                    CtfPointBlockEntity::new,
                    Set.of(ModBlocks.RED_POINT.get(), ModBlocks.BLUE_POINT.get()),
                    null));

    private ModBlockEntities() {
    }
}
