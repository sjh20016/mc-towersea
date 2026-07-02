package com.towersea.data.reload;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.towersea.TowerSeaMod;
import com.towersea.data.codec.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.io.Reader;
import java.util.*;

/**
 * 事务式数据重载器。
 *
 * prepare 阶段完整读取并校验所有候选数据到 Snapshot。
 * apply 阶段只在 Snapshot 全部有效时一次性替换静态 Map。
 * 如果候选数据有错误，保留上一份可用配置，不清空运行时数据。
 */
public class TowerSeaDataReloader extends SimplePreparableReloadListener<TowerSeaDataReloader.Snapshot> {

    public static final Map<ResourceLocation, TowerProfileCodec> TOWER_PROFILES = new HashMap<>();
    public static final Map<ResourceLocation, HeightBandCodec> HEIGHT_BANDS = new HashMap<>();
    public static final Map<ResourceLocation, BridgeRuleCodec> BRIDGE_RULES = new HashMap<>();
    public static final Map<ResourceLocation, StructureCategoryCodec> STRUCTURE_CATEGORIES = new HashMap<>();
    public static final Map<ResourceLocation, PortalRuleCodec> PORTAL_RULES = new HashMap<>();
    public static WorldRulesCodec WORLD_RULES = null;

    /** 候选快照 — prepare 阶段填充，apply 阶段决定是否采纳 */
    static final class Snapshot {
        final Map<ResourceLocation, TowerProfileCodec> towerProfiles = new HashMap<>();
        final Map<ResourceLocation, HeightBandCodec> heightBands = new HashMap<>();
        final Map<ResourceLocation, BridgeRuleCodec> bridgeRules = new HashMap<>();
        final Map<ResourceLocation, StructureCategoryCodec> structureCategories = new HashMap<>();
        final Map<ResourceLocation, PortalRuleCodec> portalRules = new HashMap<>();
        WorldRulesCodec worldRules = null;

        int errorCount = 0;
        final List<String> errorMessages = new ArrayList<>();
    }

    @Override
    protected Snapshot prepare(ResourceManager manager, ProfilerFiller profiler) {
        Snapshot snap = new Snapshot();

        // 加载并校验 tower_profiles
        loadFolder(manager, "towersea/tower_profiles", TowerProfileCodec.CODEC, snap.towerProfiles,
                (id, v) -> v.validate(id.toString()), snap);

        // 加载并校验 height_bands
        loadFolder(manager, "towersea/height_bands", HeightBandCodec.CODEC, snap.heightBands,
                (id, v) -> v.validate(id.toString()), snap);

        // 加载并校验 bridge_rules
        loadFolder(manager, "towersea/bridge_rules", BridgeRuleCodec.CODEC, snap.bridgeRules,
                (id, v) -> v.validate(id.toString()), snap);

        // 加载并校验 structure_categories
        loadFolder(manager, "towersea/structure_categories", StructureCategoryCodec.CODEC, snap.structureCategories,
                (id, v) -> v.validate(id.toString()), snap);

        // 加载并校验 portal_rules
        loadFolder(manager, "towersea/portal_rules", PortalRuleCodec.CODEC, snap.portalRules,
                (id, v) -> v.validate(id.toString()), snap);

        // 加载 world_rules（单文件）
        Map<ResourceLocation, WorldRulesCodec> worldRulesMap = new HashMap<>();
        loadFolder(manager, "towersea/world_rules", WorldRulesCodec.CODEC, worldRulesMap,
                (id, v) -> v.validate(id.toString()), snap);
        if (!worldRulesMap.isEmpty()) {
            snap.worldRules = worldRulesMap.values().iterator().next();
        }

        return snap;
    }

    @Override
    protected void apply(Snapshot snap, ResourceManager manager, ProfilerFiller profiler) {
        if (snap.errorCount > 0) {
            TowerSeaMod.LOGGER.error("[TowerSea] Reload aborted: {} configuration error(s) found.", snap.errorCount);
            TowerSeaMod.LOGGER.error("[TowerSea] Keeping previous configuration ({} tower profiles, {} bridge rules, etc.)",
                    TOWER_PROFILES.size(), BRIDGE_RULES.size());
            for (String msg : snap.errorMessages) {
                TowerSeaMod.LOGGER.error("[TowerSea]   - {}", msg);
            }
            return;
        }

        // 全部校验通过 — 原子替换
        TOWER_PROFILES.clear();
        TOWER_PROFILES.putAll(snap.towerProfiles);

        HEIGHT_BANDS.clear();
        HEIGHT_BANDS.putAll(snap.heightBands);

        BRIDGE_RULES.clear();
        BRIDGE_RULES.putAll(snap.bridgeRules);

        STRUCTURE_CATEGORIES.clear();
        STRUCTURE_CATEGORIES.putAll(snap.structureCategories);

        PORTAL_RULES.clear();
        PORTAL_RULES.putAll(snap.portalRules);

        WORLD_RULES = snap.worldRules;

        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} tower profiles.", TOWER_PROFILES.size());
        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} height bands.", HEIGHT_BANDS.size());
        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} bridge rules.", BRIDGE_RULES.size());
        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} structure categories.", STRUCTURE_CATEGORIES.size());
        TowerSeaMod.LOGGER.info("[TowerSea] Loaded {} portal rules.", PORTAL_RULES.size());
        TowerSeaMod.LOGGER.info("[TowerSea] World rules loaded: {}", WORLD_RULES != null);
    }

    @FunctionalInterface
    private interface Validator<T> {
        boolean validate(ResourceLocation id, T value);
    }

    private <T> void loadFolder(ResourceManager manager, String path, Codec<T> codec,
                                 Map<ResourceLocation, T> target, Validator<T> validator, Snapshot snap) {
        manager.listResources(path, id -> id.getPath().endsWith(".json")).forEach((id, resource) -> {
            try (Reader reader = resource.openAsReader()) {
                JsonElement json = JsonParser.parseReader(reader);
                codec.parse(JsonOps.INSTANCE, json).resultOrPartial(err -> {
                    snap.errorCount++;
                    snap.errorMessages.add("Parse error in " + id + ": " + err);
                    TowerSeaMod.LOGGER.error("[TowerSea] Parse error in {}: {}", id, err);
                }).ifPresent(value -> {
                    if (validator.validate(id, value)) {
                        target.put(id, value);
                    } else {
                        snap.errorCount++;
                        snap.errorMessages.add("Validation failed for " + id);
                        TowerSeaMod.LOGGER.warn("[TowerSea] Skipped invalid entry: {}", id);
                    }
                });
            } catch (Exception e) {
                snap.errorCount++;
                snap.errorMessages.add("Failed to load " + id + ": " + e.getMessage());
                TowerSeaMod.LOGGER.error("[TowerSea] Failed to load {}: {}", id, e.getMessage());
            }
        });
    }
}
