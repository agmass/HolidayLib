package org.agmas.holidaylib.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderPhase;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;
import org.agmas.holidaylib.client.config.HolidayConfig;

import java.awt.*;

public class HolidaylibClient implements ClientModInitializer {
    public static RenderLayer solidEmissiveRenderLayer(Identifier texture) {
        RenderLayer.MultiPhaseParameters solidEmissiveRenderLayer = RenderLayer.MultiPhaseParameters.builder().program(RenderLayer.ENTITY_TRANSLUCENT_EMISSIVE_PROGRAM).texture(new RenderPhase.Texture(texture, false, false)).cull(RenderLayer.ENABLE_CULLING).writeMaskState(RenderPhase.ALL_MASK).overlay(RenderPhase.ENABLE_OVERLAY_COLOR).build(false);
        return RenderLayer.of(
                "solidEmissiveRenderLayer",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                1536,
                true,
                false,
                solidEmissiveRenderLayer
        );
    }
    public static RenderLayer translucentMaskedEmissiveRenderLayer(Identifier texture) {
        RenderLayer.MultiPhaseParameters translucentMaskedEmissiveRenderLayer = RenderLayer.MultiPhaseParameters.builder().program(RenderLayer.ENTITY_TRANSLUCENT_EMISSIVE_PROGRAM).texture(new RenderPhase.Texture(texture, false, false)).transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY).cull(RenderLayer.ENABLE_CULLING).writeMaskState(RenderPhase.ALL_MASK).overlay(RenderPhase.ENABLE_OVERLAY_COLOR).build(false);
        return RenderLayer.of(
                "translucentMaskedEmissiveRenderLayer",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                1536,
                true,
                true,
                translucentMaskedEmissiveRenderLayer
        );
    }
    public static RenderLayer tmeRLAdditive(Identifier texture) {
        RenderLayer.MultiPhaseParameters translucentMaskedEmissiveRenderLayer = RenderLayer.MultiPhaseParameters.builder().program(RenderLayer.ENTITY_TRANSLUCENT_EMISSIVE_PROGRAM).texture(new RenderPhase.Texture(texture, false, false)).transparency(RenderLayer.ADDITIVE_TRANSPARENCY).cull(RenderLayer.ENABLE_CULLING).writeMaskState(RenderPhase.ALL_MASK).overlay(RenderPhase.ENABLE_OVERLAY_COLOR).build(false);
        return RenderLayer.of(
                "translucentMaskedEmissiveRenderLayer",
                VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL,
                VertexFormat.DrawMode.QUADS,
                1536,
                true,
                true,
                translucentMaskedEmissiveRenderLayer
        );
    }

    public static RenderLayer shaderFallbackLayer(RenderLayer original, RenderLayer fallback) {
        HolidayConfig config = AutoConfig.getConfigHolder(HolidayConfig.class).getConfig();
        if (config.disableShaders || IrisApi.getInstance().isShaderPackInUse()) return fallback;
        return original;
    }

    public static boolean canUseShaders() {
        HolidayConfig config = AutoConfig.getConfigHolder(HolidayConfig.class).getConfig();
        return !config.disableShaders && !IrisApi.getInstance().isShaderPackInUse();
    }
    public static Color shaderFallbackColor(Color original, Color fallback) {
        HolidayConfig config = AutoConfig.getConfigHolder(HolidayConfig.class).getConfig();
        if (config.disableShaders || IrisApi.getInstance().isShaderPackInUse()) return fallback;
        return original;
    }



    @Override
    public void onInitializeClient() {
        AutoConfig.register(HolidayConfig.class, JanksonConfigSerializer::new);
    }
}
