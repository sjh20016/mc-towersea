package com.towersea.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.towersea.TowerSeaMod;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record TowerProfileCodec(
        ResourceLocation id,
        int weight,
        int minY,
        int maxY,
        int minRadius,
        int maxRadius,
        int minHeight,
        int maxHeight,
        float hollowChance,
        float brokenChance,
        float caveChance,
        String topShape,
        String baseBlockTag,
        String surfaceBlockTag,
        List<String> allowedHeightBands
) {
    public static final Codec<TowerProfileCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(TowerProfileCodec::id),
            Codec.INT.fieldOf("weight").forGetter(TowerProfileCodec::weight),
            Codec.INT.fieldOf("min_y").forGetter(TowerProfileCodec::minY),
            Codec.INT.fieldOf("max_y").forGetter(TowerProfileCodec::maxY),
            Codec.INT.fieldOf("min_radius").forGetter(TowerProfileCodec::minRadius),
            Codec.INT.fieldOf("max_radius").forGetter(TowerProfileCodec::maxRadius),
            Codec.INT.fieldOf("min_height").forGetter(TowerProfileCodec::minHeight),
            Codec.INT.fieldOf("max_height").forGetter(TowerProfileCodec::maxHeight),
            Codec.FLOAT.fieldOf("hollow_chance").forGetter(TowerProfileCodec::hollowChance),
            Codec.FLOAT.fieldOf("broken_chance").forGetter(TowerProfileCodec::brokenChance),
            Codec.FLOAT.fieldOf("cave_chance").forGetter(TowerProfileCodec::caveChance),
            Codec.STRING.fieldOf("top_shape").forGetter(TowerProfileCodec::topShape),
            Codec.STRING.fieldOf("base_block_tag").forGetter(TowerProfileCodec::baseBlockTag),
            Codec.STRING.fieldOf("surface_block_tag").forGetter(TowerProfileCodec::surfaceBlockTag),
            Codec.STRING.listOf().fieldOf("allowed_height_bands").forGetter(TowerProfileCodec::allowedHeightBands)
    ).apply(instance, TowerProfileCodec::new));

    public boolean validate(String fileName) {
        boolean valid = true;
        if (weight < 1) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'weight' must be >= 1 in {} (id={})", fileName, id);
            valid = false;
        }
        if (minY >= maxY) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'min_y' must be less than 'max_y' in {} (id={})", fileName, id);
            valid = false;
        }
        if (minRadius >= maxRadius) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'min_radius' must be less than 'max_radius' in {} (id={})", fileName, id);
            valid = false;
        }
        if (minHeight >= maxHeight) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'min_height' must be less than 'max_height' in {} (id={})", fileName, id);
            valid = false;
        }
        if (hollowChance < 0 || hollowChance > 1) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'hollow_chance' must be in [0,1] in {} (id={})", fileName, id);
            valid = false;
        }
        if (brokenChance < 0 || brokenChance > 1) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'broken_chance' must be in [0,1] in {} (id={})", fileName, id);
            valid = false;
        }
        if (caveChance < 0 || caveChance > 1) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'cave_chance' must be in [0,1] in {} (id={})", fileName, id);
            valid = false;
        }
        return valid;
    }
}
