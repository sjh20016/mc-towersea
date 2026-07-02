package com.towersea.data.codec;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.towersea.TowerSeaMod;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Optional;

public record PortalRuleCodec(
        ResourceLocation id,
        String frameBlockTag,
        String ignitionItemTag,
        String portalColor,
        String targetDimension,
        int cooldownTicks,
        Optional<String> enterCondition,
        Optional<String> promptText,
        Optional<String> soundEvent,
        Optional<String> particleType
) {
    public static final Codec<PortalRuleCodec> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(PortalRuleCodec::id),
            Codec.STRING.fieldOf("frame_block_tag").forGetter(PortalRuleCodec::frameBlockTag),
            Codec.STRING.fieldOf("ignition_item_tag").forGetter(PortalRuleCodec::ignitionItemTag),
            Codec.STRING.fieldOf("portal_color").forGetter(PortalRuleCodec::portalColor),
            Codec.STRING.fieldOf("target_dimension").forGetter(PortalRuleCodec::targetDimension),
            Codec.INT.fieldOf("cooldown_ticks").forGetter(PortalRuleCodec::cooldownTicks),
            Codec.STRING.optionalFieldOf("enter_condition").forGetter(PortalRuleCodec::enterCondition),
            Codec.STRING.optionalFieldOf("prompt_text").forGetter(PortalRuleCodec::promptText),
            Codec.STRING.optionalFieldOf("sound_event").forGetter(PortalRuleCodec::soundEvent),
            Codec.STRING.optionalFieldOf("particle_type").forGetter(PortalRuleCodec::particleType)
    ).apply(instance, PortalRuleCodec::new));

    public boolean validate(String fileName) {
        boolean valid = true;
        if (cooldownTicks < 0) {
            TowerSeaMod.LOGGER.error("[TowerSea] Field 'cooldown_ticks' must be >= 0 in {} (id={})", fileName, id);
            valid = false;
        }
        return valid;
    }
}
