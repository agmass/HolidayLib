package org.agmas.holidaylib.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface ModifyPlayerSkinTint {

    Event<ModifyPlayerSkinTint> EVENT = createArrayBacked(ModifyPlayerSkinTint.class, listeners -> (player) -> {
        Color mixed = null;
        for (ModifyPlayerSkinTint listener : listeners) {
            Color mult = listener.modify(player);
            if (mult != null) {
                if (mixed == null) {
                    mixed = mult;
                    continue;
                }
                //mixed = new Color(
                //        (int) (Math.clamp((long) (mixed.getRed() / 255f) * (mult.getRed()/255f),0,1) * 255),
                 //       (int) (Math.clamp((long) (mixed.getGreen() / 255f) * (mult.getGreen()/255f),0,1) * 255),
                 //       (int) (Math.clamp((long) (mixed.getBlue() / 255f) * (mult.getBlue()/255f),0,1) * 255),
                 //       mult.getAlpha());
            }
        }
        return mixed;
    });

    Color modify(LivingEntity player);

}
