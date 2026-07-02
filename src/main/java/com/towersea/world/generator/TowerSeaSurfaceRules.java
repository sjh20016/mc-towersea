package com.towersea.world.generator;

import com.towersea.TowerSeaMod;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class TowerSeaSurfaceRules {
    public static SurfaceRules.RuleSource makeRules() {
        TowerSeaMod.LOGGER.info("[TowerSea] Building surface rules.");
        return SurfaceRules.sequence(
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(0, false, 0, SurfaceRules.SurfaceType.FLOOR),
                        SurfaceRules.state(Blocks.GRASS_BLOCK.defaultBlockState())
                ),
                SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(1, false, 1, SurfaceRules.SurfaceType.FLOOR),
                        SurfaceRules.state(Blocks.DIRT.defaultBlockState())
                ),
                SurfaceRules.state(Blocks.STONE.defaultBlockState())
        );
    }
}
