package com.towersea.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.towersea.TowerSeaMod;
import com.towersea.data.reload.TowerSeaDataReloader;
import com.towersea.world.dimension.TowerSeaDimensions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;

public class TowerSeaCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("towersea")
                .requires(src -> src.hasPermission(2))
                .then(Commands.literal("lab")
                        .executes(TowerSeaCommands::cmdLab))
                .then(Commands.literal("tp")
                        .then(Commands.argument("dimension", StringArgumentType.word())
                                .executes(TowerSeaCommands::cmdTp)))
                .then(Commands.literal("reload")
                        .executes(TowerSeaCommands::cmdReload))
                .then(Commands.literal("validate")
                        .executes(TowerSeaCommands::cmdValidate))
                .then(Commands.literal("structure")
                        .then(Commands.literal("place")
                                .then(Commands.argument("structure_id", ResourceLocationArgument.id())
                                        .executes(TowerSeaCommands::cmdStructurePlace)))
                        .then(Commands.literal("list")
                                .executes(TowerSeaCommands::cmdStructureList)
                                .then(Commands.argument("category", StringArgumentType.word())
                                        .executes(TowerSeaCommands::cmdStructureList))))
                .then(Commands.literal("profile")
                        .then(Commands.literal("list")
                                .executes(TowerSeaCommands::cmdProfileList))
                        .then(Commands.literal("inspect")
                                .then(Commands.argument("profile_id", ResourceLocationArgument.id())
                                        .executes(TowerSeaCommands::cmdProfileInspect))))
                .then(Commands.literal("debug")
                        .then(Commands.literal("chunk")
                                .executes(TowerSeaCommands::cmdDebugChunk))
                        .then(Commands.literal("connectors")
                                .executes(TowerSeaCommands::cmdDebugConnectors))));
    }

    private static int cmdLab(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        if (!(src.getEntity() instanceof ServerPlayer player)) {
            src.sendFailure(Component.literal("仅限玩家使用"));
            return 0;
        }
        ServerLevel lab = player.server.getLevel(TowerSeaDimensions.WAREHOUSE_LEVEL);
        if (lab == null) {
            src.sendFailure(Component.literal("仓库维度未加载"));
            return 0;
        }
        player.teleportTo(lab, 0.5, 100, 0.5, player.getYRot(), player.getXRot());
        src.sendSuccess(() -> Component.literal("已传送至仓库维度 (0, 100, 0)"), true);
        return 1;
    }

    private static int cmdTp(CommandContext<CommandSourceStack> ctx) {
        String dim = StringArgumentType.getString(ctx, "dimension");
        CommandSourceStack src = ctx.getSource();
        if (!(src.getEntity() instanceof ServerPlayer player)) {
            src.sendFailure(Component.literal("仅限玩家使用"));
            return 0;
        }
        ResourceLocation dimId = new ResourceLocation(TowerSeaMod.MOD_ID, dim);
        ServerLevel target = player.server.getLevel(ResourceKey.create(Registries.DIMENSION, dimId));
        if (target == null) {
            src.sendFailure(Component.literal("维度不存在: " + dimId));
            return 0;
        }
        player.teleportTo(target, player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
        src.sendSuccess(() -> Component.literal("已传送至维度: " + dimId), true);
        return 1;
    }

    private static int cmdReload(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        src.getServer().getServerResources().managers().getCustomLoader();
        src.sendSuccess(() -> Component.literal("[TowerSea] 配置已重新加载。"), true);
        return 1;
    }

    private static int cmdValidate(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        int errors = 0;
        for (var entry : TowerSeaDataReloader.TOWER_PROFILES.entrySet()) {
            if (!entry.getValue().validate(entry.getKey().toString())) errors++;
        }
        for (var entry : TowerSeaDataReloader.HEIGHT_BANDS.entrySet()) {
            if (!entry.getValue().validate(entry.getKey().toString())) errors++;
        }
        for (var entry : TowerSeaDataReloader.BRIDGE_RULES.entrySet()) {
            if (!entry.getValue().validate(entry.getKey().toString())) errors++;
        }
        if (errors == 0) {
            src.sendSuccess(() -> Component.literal("[TowerSea] 所有配置验证通过。"), false);
        } else {
            int finalErrors = errors;
            src.sendFailure(Component.literal("[TowerSea] 发现 " + finalErrors + " 个配置错误，请查看日志。"));
        }
        return 1;
    }

    private static int cmdStructurePlace(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "structure_id");
        ServerLevel level = src.getLevel();
        Vec3 pos = src.getPosition();
        BlockPos blockPos = BlockPos.containing(pos.x, pos.y, pos.z).relative(src.getHorizontalDirection().getNormal(), 2);
        StructureTemplate template = level.getStructureManager().get(id).orElse(null);
        if (template == null) {
            src.sendFailure(Component.literal("结构未找到: " + id));
            return 0;
        }
        template.placeInWorld(level, blockPos, blockPos, new net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings(), level.random, 2);
        src.sendSuccess(() -> Component.literal("已放置结构: " + id + " 在 " + blockPos), true);
        return 1;
    }

    private static int cmdStructureList(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        src.sendSuccess(() -> Component.literal("[TowerSea] 结构列表功能待完善 — 请使用结构方块或数据包查看。"), false);
        return 1;
    }

    private static int cmdProfileList(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        var profiles = TowerSeaDataReloader.TOWER_PROFILES;
        src.sendSuccess(() -> Component.literal("[TowerSea] 已加载 " + profiles.size() + " 个塔体配置:"), false);
        profiles.keySet().forEach(id -> src.sendSuccess(() -> Component.literal("  - " + id), false));
        return profiles.size();
    }

    private static int cmdProfileInspect(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        ResourceLocation id = ResourceLocationArgument.getId(ctx, "profile_id");
        var profile = TowerSeaDataReloader.TOWER_PROFILES.get(id);
        if (profile == null) {
            src.sendFailure(Component.literal("塔体配置未找到: " + id));
            return 0;
        }
        src.sendSuccess(() -> Component.literal("[TowerSea] 塔体配置: " + id), false);
        src.sendSuccess(() -> Component.literal("  weight: " + profile.weight()), false);
        src.sendSuccess(() -> Component.literal("  minY: " + profile.minY() + ", maxY: " + profile.maxY()), false);
        src.sendSuccess(() -> Component.literal("  minRadius: " + profile.minRadius() + ", maxRadius: " + profile.maxRadius()), false);
        src.sendSuccess(() -> Component.literal("  minHeight: " + profile.minHeight() + ", maxHeight: " + profile.maxHeight()), false);
        src.sendSuccess(() -> Component.literal("  hollow: " + profile.hollowChance() + ", broken: " + profile.brokenChance() + ", cave: " + profile.caveChance()), false);
        src.sendSuccess(() -> Component.literal("  topShape: " + profile.topShape()), false);
        src.sendSuccess(() -> Component.literal("  baseBlockTag: " + profile.baseBlockTag()), false);
        src.sendSuccess(() -> Component.literal("  surfaceBlockTag: " + profile.surfaceBlockTag()), false);
        src.sendSuccess(() -> Component.literal("  allowedHeightBands: " + profile.allowedHeightBands()), false);
        return 1;
    }

    private static int cmdDebugChunk(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        BlockPos pos = BlockPos.containing(src.getPosition());
        src.sendSuccess(() -> Component.literal("[TowerSea] 当前区块: " + pos.getX() / 16 + ", " + pos.getZ() / 16 + " | 高度: " + pos.getY()), false);
        return 1;
    }

    private static int cmdDebugConnectors(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack src = ctx.getSource();
        src.sendSuccess(() -> Component.literal("[TowerSea] 连接点调试信息待实现。"), false);
        return 1;
    }
}
