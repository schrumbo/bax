package schrumbo.bax.utils.location;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import schrumbo.bax.Bax;
import schrumbo.bax.utils.TablistUtils;

import java.util.List;

import static schrumbo.bax.utils.StringUtils.noColorCodes;
import static schrumbo.bax.utils.StringUtils.removePrefix;

/**
 *
 */
public class LocationManager {
    public static Location currentLocation = null;

    public static void register(){
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) ->{
            new Thread(() -> {
                try {
                    //in case of lag
                    Thread.sleep(3000);
                    updateLocation();
                } catch (InterruptedException ignored) {}
            }).start();
        });
    }

    /**
     * gets the players current Location in Skyblock via the tablist
     */
    public static void updateLocation(){
        List<String> lines = TablistUtils.getTablistLines();
        for (String line : lines){

            String cleanLine = noColorCodes(line);
            cleanLine = removePrefix(cleanLine, "Area: ");

            Location loc = Location.isLocation(cleanLine);
            if (loc != null) {
                Bax.LOGGER.info("Location: " + loc.getDisplayName());
                currentLocation = loc;
                return;
            }
        }
        Bax.LOGGER.info("Couldn't find location");
    }




}
