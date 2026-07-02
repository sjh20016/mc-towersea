package com.towersea.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.towersea.TowerSeaMod;

public record WorldRulesCodec(
        int fallReturnThreshold,
        String fallReturnTarget,
        int fallProtectionTicks,
        boolean grantSlowFalling,
        boolean grantFeatherFalling,
        int spawnPlatformRadius,
        int initialAreaSize
) {
    public static final Codec<WorldRulesCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("fall_return_threshold").forGetter(WorldRulesCodec::fallReturnThreshold),
            Codec.STRING.fieldOf("fall_return_target").forGetter(WorldRulesCodec::fallReturnTarget),
            Codec.INT.fieldOf("fall_protection_ticks").forGetter(WorldRulesCodec::fallProtectionTicks),
            Codec.BOOL.fieldOf("grant_slow_falling").forGetter(WorldRulesCodec::grantSlowFalling),
            Codec.BOOL.fieldOf("grant_feather_falling").forGetter(WorldRulesCodec::grantFeatherFalling),
            Codec.INT.fieldOf("spawn_platform_radius").forGetter(WorldRulesCodec::spawnPlatformRadius),
            Codec.INT.fieldOf("initial_area_size").forGetter(WorldRulesCodec::initialAreaSize)
    ).apply(instance, WorldRulesCodec::new));

    public boolean validate(String fileName) {
        boolean valid = true;
        if (fallReturnThreshold < -64 || fallReturnThreshold > 320) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'fall_return_threshold' out of reasonable range in {}.", fileName);
            valid = false;
        }
        return valid;
    }
}
