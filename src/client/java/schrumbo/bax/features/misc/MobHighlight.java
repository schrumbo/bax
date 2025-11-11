package schrumbo.bax.features.misc;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.bax.BaxClient.highlightConfig;
import static schrumbo.bax.utils.render.HighlightUtils.getEntityByArmorStandName;
import static schrumbo.bax.utils.render.RenderUtils.renderHitbox;

/**
 * highlights all mobs specified in the mob highlight list
 */
public class MobHighlight {
    public static void register(){
        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            List<String> names = highlightConfig.getHighlightList();

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null || client.player == null) return;

            Camera camera = context.camera();
            MatrixStack matrices = context.matrixStack();

            List<Entity> entities = new ArrayList<>();
            for (String name : names){
                entities.addAll(getEntityByArmorStandName(name));
            }
            for (Entity entity : entities){
                renderHitbox(entity, camera, matrices);
            }
        });
    }
}
