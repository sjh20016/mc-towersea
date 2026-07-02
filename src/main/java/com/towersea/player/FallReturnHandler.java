package com.towersea.player;

import com.towersea.TowerSeaMod;
import com.towersea.data.reload.TowerSeaDataReloader;
import com.towersea.world.dimension.TowerSeaDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TowerSeaMod.MOD_ID)
public class FallReturnHandler {
    // 备用安全网：当数据包未加载时，Java 层仍然保障玩家安全
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.TickEvent.Phase.END) return;
        if (!(event.player instanceof ServerPlayer player)) return;
        if (player.level().dimension() != TowerSeaDimensions.TOWERSEA_LEVEL) return;

        // 如果数据包规则已加载，由数据包处理
        if (TowerSeaDataReloader.WORLD_RULES != null) return;

        // 无数据包时的硬编码安全网
        if (player.getY() <= -240) {
            ServerLevel overworld = player.server.getLevel(net.minecraft.world.level.Level.OVERWORLD);
            if (overworld == null) return;
            BlockPos pos = new BlockPos(0, 360, 0);
            player.teleportTo(overworld, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    player.getYRot(), player.getXRot());
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 0, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 160, 0, false, true));
            TowerSeaMod.LOGGER.info("[TowerSea] Fallback fall return triggered for {}.",
                    player.getName().getString());
        }
    }
}
