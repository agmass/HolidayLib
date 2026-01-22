package org.agmas.holidaylib.client.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import foundry.veil.api.client.render.rendertype.VeilRenderType;
import me.shedaniel.autoconfig.AutoConfig;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.agmas.holidaylib.client.events.ModifyPlayerRenderLayer;
import org.agmas.holidaylib.client.events.ModifyPlayerSkinTint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(LivingEntityRenderer.class)
public abstract class HolidaySkinMixin {

    @ModifyArg(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;"), index = 2)
    public boolean translucentMixin(boolean translucent, @Local(argsOnly = true) LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) return translucent;
        if (ModifyPlayerRenderLayer.EVENT.invoker().modify(livingEntity,((LivingEntityRenderer)(Object)this).getTexture(livingEntity)) != null) {
            return true;
        }
        return translucent;
    }
    @Inject(method = "getRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getTexture(Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/Identifier;", shift = At.Shift.AFTER), cancellable = true)
    public void a(LivingEntity entity, boolean showBody, boolean translucent, boolean showOutline, CallbackInfoReturnable<RenderLayer> cir) {
        if (!(entity instanceof PlayerEntity)) return;
        ModifyPlayerRenderLayer.Entry entry = ModifyPlayerRenderLayer.EVENT.invoker().modify(entity, ((LivingEntityRenderer)(Object)this).getTexture(entity));
        if (entry != null) {
            RenderLayer layer = entry.layer;
            if (layer != null) {
                cir.setReturnValue(layer);
                cir.cancel();
            }
        }
    }
    @ModifyArg(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;"), index = 3)
    public boolean outlineMixin(boolean outline, @Local(argsOnly = true) LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) return outline;
        if (ModifyPlayerRenderLayer.EVENT.invoker().modify(livingEntity, ((LivingEntityRenderer)(Object)this).getTexture(livingEntity)) != null) {
            return true;
        }
        return outline;
    }
    @ModifyArg(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"), index = 4)
    public int visibilityMixin(int par3, @Local(argsOnly = true) LivingEntity livingEntity) {
        if (!(livingEntity instanceof PlayerEntity)) return par3;
        Color color = ModifyPlayerSkinTint.EVENT.invoker().modify(livingEntity);
        if (color != null) {
            return color.getRGB();
        }
        return par3;
    }
}
