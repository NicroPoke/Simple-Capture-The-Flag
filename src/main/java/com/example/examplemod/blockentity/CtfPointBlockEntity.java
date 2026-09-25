package com.example.examplemod.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import com.example.examplemod.registry.ModBlockEntities;

public class CtfPointBlockEntity extends BlockEntity {

    public CtfPointBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CTF_POINT.get(), pos, state);
    }
}
