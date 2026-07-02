package com.towersea.debug;

import com.towersea.TowerSeaMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

public class DebugHelper {
    public static void logChunkInfo(ServerLevel level, BlockPos pos) {
        ChunkAccess chunk = level.getChunk(pos);
        TowerSeaMod.LOGGER.debug("[TowerSea] Chunk at {} status: {}", pos, chunk.getStatus());
    }
}
