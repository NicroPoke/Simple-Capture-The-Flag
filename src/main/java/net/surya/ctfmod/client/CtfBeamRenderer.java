package net.surya.ctfmod.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.surya.ctfmod.block.CtfPointBlock;

public class CtfBeamRenderer implements BlockEntityRenderer<BlockEntity> {

    private static final ResourceLocation BEAM_TEXTURE =
            new ResourceLocation("textures/entity/beacon_beam.png");

    public CtfBeamRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BlockEntity blockEntity, float partialTick, PoseStack poseStack,
            MultiBufferSource buffer, int packedLight, int packedOverlay) {
        if (!CtfClientState.beamEnabled) {
            return;
        }
        if (!(blockEntity.getBlockState().getBlock() instanceof CtfPointBlock point)) {
            return;
        }
        float red = point.isRed() ? 1.0F : 0.2F;
        float green = point.isRed() ? 0.2F : 0.4F;
        float blue = point.isRed() ? 0.2F : 1.0F;
        long gameTime = blockEntity.getLevel() != null ? blockEntity.getLevel().getGameTime() : 0L;

        int yOffset = 2;
        int height = blockEntity.getLevel() != null
                ? Math.max(1, blockEntity.getLevel().getMaxBuildHeight() + 32 - blockEntity.getBlockPos().getY() - yOffset)
                : 512;

        BeaconRenderer.renderBeaconBeam(poseStack, buffer, BEAM_TEXTURE, partialTick, 1.0F,
                gameTime, yOffset, height, new float[]{red, green, blue}, 0.2F, 0.25F);
    }

    @Override
    public boolean shouldRenderOffScreen(BlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
