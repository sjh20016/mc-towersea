package com.towersea.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.towersea.TowerSeaMod;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record HeightBandCodec(
        ResourceLocation id,
        int minY,
        int maxY,
        float fogDensity,
        List<String> allowedStructureCategories,
        String resourceProfile,
        String mobProfile,
        String ambientProfile
) {
    public static final Codec<HeightBandCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(HeightBandCodec::id),
            Codec.INT.fieldOf("min_y").forGetter(HeightBandCodec::minY),
            Codec.INT.fieldOf("max_y").forGetter(HeightBandCodec::maxY),
            Codec.FLOAT.fieldOf("fog_density").forGetter(HeightBandCodec::fogDensity),
            Codec.STRING.listOf().fieldOf("allowed_structure_categories").forGetter(HeightBandCodec::allowedStructureCategories),
            Codec.STRING.fieldOf("resource_profile").forGetter(HeightBandCodec::resourceProfile),
            Codec.STRING.fieldOf("mob_profile").forGetter(HeightBandCodec::mobProfile),
            Codec.STRING.fieldOf("ambient_profile").forGetter(HeightBandCodec::ambientProfile)
    ).apply(instance, HeightBandCodec::new));

    public boolean validate(String fileName) {
        boolean valid = true;
        if (minY >= maxY) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'min_y' must be less than 'max_y' in {} (id={})", fileName, id);
            valid = false;
        }
        if (fogDensity < 0 || fogDensity > 1) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'fog_density' must be in [0,1] in {} (id={})", fileName, id);
            valid = false;
        }
        return valid;
    }
}
