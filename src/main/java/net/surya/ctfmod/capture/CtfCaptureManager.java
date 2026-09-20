package net.surya.ctfmod.capture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.PlayerTeam;
import net.surya.ctfmod.block.CtfPointBlock;
import net.surya.ctfmod.registry.ModBlocks;

public final class CtfCaptureManager {

    public static final int DEFAULT_CAPTURE_SECONDS = 300;
    public static final int DEFAULT_CAPTURE_RADIUS = 5;

    private static int captureSeconds = DEFAULT_CAPTURE_SECONDS;
    private static int captureRadius = DEFAULT_CAPTURE_RADIUS;
    private static boolean beamEnabled = true;

    private static final Map<PointKey, Capture> captures = new HashMap<>();

    private CtfCaptureManager() {
    }

    public static int getCaptureSeconds() {
        return captureSeconds;
    }

    public static void setCaptureSeconds(int seconds) {
        captureSeconds = Math.max(1, seconds);
    }

    public static int getCaptureRadius() {
        return captureRadius;
    }

    public static void setCaptureRadius(int radius) {
        captureRadius = Math.max(0, Math.min(64, radius));
    }

    public static boolean isBeamEnabled() {
        return beamEnabled;
    }

    public static void setBeamEnabled(boolean enabled) {
        beamEnabled = enabled;
        net.surya.ctfmod.network.ModNetwork.sendBeamState(enabled);
    }

    public static void reset() {
        for (Capture capture : captures.values()) {
            capture.removeAllViewers();
        }
        captures.clear();
    }

    public static void serverTick(MinecraftServer server) {
        int required = captureSeconds * 20;

        for (ServerLevel level : server.getAllLevels()) {
            Map<BlockPos, List<ServerPlayer>> zones = collectPlayersInZones(level);
            if (zones.isEmpty() && captures.isEmpty()) {
                continue;
            }

            Set<PointKey> activeNow = new HashSet<>();

            for (Map.Entry<BlockPos, List<ServerPlayer>> entry : zones.entrySet()) {
                BlockPos pos = entry.getKey();
                if (!(level.getBlockState(pos).getBlock() instanceof CtfPointBlock point)) {

                    CtfPointTracker.removePoint(level, pos);
                    continue;
                }
                boolean blockIsRed = point.isRed();

                List<ServerPlayer> attackers = new ArrayList<>();
                List<ServerPlayer> defenders = new ArrayList<>();
                PlayerTeam attackerTeam = null;

                for (ServerPlayer player : entry.getValue()) {

                    PlayerTeam team = server.getScoreboard().getPlayersTeam(player.getScoreboardName());
                    if (team == null) {
                        continue;
                    }
                    boolean teamRed = isRedColor(team.getColor());
                    boolean teamBlue = isBlueColor(team.getColor());
                    if (!teamRed && !teamBlue) {
                        continue;
                    }
                    if (teamRed == blockIsRed) {
                        defenders.add(player);
                    } else {
                        attackers.add(player);
                        if (attackerTeam == null) {
                            attackerTeam = team;
                        }
                    }
                }

                PointKey key = new PointKey(level.dimension(), pos);

                if (defenders.isEmpty() && !attackers.isEmpty() && attackerTeam != null) {

                    Capture capture = captures.computeIfAbsent(key, k -> new Capture());
                    BossEvent.BossBarColor color = blockIsRed
                            ? BossEvent.BossBarColor.BLUE
                            : BossEvent.BossBarColor.RED;
                    capture.ensureBar(color, attackerTeam);
                    capture.progress += attackers.size();
                    capture.bar.setProgress(Math.min(1.0F, capture.progress / (float) required));
                    capture.updateViewers(attackers);

                    long remaining = Math.max(0, (required - capture.progress
                            + (long) attackers.size() * 20 - 1) / ((long) attackers.size() * 20));
                    if (remaining != capture.lastShownSeconds) {
                        capture.lastShownSeconds = remaining;
                        capture.bar.setName(Component.translatable("ctfmod.bossbar.capture",
                                attackerTeam.getDisplayName(),
                                Component.literal(formatTime(remaining))));
                    }
                    if (capture.progress >= required) {
                        completeCapture(level, pos, blockIsRed, attackerTeam);
                        capture.removeAllViewers();
                        captures.remove(key);
                        continue;
                    }
                    activeNow.add(key);
                } else if (!attackers.isEmpty()) {

                    Capture capture = captures.get(key);
                    if (capture != null) {
                        capture.updateViewers(attackers);
                        activeNow.add(key);
                    }
                }

                if (!activeNow.contains(key)) {
                    Capture stale = captures.remove(key);
                    if (stale != null) {
                        stale.removeAllViewers();
                    }
                }
            }

            captures.keySet().removeIf(key -> {
                if (!key.dimension().equals(level.dimension()) || zones.containsKey(key.pos())) {
                    return false;
                }
                Capture capture = captures.get(key);
                if (capture != null) {
                    capture.removeAllViewers();
                }
                return true;
            });
        }
    }

