package net.surya.ctfmod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.surya.ctfmod.CtfMod;

public final class ModNetwork {

    private static final String PROTOCOL = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CtfMod.MOD_ID, "main"),
            () -> PROTOCOL,
            PROTOCOL::equals,
            PROTOCOL::equals);

    private static int packetId = 0;

    private ModNetwork() {
    }

    public static void register() {
        CHANNEL.messageBuilder(BeamStatePacket.class, packetId++)
                .encoder(BeamStatePacket::encode)
                .decoder(BeamStatePacket::decode)
                .consumerMainThread(BeamStatePacket::handle)
                .add();
    }

    public static void sendBeamState(boolean enabled) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), new BeamStatePacket(enabled));
    }
}
