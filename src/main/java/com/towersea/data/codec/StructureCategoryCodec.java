package com.towersea.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.towersea.TowerSeaMod;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record StructureCategoryCodec(
        ResourceLocation id,
        int minY,
        int maxY,
        int defaultWeight,
        int maxPerRegion,
        List<String> allowedBiomes
) {
    public static final Codec<StructureCategoryCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(StructureCategoryCodec::id),
            Codec.INT.fieldOf("min_y").forGetter(StructureCategoryCodec::minY),
            Codec.INT.fieldOf("max_y").forGetter(StructureCategoryCodec::maxY),
            Codec.INT.fieldOf("default_weight").forGetter(StructureCategoryCodec::defaultWeight),
            Codec.INT.fieldOf("max_per_region").forGetter(StructureCategoryCodec::maxPerRegion),
            Codec.STRING.listOf().fieldOf("allowed_biomes").forGetter(StructureCategoryCodec::allowedBiomes)
    ).apply(instance, StructureCategoryCodec::new));

    public boolean validate(String fileName) {
        boolean valid = true;
        if (minY >= maxY) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'min_y' must be less than 'max_y' in {} (id={})", fileName, id);
            valid = false;
        }
        if (defaultWeight < 1) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'default_weight' must be >= 1 in {} (id={})", fileName, id);
            valid = false;
        }
        if (maxPerRegion < 1) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'max_per_region' must be >= 1 in {} (id={})", fileName, id);
            valid = false;
        }
        return valid;
    }
}
