package com.example.examplemod.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import com.example.examplemod.SimpleCaptureTheFlag;
import com.example.examplemod.client.CtfClientState;

public record BeamStatePacket(boolean enabled) implements CustomPacketPayload {

    public static final BeamStatePacket ENABLED = new BeamStatePacket(true);
    public static final BeamStatePacket DISABLED = new BeamStatePacket(false);

    public static final CustomPacketPayload.Type<BeamStatePacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(SimpleCaptureTheFlag.MODID, "beam_state"));

    public static final StreamCodec<ByteBuf, BeamStatePacket> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.BOOL, BeamStatePacket::enabled, BeamStatePacket::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(BeamStatePacket packet, IPayloadContext context) {
        CtfClientState.beamEnabled = packet.enabled;
    }
}
