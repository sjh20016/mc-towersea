package com.towersea.world.feature;

import com.mojang.serialization.Codec;
import com.towersea.TowerSeaMod;
import com.towersea.data.reload.TowerSeaDataReloader;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class TowerFeature extends Feature<NoneFeatureConfiguration> {
    public TowerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        var profiles = TowerSeaDataReloader.TOWER_PROFILES.values();
        if (profiles.isEmpty()) {
            TowerSeaMod.LOGGER.warn("[TowerSea] No tower profiles loaded, skipping tower generation.");
            return false;
        }

        var profileList = profiles.stream().toList();
        var profile = profileList.get(random.nextInt(profileList.size()));

        int centerX = origin.getX();
        int centerZ = origin.getZ();
        int baseY = profile.minY() + random.nextInt(profile.maxY() - profile.minY());
        int height = profile.minHeight() + random.nextInt(profile.maxHeight() - profile.minHeight());
        int radius = profile.minRadius() + random.nextInt(profile.maxRadius() - profile.minRadius());

        boolean hollow = random.nextFloat() < profile.hollowChance();
        boolean broken = random.nextFloat() < profile.brokenChance();
        boolean cave = random.nextFloat() < profile.caveChance();

        for (int y = 0; y < height; y++) {
            if (broken && y > height * 0.6 && random.nextFloat() < 0.3) continue;

            int currentRadius = radius;
            if (y > height * 0.8) {
                currentRadius = Math.max(2, radius - (int)((y - height * 0.8) / (height * 0.2) * radius * 0.5));
            }

            for (int dx = -currentRadius; dx <= currentRadius; dx++) {
                for (int dz = -currentRadius; dz <= currentRadius; dz++) {
                    double dist = Math.sqrt(dx * dx + dz * dz);
                    if (dist > currentRadius) continue;

                    if (hollow && dist < currentRadius * 0.6 && y > 5 && y < height - 5) continue;

                    if (cave && random.nextFloat() < 0.05 && dist < currentRadius * 0.8) continue;

                    BlockPos pos = new BlockPos(centerX + dx, baseY + y, centerZ + dz);
                    if (level.isOutsideBuildHeight(pos)) continue;

                    if (y == 0 || dist > currentRadius - 1) {
                        level.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
                    } else if (y < height - 3) {
                        level.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
                    } else {
                        level.setBlock(pos, Blocks.GRASS_BLOCK.defaultBlockState(), 2);
                    }
                }
            }
        }

        return true;
    }
}
