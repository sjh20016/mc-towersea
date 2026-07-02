package com.towersea.data.reload;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import com.towersea.TowerSeaMod;
import com.towersea.data.codec.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.*;

public class TowerSeaDataReloader extends SimplePreparableReloadListener<Map<String, Map<ResourceLocation, ?>>> {
    public static final Map<ResourceLocation, TowerProfileCodec> TOWER_PROFILES = new HashMap<>();
    public static final Map<ResourceLocation, HeightBandCodec> HEIGHT_BANDS = new HashMap<>();
    public static final Map<ResourceLocation, BridgeRuleCodec> BRIDGE_RULES = new HashMap<>();
    public static final Map<ResourceLocation, StructureCategoryCodec> STRUCTURE_CATEGORIES = new HashMap<>();
    public static final Map<ResourceLocation, PortalRuleCodec> PORTAL_RULES = new HashMap<>();
    public static WorldRulesCodec WORLD_RULES = null;

    private static final String PREFIX = "towersea";

    @Override
    protected Map<String, Map<ResourceLocation, ?>> prepare(ResourceManager manager, ProfilerFiller profiler) {
        Map<String, Map<ResourceLocation, ?>> result = new HashMap<>();
        result.put("tower_profiles", loadFolder(manager, "towersea/tower_profiles", TowerProfileCodec.CODEC));
        result.put("height_bands", loadFolder(manager, "towersea/height_bands", HeightBandCodec.CODEC));
        result.put("bridge_rules", loadFolder(manager, "towersea/bridge_rules", BridgeRuleCodec.CODEC));
        result.put("structure_categories", loadFolder(manager, "towersea/structure_categories", StructureCategoryCodec.CODEC));
        result.put("portal_rules", loadFolder(manager, "towersea/portal_rules", PortalRuleCodec.CODEC));
        result.put("world_rules", loadSingle(manager, "towersea/world_rules/world_rules.json", WorldRulesCodec.CODEC));
        return result;
    }

    @Override
    protected void apply(Map<String, Map<ResourceLocation, ?>> data, ResourceManager manager, ProfilerFiller profiler) {
        TOWER_PROFILES.clear();
        @SuppressWarnings("unchecked")
        Map<ResourceLocation, TowerProfileCodec> profiles = (Map<ResourceLocation, TowerProfileCodec>) data.get("tower_profiles");
        if (profiles != null) {
            profiles.forEach((id, profile) -> {
                if (profile.validate(id.toString())) TOWER_PROFILES.put(id, profile);
                else TowerSeaMod.LOGGER.warn("[TowerSea] Skipped invalid tower profile: {}", id);
            });
        }

        HEIGHT_BANDS.clear();
        @SuppressWarnings("unchecked")
        Map<ResourceLocation, HeightBandCodec> bands = (Map<ResourceLocation, HeightBandCodec>) data.get("height_bands");
        if (bands != null) {
            bands.forEach((id, band) -> {
                if (band.validate(id.toString())) HEIGHT_BANDS.put(id, band);
                else TowerSeaMod.LOGGER.warn("[TowerSea] Skipped invalid height band: {}", id);
            });
        }

        BRIDGE_RULES.clear();
        @SuppressWarnings("unchecked")
        Map<ResourceLocation, BridgeRuleCodec> bridges = (Map<ResourceLocation, BridgeRuleCodec>) data.get("bridge_rules");
        if (bridges != null) {
            bridges.forEach((id, rule) -> {
                if (rule.validate(id.toString())) BRIDGE_RULES.put(id, rule);
                else TowerSeaMod.LOGGER.warn("[TowerSea] Skipped invalid bridge rule: {}", id);
            });
        }

        STRUCTURE_CATEGORIES.clear();
        @SuppressWarnings("unchecked")
        Map<ResourceLocation, StructureCategoryCodec> categories = (Map<ResourceLocation, StructureCategoryCodec>) data.get("structure_categories");
        if (categories != null) {
            categories.forEach((id, cat) -> {
                if (cat.validate(id.toString())) STRUCTURE_CATEGORIES.put(id, cat);
                else TowerSeaMod.LOGGER.warn("[TowerSea] Skipped invalid structure category: {}", id);
            });
        }

        PORTAL_RULES.clear();
        @SuppressWarnings("unchecked")
        Map<ResourceLocation, PortalRuleCodec> portals = (Map<ResourceLocation, PortalRuleCodec>) data.get("portal_rules");
        if (portals != null) {
            portals.forEach((id, rule) -> {
                if (rule.validate(id.toString())) PORTAL_RULES.put(id, rule);
                else TowerSeaMod.LOGGER.warn("[TowerSea] Skipped invalid portal rule: {}", id);
            });
        }

        WORLD_RULES = null;
        @SuppressWarnings("unchecked")
        Map<ResourceLocation, WorldRulesCodec> rules = (Map<ResourceLocation, WorldRulesCodec>) data.get("world_rules");
        if (rules != null && !rules.isEmpty()) {
            WorldRulesCodec wr = rules.values().iterator().next();
            if (wr.validate("world_rules.json")) WORLD_RULES = wr;
        }

        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} tower profiles.", TOWER_PROFILES.size());
        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} height bands.", HEIGHT_BANDS.size());
        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} bridge rules.", BRIDGE_RULES.size());
        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} structure categories.", STRUCTURE_CATEGORIES.size());
        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} portal rules.", PORTAL_RULES.size());
        TowerSeaMod.LOGGER.info("[TowerSea] World rules loaded: {}", WORLD_RULES != null);
    }

    private <T> Map<ResourceLocation, T> loadFolder(ResourceManager manager, String path, com.mojang.serialization.Codec<T> codec) {
        Map<ResourceLocation, T> map = new HashMap<>();
        manager.listResources(path, id -> id.getPath().endsWith(".json")).forEach((id, resource) -> {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = JsonParser.parseReader(reader);
                codec.parse(JsonOps.INSTANCE, json).resultOrPartial(err ->
                        TowerSeaMod.LOGGER.error("[TowerSea] Parse error in {}: {}", id, err)
                ).ifPresent(value -> map.put(id, value));
            } catch (Exception e) {
                TowerSeaMod.LOGGER.error("[TowerSea] Failed to load {}: {}", id, e.getMessage());
            }
        });
        return map;
    }

    private <T> Map<ResourceLocation, T> loadSingle(ResourceManager manager, String path, com.mojang.serialization.Codec<T> codec) {
        Map<ResourceLocation, T> map = new HashMap<>();
        try {
            for (var entry : manager.listResources(path.substring(0, path.lastIndexOf('/')), id -> id.getPath().equals(path)).entrySet()) {
                try (Reader reader = entry.getValue().openAsReader()) {
                    JsonElement json = JsonParser.parseReader(reader);
                    codec.parse(JsonOps.INSTANCE, json).resultOrPartial(err ->
                            TowerSeaMod.LOGGER.error("[TowerSea] Parse error in {}: {}", entry.getKey(), err)
                    ).ifPresent(value -> map.put(entry.getKey(), value));
                }
            }
        } catch (Exception e) {
            TowerSeaMod.LOGGER.error("[TowerSea] Failed to load world rules: {}", e.getMessage());
        }
        return map;
    }
}