    private static Map<BlockPos, List<ServerPlayer>> collectPlayersInZones(ServerLevel level) {
        Map<BlockPos, List<ServerPlayer>> zones = new HashMap<>();
        Set<BlockPos> points = CtfPointTracker.get(level).getPoints();
        for (ServerPlayer player : level.players()) {
            if (player.isSpectator()) {
                continue;
            }

            BlockPos feet = player.blockPosition().below();
            if (level.getBlockState(feet).getBlock() instanceof CtfPointBlock) {
                CtfPointTracker.addPoint(level, feet);
                zones.computeIfAbsent(feet, p -> new ArrayList<>()).add(player);
                continue;
            }
            if (points.isEmpty()) {
                continue;
            }

            for (BlockPos pos : points) {
                double dx = player.getX() - (pos.getX() + 0.5D);
                double dz = player.getZ() - (pos.getZ() + 0.5D);
                double dy = Math.abs(player.getY() - (pos.getY() + 0.5D));
                if (dx * dx + dz * dz <= (double) captureRadius * captureRadius
                        && dy <= captureRadius) {
                    zones.computeIfAbsent(pos, p -> new ArrayList<>()).add(player);
                }
            }
        }
        return zones;
    }

    private static void completeCapture(ServerLevel level, BlockPos pos, boolean blockWasRed, PlayerTeam team) {

        BlockState oldState = level.getBlockState(pos);
        BlockState newState = (blockWasRed ? ModBlocks.BLUE_POINT : ModBlocks.RED_POINT).get().defaultBlockState();
        if (oldState.hasProperty(CtfPointBlock.FACING)) {
            newState = newState.setValue(CtfPointBlock.FACING, oldState.getValue(CtfPointBlock.FACING));
        }
        level.setBlock(pos, newState, 3);
        level.playSound(null, pos, SoundEvents.BEACON_POWER_SELECT, SoundSource.BLOCKS, 1.0F, 1.0F);
        Component message = Component.translatable("ctfmod.msg.captured",
                team.getDisplayName(),
                Component.literal(String.valueOf(pos.getX())),
                Component.literal(String.valueOf(pos.getY())),
                Component.literal(String.valueOf(pos.getZ())));
        level.getServer().getPlayerList().broadcastSystemMessage(message, false);
    }

    public static boolean isRedTeam(PlayerTeam team) {
        return team != null && isRedColor(team.getColor());
    }

    public static boolean isBlueTeam(PlayerTeam team) {
        return team != null && isBlueColor(team.getColor());
    }

    private static boolean isRedColor(ChatFormatting color) {
        return color == ChatFormatting.RED || color == ChatFormatting.DARK_RED;
    }

    private static boolean isBlueColor(ChatFormatting color) {
        return color == ChatFormatting.BLUE || color == ChatFormatting.DARK_BLUE;
    }

    private static String formatTime(long totalSeconds) {
        return String.format("%d:%02d", totalSeconds / 60, totalSeconds % 60);
    }

    private record PointKey(ResourceKey<Level> dimension, BlockPos pos) {
    }

    private static class Capture {
        private int progress;
        private ServerBossEvent bar;
        private BossEvent.BossBarColor color;
        private long lastShownSeconds = -1;

        private void ensureBar(BossEvent.BossBarColor barColor, PlayerTeam team) {
            if (bar == null) {
                color = barColor;
                lastShownSeconds = -1;
                bar = new ServerBossEvent(Component.translatable("ctfmod.bossbar.capture",
                        team.getDisplayName(), Component.literal("")), barColor,
                        BossEvent.BossBarOverlay.PROGRESS);
            } else if (color != barColor) {
                color = barColor;
                bar.setColor(barColor);
            }
            bar.setProgress(0.0F);
        }

        private void updateViewers(List<ServerPlayer> viewers) {
            Set<ServerPlayer> current = new HashSet<>(bar.getPlayers());
            for (ServerPlayer viewer : current) {
                if (!viewers.contains(viewer)) {
                    bar.removePlayer(viewer);
                }
            }
            for (ServerPlayer viewer : viewers) {
                if (!current.contains(viewer)) {
                    bar.addPlayer(viewer);
                }
            }
        }

        private void removeAllViewers() {
            if (bar != null) {
                bar.removeAllPlayers();
            }
        }
    }
}
