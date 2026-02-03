package com.gammahollow.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import java.util.*;

public class RadiationUtilsOld {

    // Pre-calculated spiral order for x and z offsets
    private static final int[] OFFSETS = {0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5};
    private static final double MAX_RADIUS_SQ = 25.0; // 5.0 squared

    public static boolean isExposed(Level level, BlockPos start) {
        // 1. Immediate sky check (Fastest possible exit)
        if (level.canSeeSky(start)) return true;

        // 2. Scan in a disk using spiral offsets for early exits
        for (int x : OFFSETS) {
            for (int z : OFFSETS) {
                // Euclidean distance check: x^2 + z^2 <= r^2
                if ((x * x) + (z * z) > MAX_RADIUS_SQ) continue;

                int topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, start.getX() + x, start.getZ() + z);
                
                // Only pathfind to windows at or above the entity's feet
                int windowY = Math.max(start.getY(), topY);
                BlockPos window = new BlockPos(start.getX() + x, windowY, start.getZ() + z);
                
                if (hasValidPath(level, start, window)) return true;
            }
        }
        return false;
    }

    private static boolean hasValidPath(Level level, BlockPos start, BlockPos target) {
        // If the window itself is not air, the path is blocked immediately
        if (!level.getBlockState(target).isAir()) return false;
        
        Queue<BlockPos> queue = new LinkedList<>();
        Set<BlockPos> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        int targetX = target.getX();
        int targetY = target.getY();
        int targetZ = target.getZ();

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (current.equals(target)) return true;

            // Constrained Directions (No Backwards Movement)
            // We calculate neighbors that move specifically towards the target coordinates
            List<BlockPos> neighbors = getProgressiveNeighbors(current, targetX, targetY, targetZ);

            for (BlockPos next : neighbors) {
                if (!visited.contains(next) && level.getBlockState(next).isAir()) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }
        return false;
    }

    private static List<BlockPos> getProgressiveNeighbors(BlockPos cur, int tx, int ty, int tz) {
        List<BlockPos> list = new ArrayList<>(3);
        // Always allow UP
        if (cur.getY() < ty) list.add(cur.above());
        
        // Move along X towards target
        if (cur.getX() < tx) list.add(cur.east());
        else if (cur.getX() > tx) list.add(cur.west());
        
        // Move along Z towards target
        if (cur.getZ() < tz) list.add(cur.north());
        else if (cur.getZ() > tz) list.add(cur.south());
        
        return list;
    }
}