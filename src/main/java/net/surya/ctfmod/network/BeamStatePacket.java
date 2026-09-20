package net.surya.ctfmod.network;

import java.util.function.Supplier;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.surya.ctfmod.client.CtfClientState;

public class BeamStatePacket {

    private final boolean enabled;

    public BeamStatePacket(boolean enabled) {
        this.enabled = enabled;
    }

    public static void encode(BeamStatePacket msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.enabled);
    }

    public static BeamStatePacket decode(FriendlyByteBuf buf) {
        return new BeamStatePacket(buf.readBoolean());
    }

    public static void handle(BeamStatePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                        () -> () -> CtfClientState.beamEnabled = msg.enabled));
        ctx.get().setPacketHandled(true);
    }
}
