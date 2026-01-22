package org.agmas.holidaylib.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.agmas.holidaylib.client.events.ModifyPlayerRenderLayer;
import org.agmas.holidaylib.client.events.ModifyPlayerSkinTint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(PlayerEntityRenderer.class)
public class HolidaySkinPlayerMixin {

    @Inject(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/PlayerEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", shift = At.Shift.AFTER), cancellable = true)
    public void visibilityMixin2(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve, CallbackInfo ci, @Local(argsOnly = true) AbstractClientPlayerEntity livingEntity) {

        Color color = ModifyPlayerSkinTint.EVENT.invoker().modify(livingEntity);
        ModifyPlayerRenderLayer.Entry entry = ModifyPlayerRenderLayer.EVENT.invoker().modify(player, ((LivingEntityRenderer)(Object)this).getTexture(player));
        if (entry != null) {
            RenderLayer layer = entry.layer;
            if (layer != null) {
                ci.cancel();
                arm.pitch = 0.0F;
                if (color == null) color = new Color(-1);
                arm.render(matrices, vertexConsumers.getBuffer(layer), light, OverlayTexture.DEFAULT_UV, color.getRGB());
                sleeve.pitch = 0.0F;
                sleeve.render(matrices, vertexConsumers.getBuffer(layer), light, OverlayTexture.DEFAULT_UV, color.getRGB());
            } else if (color != null) {
                ci.cancel();
                arm.pitch = 0.0F;
                arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(player.getSkinTextures().texture())), light, OverlayTexture.DEFAULT_UV, color.getRGB());
                sleeve.pitch = 0.0F;
                sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(player.getSkinTextures().texture())), light, OverlayTexture.DEFAULT_UV, color.getRGB());
            }
        }
    }
}
