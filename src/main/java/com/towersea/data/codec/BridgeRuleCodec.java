package com.towersea.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.towersea.TowerSeaMod;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record BridgeRuleCodec(
        ResourceLocation id,
        int weight,
        int minDistance,
        int maxDistance,
        int minHeightDifference,
        int maxHeightDifference,
        List<String> sourceConnectorTags,
        List<String> targetConnectorTags,
        String structurePool,
        boolean allowRotation,
        boolean allowMirror,
        int clearanceRadius
) {
    public static final Codec<BridgeRuleCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(BridgeRuleCodec::id),
            Codec.INT.fieldOf("weight").forGetter(BridgeRuleCodec::weight),
            Codec.INT.fieldOf("min_distance").forGetter(BridgeRuleCodec::minDistance),
            Codec.INT.fieldOf("max_distance").forGetter(BridgeRuleCodec::maxDistance),
            Codec.INT.fieldOf("min_height_difference").forGetter(BridgeRuleCodec::minHeightDifference),
            Codec.INT.fieldOf("max_height_difference").forGetter(BridgeRuleCodec::maxHeightDifference),
            Codec.STRING.listOf().fieldOf("source_connector_tags").forGetter(BridgeRuleCodec::sourceConnectorTags),
            Codec.STRING.listOf().fieldOf("target_connector_tags").forGetter(BridgeRuleCodec::targetConnectorTags),
            Codec.STRING.fieldOf("structure_pool").forGetter(BridgeRuleCodec::structurePool),
            Codec.BOOL.fieldOf("allow_rotation").forGetter(BridgeRuleCodec::allowRotation),
            Codec.BOOL.fieldOf("allow_mirror").forGetter(BridgeRuleCodec::allowMirror),
            Codec.INT.fieldOf("clearance_radius").forGetter(BridgeRuleCodec::clearanceRadius)
    ).apply(instance, BridgeRuleCodec::new));

    public boolean validate(String fileName) {
        boolean valid = true;
        if (weight < 1) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'weight' must be >= 1 in {} (id={})", fileName, id);
            valid = false;
        }
        if (minDistance >= maxDistance) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'min_distance' must be less than 'max_distance' in {} (id={})", fileName, id);
            valid = false;
        }
        if (minHeightDifference > maxHeightDifference) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'min_height_difference' must be <= 'max_height_difference' in {} (id={})", fileName, id);
            valid = false;
        }
        if (clearanceRadius < 0) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'clearance_radius' must be >= 0 in {} (id={})", fileName, id);
            valid = false;
        }
        return valid;
    }
}
