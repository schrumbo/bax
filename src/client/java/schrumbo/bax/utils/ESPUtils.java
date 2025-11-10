package schrumbo.bax.utils;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.utils.RenderUtils.renderHitbox;

public class ESPUtils {


    /**
     * renders the hitbox for every mod with the specified name through walls
     */
    public static void renderESPByName(){
        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            List<String> names = new ArrayList<>();
            if (config.maniacESP){
                names.add("Glacite Caver");
                names.add("Glacite Mage");
                names.add("Glacite Bowman");
                names.add("Glacite Mutt");
            }
            if (config.starredESP){
                names.add("✯");
            }
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

    /**
     * gets alist of all entities belonging to a specified name
     * @param name
     * @return List of entities with the wanted name
     */
    public static List<Entity> getEntityByArmorStandName( String name) {
        List<Entity> result = new ArrayList<>();

        MinecraftClient client = MinecraftClient.getInstance();
        assert client.world != null;
        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof ArmorStandEntity armorStand)) continue;
            if (!armorStand.hasCustomName()) continue;

            Text customName = armorStand.getCustomName();
            if (customName == null) continue;

            if (customName.getString().contains(name)) {

                Box searchBox = armorStand.getBoundingBox().expand(0.1, 2.0, 0.1).offset(0, -1.5, 0);

                for (Entity nearbyEntity : client.world.getOtherEntities(armorStand, searchBox)) {
                    if (nearbyEntity instanceof ArmorStandEntity) continue;
                    if (nearbyEntity == client.player)continue;
                    result.add(nearbyEntity);
                }
            }
        }
        return result;
    }

}
