package schrumbo.bax.utils.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import schrumbo.bax.Bax;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HighlightUtils {

    private static MinecraftClient client = MinecraftClient.getInstance();

    /**
     * gets alist of all entities belonging to a specified name
     * @param name
     * @return List of entities with the wanted name
     */
    public static List<Entity> getEntityByArmorStandName(String name) {
        List<Entity> result = new ArrayList<>();

        assert client.world != null;
        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof ArmorStandEntity armorStand)) continue;
            if (!armorStand.hasCustomName()) continue;

            Text customName = armorStand.getCustomName();
            if (customName == null) continue;

            if (customName.getString().toLowerCase().contains(name)) {

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

    public static List<Entity> getEntityByName(String name) {
        List<Entity> result = new ArrayList<>();

        if (client.world == null) return result;

        for (Entity entity : client.world.getEntities()) {
            if (entity instanceof ArmorStandEntity) continue;
            if (entity == client.player) continue;

            Text entityName = entity.getName();
            if (entityName != null && entityName.getString().toLowerCase().contains(name.toLowerCase())) {
                result.add(entity);
            }
        }
        return result;
    }

    //TODO DOODDO
    public static  List<Entity> getWolfEntities(){
        List<Entity> result = new ArrayList<>();
        if (client.world == null)return result;
        for (Entity e : client.world.getEntities()){
            if (e instanceof WolfEntity){
                result.add(e);
            }
        }

        return result;
    }


    /**
     * prints out every player entity
     */
    public static void debugEntities(){
        if (client.world == null) return;
        for (Entity entity : client.world.getEntities()) {

            if (entity.getName().equals(Text.literal("schrumbo"))) continue;
            String entityName = entity.getName().getString();
            if (entityName == null)continue;

            Text entityCustomName = entity.getCustomName();
            if (entityCustomName != null)continue;

            if (!(entity instanceof PlayerEntity))continue;

            Bax.LOGGER.info("__________________________________________________");
            Bax.LOGGER.info(String.valueOf( entity.getType()));
            Bax.LOGGER.info("EntityName: {}", entityName);
            Bax.LOGGER.info(String.valueOf(entity));
            Bax.LOGGER.info("__________________________________________________");
        }
    }

}
