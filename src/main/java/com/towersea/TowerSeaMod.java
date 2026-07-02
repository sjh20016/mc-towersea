package com.towersea;

import com.mojang.logging.LogUtils;
import com.towersea.command.TowerSeaCommands;
import com.towersea.data.reload.TowerSeaDataReloader;
import com.towersea.world.dimension.TowerSeaDimensions;
import com.towersea.world.feature.TowerSeaFeatures;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TowerSeaMod.MOD_ID)
public class TowerSeaMod {
    public static final String MOD_ID = "towersea";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TowerSeaMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        TowerSeaDimensions.register();
        TowerSeaFeatures.FEATURES.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("[TowerSea] Common setup complete.");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        TowerSeaCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new TowerSeaDataReloader());
    }
}
