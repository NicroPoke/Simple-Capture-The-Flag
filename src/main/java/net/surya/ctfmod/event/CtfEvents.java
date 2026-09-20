package net.surya.ctfmod.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.surya.ctfmod.CtfMod;
import net.surya.ctfmod.block.CtfPointBlock;
import net.surya.ctfmod.capture.CtfCaptureManager;
import net.surya.ctfmod.capture.CtfPointTracker;
import net.surya.ctfmod.network.BeamStatePacket;
import net.surya.ctfmod.network.ModNetwork;

@Mod.EventBusSubscriber(modid = CtfMod.MOD_ID)
public final class CtfEvents {

    private CtfEvents() {
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server != null) {
            CtfCaptureManager.serverTick(server);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ModNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player),
                    new BeamStatePacket(CtfCaptureManager.isBeamEnabled()));
        }
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        CtfCaptureManager.reset();
    }

    @SubscribeEvent
    public static void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getLevel() instanceof ServerLevel level
                && event.getPlacedBlock().getBlock() instanceof CtfPointBlock) {
            CtfPointTracker.addPoint(level, event.getPos());
        }
    }

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)
                || !(event.getState().getBlock() instanceof CtfPointBlock point)) {
            return;
        }
        Player player = event.getPlayer();
        if (!player.isCreative()) {
            PlayerTeam team = level.getScoreboard().getPlayersTeam(player.getScoreboardName());
            boolean allowed = point.isRed()
                    ? CtfCaptureManager.isRedTeam(team)
                    : CtfCaptureManager.isBlueTeam(team);
            if (!allowed) {
                event.setCanceled(true);
                player.sendSystemMessage(Component.translatable("ctfmod.msg.no_break"));
                return;
            }
        }
        CtfPointTracker.removePoint(level, event.getPos());
    }
}
