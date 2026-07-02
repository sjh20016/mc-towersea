package com.towersea.world.generator;

import com.towersea.TowerSeaMod;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.chunk.ChunkStatus;

public class TowerSeaChunkStatus {
    public static void logGenerationProgress(WorldGenRegion region, int centerX, int centerZ) {
        TowerSeaMod.LOGGER.debug("[TowerSea] Generating chunk at {}, {}", centerX, centerZ);
    }
}
