package com.example.examplemod.network;

import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import com.example.examplemod.SimpleCaptureTheFlag;

@EventBusSubscriber(modid = SimpleCaptureTheFlag.MODID)
public final class ModNetwork {

    private ModNetwork() {
    }

    @SubscribeEvent
    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(BeamStatePacket.TYPE, BeamStatePacket.STREAM_CODEC, BeamStatePacket::handle);
    }

    public static void sendBeamState(boolean enabled) {
        PacketDistributor.sendToAllPlayers(enabled ? BeamStatePacket.ENABLED : BeamStatePacket.DISABLED);
    }

    public static void sendBeamStateTo(ServerPlayer player, boolean enabled) {
        PacketDistributor.sendToPlayer(player, enabled ? BeamStatePacket.ENABLED : BeamStatePacket.DISABLED);
    }
}
