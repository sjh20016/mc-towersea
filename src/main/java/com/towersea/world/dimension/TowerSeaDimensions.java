package com.towersea.world.dimension;

import com.towersea.TowerSeaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class TowerSeaDimensions {
    public static final ResourceKey<Level> TOWERSEA_LEVEL = ResourceKey.create(
            Registries.DIMENSION, new ResourceLocation(TowerSeaMod.MOD_ID, "towersea"));
    public static final ResourceKey<Level> WAREHOUSE_LEVEL = ResourceKey.create(
            Registries.DIMENSION, new ResourceLocation(TowerSeaMod.MOD_ID, "warehouse"));

    public static final ResourceKey<DimensionType> TOWERSEA_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE, new ResourceLocation(TowerSeaMod.MOD_ID, "towersea"));
    public static final ResourceKey<DimensionType> WAREHOUSE_TYPE = ResourceKey.create(
            Registries.DIMENSION_TYPE, new ResourceLocation(TowerSeaMod.MOD_ID, "warehouse"));

    public static void register() {
        TowerSeaMod.LOGGER.info("[TowerSea] Dimension keys registered.");
    }
}
