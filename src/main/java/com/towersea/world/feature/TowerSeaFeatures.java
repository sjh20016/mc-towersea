package com.towersea.world.feature;

import com.towersea.TowerSeaMod;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TowerSeaFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, TowerSeaMod.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> TOWER = FEATURES.register("tower",
            () -> new TowerFeature(NoneFeatureConfiguration.CODEC));
}
