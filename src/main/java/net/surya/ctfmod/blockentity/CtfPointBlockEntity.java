package net.surya.ctfmod.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.surya.ctfmod.registry.ModBlockEntities;

public class CtfPointBlockEntity extends BlockEntity {

    public CtfPointBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CTF_POINT.get(), pos, state);
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(0.0D, 512.0D, 0.0D).inflate(2.0D);
    }
}
