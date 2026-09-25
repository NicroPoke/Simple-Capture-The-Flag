package com.example.examplemod;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import com.example.examplemod.client.CtfBeamRenderer;
import com.example.examplemod.registry.ModBlockEntities;

@EventBusSubscriber(modid = SimpleCaptureTheFlag.MODID, value = Dist.CLIENT)
public final class SimpleCaptureTheFlagClient {

    private SimpleCaptureTheFlagClient() {
    }

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CTF_POINT.get(), CtfBeamRenderer::new);
    }
}

