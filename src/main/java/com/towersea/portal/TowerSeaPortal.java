package com.towersea.portal;

import com.towersea.TowerSeaMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TowerSeaMod.MOD_ID)
public class TowerSeaPortal {
    // 传送门逻辑主要由数据包函数驱动 (advancement + mcfunction)
    // Java 层仅提供安全传送工具方法

    public static void safeTeleport(ServerPlayer player, ServerLevel targetLevel, BlockPos targetPos) {
        if (player == null || targetLevel == null || targetPos == null) return;
        player.teleportTo(targetLevel,
                targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5,
                player.getYRot(), player.getXRot());
        TowerSeaMod.LOGGER.debug("[TowerSea] Teleported {} to {} at {}",
                player.getName().getString(), targetLevel.dimension().location(), targetPos);
    }

    public static void returnToOverworld(ServerPlayer player) {
        ServerLevel overworld = player.server.getLevel(net.minecraft.world.level.Level.OVERWORLD);
        if (overworld == null) {
            TowerSeaMod.LOGGER.warn("[TowerSea] Overworld not found for return teleport.");
            return;
        }
        BlockPos safePos = findSafePos(overworld, player.blockPosition());
        safeTeleport(player, overworld, safePos);
        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 0, false, true));
        player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 160, 0, false, true));
    }

    private static BlockPos findSafePos(ServerLevel level, BlockPos origin) {
        int x = origin.getX();
        int z = origin.getZ();
        for (int y = 320; y > 60; y--) {
            BlockPos pos = new BlockPos(x, y, z);
            if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {
                return pos;
            }
        }
        return new BlockPos(x, 128, z);
    }
}
