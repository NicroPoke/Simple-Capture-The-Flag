package com.example.examplemod.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.PlayerTeam;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import com.example.examplemod.SimpleCaptureTheFlag;
import com.example.examplemod.block.CtfPointBlock;
import com.example.examplemod.capture.CtfCaptureManager;
import com.example.examplemod.capture.CtfPointTracker;
import com.example.examplemod.network.BeamStatePacket;
import com.example.examplemod.network.ModNetwork;

@EventBusSubscriber(modid = SimpleCaptureTheFlag.MODID)
public final class CtfEvents {

    private CtfEvents() {
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        if (server != null) {
            CtfCaptureManager.serverTick(server);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ModNetwork.sendBeamStateTo(player, CtfCaptureManager.isBeamEnabled());
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
