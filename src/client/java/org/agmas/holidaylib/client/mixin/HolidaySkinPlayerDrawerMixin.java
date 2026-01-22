package org.agmas.holidaylib.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import foundry.veil.api.client.render.VeilRenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.agmas.holidaylib.client.events.ModifyPlayerRenderLayer;
import org.agmas.holidaylib.client.events.ModifyPlayerSkinTint;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(PlayerListHud.class)
public class HolidaySkinPlayerDrawerMixin {

    @WrapOperation(method = "render",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/PlayerSkinDrawer;draw(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/util/Identifier;IIIZZ)V"))
    void visibilityMixin2(DrawContext context, Identifier texture, int x, int y, int size, boolean hatVisible, boolean upsideDown, Operation<Void> original, @Local PlayerEntity player) {


        if (player != null) {
            ModifyPlayerRenderLayer.Entry entry = ModifyPlayerRenderLayer.EVENT.invoker().modify(player, texture);
            Color skinTint = ModifyPlayerSkinTint.EVENT.invoker().modify(player);

            float u1 = 0.125f;
            float u2 = 0.25f;

            float u1h = 0.625f;
            float u2h = 0.75f;
            if (FabricLoader.getInstance().isModLoaded("veil")) {
                if (entry != null) {
                    if (entry.shaderIdentifier != null) {

                        if (skinTint == null) skinTint = new Color(-1);
                        RenderSystem.setShaderTexture(0, texture);
                        VeilRenderSystem.setShader(entry.shaderIdentifier);
                        BufferBuilder bufferBuilder = bufferSection(context, x, y, u1, u2, u1, u2, skinTint.getRGB());
                        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                        bufferBuilder = bufferSection(context, x, y, u1h, u2h, u1, u2, skinTint.getRGB());
                        RenderSystem.enableBlend();
                        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                        RenderSystem.disableBlend();
                        return;
                    }
                }
            }
            if (skinTint != null) {

                RenderSystem.setShaderTexture(0, texture);
                RenderSystem.setShader(GameRenderer::getPositionTexProgram);
                BufferBuilder bufferBuilder = bufferSection(context, x, y, u1, u2, u1, u2, skinTint.getRGB());
                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                bufferBuilder = bufferSection(context, x, y, u1h, u2h, u1, u2, skinTint.getRGB());
                RenderSystem.enableBlend();
                BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
                RenderSystem.disableBlend();
            }
        }
        original.call(context,texture,x,y,size,hatVisible,upsideDown);
    }

    @Unique
    private BufferBuilder bufferSection(DrawContext context, int x, int y, float u1, float u2, float v1, float v2, int color) {
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
        bufferBuilder.vertex(matrix4f, x, y, 0).texture(u1, v1).color(color).light(-1);
        bufferBuilder.vertex(matrix4f, x, y+ 8, 0).texture(u1, v2).color(color).light(-1);
        bufferBuilder.vertex(matrix4f, x + 8, y+ 8, 0).texture(u2, v2).color(color).light(-1);
        bufferBuilder.vertex(matrix4f, x + 8, y, 0).texture(u2, v1).color(color).light(-1);
        return bufferBuilder;
    }
}
