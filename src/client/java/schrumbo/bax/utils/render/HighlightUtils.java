package schrumbo.bax.utils.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.List;

public class HighlightUtils {


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
