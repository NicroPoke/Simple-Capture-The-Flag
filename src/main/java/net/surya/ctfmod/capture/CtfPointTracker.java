package net.surya.ctfmod.capture;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.surya.ctfmod.block.CtfPointBlock;

public class CtfPointTracker extends SavedData {

    private static final String DATA_NAME = "ctf_points";

    private final Set<BlockPos> points = new HashSet<>();

    public CtfPointTracker() {
    }

    public static CtfPointTracker get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(CtfPointTracker::load, CtfPointTracker::new, DATA_NAME);
    }

    public static CtfPointTracker load(CompoundTag tag) {
        CtfPointTracker data = new CtfPointTracker();
        ListTag list = tag.getList("points", Tag.TAG_LONG);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof LongTag longTag) {
                data.points.add(BlockPos.of(longTag.getAsLong()));
            }
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (BlockPos pos : points) {
            list.add(LongTag.valueOf(pos.asLong()));
        }
        tag.put("points", list);
        return tag;
    }

    public Set<BlockPos> getPoints() {
        return points;
    }

    public static boolean addPoint(ServerLevel level, BlockPos pos) {
        CtfPointTracker data = get(level);
        if (data.points.add(pos.immutable())) {
            data.setDirty();
            return true;
        }
        return false;
    }

    public static boolean removePoint(ServerLevel level, BlockPos pos) {
        CtfPointTracker data = get(level);
        if (data.points.remove(pos)) {
            data.setDirty();
            return true;
        }
        return false;
    }
}
