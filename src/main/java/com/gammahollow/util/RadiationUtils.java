package com.gammahollow.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;


public class RadiationUtils {

    // Pre-calculated spiral order for x and z offsets
    private static final int[] OFFSETS = {0, 1, -1, 2, -2, 3, -3, 4, -4, 5, -5};
    private static final int[] DX = {1, -1};
    private static final double MAX_RADIUS_SQ = 25.0; // 5.0 squared

    public record XYPair(int x, int z) {}
    public static long pack(int x, int z) {
        return ((long)x << 32) | (z & 0xFFFFFFFFL);
    }
    public static Integer packDelta(int dx, int dy, int dz) {
        return (dx & 0xFF) << 16 | (dy & 0xFF) << 8 | (dz & 0xFF);
    }

    public static boolean isExposed(Level level, BlockPos start) {
        // Create heightmaps for top
        Map<Long, Integer> topHeightMap = new HashMap<>();

        int start_x = start.getX();
        int start_z = start.getZ();

        // Prepare heightmap.
        for (int x : OFFSETS) {
            for (int z : OFFSETS) {
                // Euclidean distance check: x^2 + z^2 <= r^2
                if ((x * x) + (z * z) > MAX_RADIUS_SQ) continue;

                Integer topY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, start_x + x, start_z + z);
                topHeightMap.put(pack(start_x + x, start_z + z), Math.max(start.getY(), topY));
            }
        }

        // Check the starting position
        if (!level.getBlockState(start).isAir()) {
            return false;
        }
        if (topHeightMap.get(pack(start_x, start_z)) <= start.getY()) {
            return true;
        }
        
        // BFS Initialization
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(start);
        Set<Integer> visited = new HashSet<>();
        
        // BFS Loop
        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            int x = current.getX(), y = current.getY(), z = current.getZ();
            Integer packedBlock = packDelta(x - start_x + 128, y - start.getY() + 128, z - start_z + 128);
            if (visited.contains(packedBlock)) continue;
            visited.add(packedBlock);

            int dx = x > start_x ? 1 : (x < start_x ? -1 : 0);
            int dz = z > start_z ? 1 : (z < start_z ? -1 : 0);

            // Explore neighbors
            if (dx != 0 && topHeightMap.containsKey(pack(x + dx, z))) {  // Move in x direction
                BlockPos neighbor = new BlockPos(x + dx, y, z);
                Integer neighborPacked = packDelta(neighbor.getX() - start_x + 128, neighbor.getY() - start.getY() + 128, neighbor.getZ() - start_z + 128);
                if (!visited.contains(neighborPacked) && level.getBlockState(neighbor).isAir()) {
                    queue.add(neighbor);
                    if (topHeightMap.get(pack(neighbor.getX(), neighbor.getZ())) <= neighbor.getY()) {
                        return true;
                    }
                }
            } else if (dx == 0) {
                if (topHeightMap.containsKey(pack(x + 1, z))) {  // Move in +x direction
                    BlockPos neighbor = new BlockPos(x + 1, y, z);
                    Integer neighborPacked = packDelta(neighbor.getX() - start_x + 128, neighbor.getY() - start.getY() + 128, neighbor.getZ() - start_z + 128);
                    if (!visited.contains(neighborPacked) && level.getBlockState(neighbor).isAir()) {
                        queue.add(neighbor);
                        if (topHeightMap.get(pack(neighbor.getX(), neighbor.getZ())) <= neighbor.getY()) {
                            return true;
                        }
                    }
                }
                if (topHeightMap.containsKey(pack(x - 1, z))) {  // Move in -x direction
                    BlockPos neighbor = new BlockPos(x - 1, y, z);
                    Integer neighborPacked = packDelta(neighbor.getX() - start_x + 128, neighbor.getY() - start.getY() + 128, neighbor.getZ() - start_z + 128);
                    if (!visited.contains(neighborPacked) && level.getBlockState(neighbor).isAir()) {
                        queue.add(neighbor);
                        if (topHeightMap.get(pack(neighbor.getX(), neighbor.getZ())) <= neighbor.getY()) {
                            return true;
                        }
                    }
                }
            }
            if (dz != 0 && topHeightMap.containsKey(pack(x, z + dz))) {  // Move in z direction
                BlockPos neighbor = new BlockPos(x, y, z + dz);
                Integer neighborPacked = packDelta(neighbor.getX() - start_x + 128, neighbor.getY() - start.getY() + 128, neighbor.getZ() - start_z + 128);
                if (!visited.contains(neighborPacked) && level.getBlockState(neighbor).isAir()) {
                    queue.add(neighbor);
                    if (topHeightMap.get(pack(neighbor.getX(), neighbor.getZ())) <= neighbor.getY()) {
                        return true;
                    }
                }
            } else if (dz == 0) {
                if (topHeightMap.containsKey(pack(x, z + 1))) {  // Move in +z direction
                    BlockPos neighbor = new BlockPos(x, y, z + 1);
                    Integer neighborPacked = packDelta(neighbor.getX() - start_x + 128, neighbor.getY() - start.getY() + 128, neighbor.getZ() - start_z + 128);
                    if (!visited.contains(neighborPacked) && level.getBlockState(neighbor).isAir()) {
                        queue.add(neighbor);
                        if (topHeightMap.get(pack(neighbor.getX(), neighbor.getZ())) <= neighbor.getY()) {
                            return true;
                        }
                    }
                }
                if (topHeightMap.containsKey(pack(x, z - 1))) {  // Move in -z direction
                    BlockPos neighbor = new BlockPos(x, y, z - 1);
                    Integer neighborPacked = packDelta(neighbor.getX() - start_x + 128, neighbor.getY() - start.getY() + 128, neighbor.getZ() - start_z + 128);
                    if (!visited.contains(neighborPacked) && level.getBlockState(neighbor).isAir()) {
                        queue.add(neighbor);
                        if (topHeightMap.get(pack(neighbor.getX(), neighbor.getZ())) <= neighbor.getY()) {
                            return true;
                        }
                    }
                }
            }
            if (level.getBlockState(new BlockPos(x, y + 1, z)).isAir())  {  // Move up
                queue.add(new BlockPos(x, y + 1, z));
            }
        }
        return false;
    }
}