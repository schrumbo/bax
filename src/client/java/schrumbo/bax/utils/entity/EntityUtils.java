package schrumbo.bax.utils.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;
import schrumbo.bax.Bax;

import java.util.ArrayList;
import java.util.List;

public class EntityUtils {

    public static final MinecraftClient client = MinecraftClient.getInstance();

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

    /**
     * gets all entities of a specific Type
     * @param entityClass
     * @return
     * @param <T>
     */
    public static <T extends Entity> List<T> getEntitiesByType(Class<T> entityClass) {
        List<T> result = new ArrayList<>();
        if (client.world == null) return result;

        for (Entity e : client.world.getEntities()) {
            if (entityClass.isInstance(e)) {
                result.add(entityClass.cast(e));
            }
        }

        return result;
    }


    /**
     * prints out every player entity
     */
    public static void debugPlayerEntities(){
        if (client.world == null) return;
        for (Entity entity : client.world.getEntities()) {

            if (entity.getName().equals(Text.literal("schrumbo"))) continue;
            String entityName = entity.getName().getString();
            if (entityName == null)continue;

            Text entityCustomName = entity.getCustomName();
            if (entityCustomName != null)continue;

            if (!(entity instanceof PlayerEntity))continue;
            entityInformation(entity);
        }
    }

    public static void debugWitherEntities(){
        if (client.world == null) return;
        for (Entity entity : client.world.getEntities()) {

            if (entity.getName().equals(Text.literal("schrumbo"))) continue;
            String entityName = entity.getName().getString();
            if (entityName == null)continue;

            Text entityCustomName = entity.getCustomName();
            //if (entityCustomName != null)continue;

            if (!(entity instanceof WitherEntity))continue;

            entityInformation(entity);
        }
    }

    /**
     * return exactly one armorstand with a given name
     * @param name wanted name
     * @return found armorstand - if couldnt find matching entity return null
     */
    public static Entity getArmorStand(String name){
        if(client.world == null) return null;

        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof ArmorStandEntity))continue;
            if(entity.getName().getString().toLowerCase().contains(name.toLowerCase())){
                return entity;
            }
        }
        return null;
    }

    private static void entityInformation(Entity entity){
        String entityName = entity.getName().getString();
        Bax.LOGGER.info("__________________________________________________");
        Bax.LOGGER.info(String.valueOf( entity.getType()));
        Bax.LOGGER.info("Dimensions: {}", entity.getDimensions(entity.getPose()));
        Bax.LOGGER.info("EntityName: {}", entityName);
        Bax.LOGGER.info("Entity Custom Name: {}", entity.getCustomName());
        Bax.LOGGER.info("Velocity: {}", entity.getVelocity());
        for (String commandTag : entity.getCommandTags()){
            Bax.LOGGER.info("Command Tag: {}", commandTag);
        }
        Bax.LOGGER.info(String.valueOf(entity));
        Bax.LOGGER.info("__________________________________________________");
    }

}
