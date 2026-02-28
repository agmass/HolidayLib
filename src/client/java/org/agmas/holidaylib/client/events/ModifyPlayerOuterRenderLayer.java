package org.agmas.holidaylib.client.events;

import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import static net.fabricmc.fabric.api.event.EventFactory.createArrayBacked;

public interface ModifyPlayerOuterRenderLayer {

    Event<ModifyPlayerOuterRenderLayer> EVENT = createArrayBacked(ModifyPlayerOuterRenderLayer.class, listeners -> (player, texture) -> {
        int highestPriority = -100000;
        Entry layer = null;
        for (ModifyPlayerOuterRenderLayer listener : listeners) {
            Entry layer2 = listener.modify(player,texture);
            if (layer2 != null && layer2.priority > highestPriority) layer = layer2;
        }
        return layer;
    });

    Entry modify(LivingEntity player, Identifier texture);

    class Entry {
        public int priority = 1000;
        public RenderLayer layer;
        public Identifier shaderIdentifier;
    }
}
