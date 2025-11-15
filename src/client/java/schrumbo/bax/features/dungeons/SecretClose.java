package schrumbo.bax.features.dungeons;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import schrumbo.bax.utils.InventoryUtils;
import schrumbo.bax.utils.Utils;
import schrumbo.bax.utils.location.Location;

import java.util.Timer;
import java.util.TimerTask;

import static schrumbo.bax.BaxClient.client;
import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.utils.InventoryUtils.closeGui;
import static schrumbo.bax.utils.InventoryUtils.isInGui;
import static schrumbo.bax.utils.location.LocationManager.currentLocation;

/**
 * closes a secret chest after clicking a mouse button
 */
public class SecretClose {
    private static boolean enabled = config.getCloseSecrets();


    /**
     * closes a chest after clicking, doesnt need a delay because it closes on user input
     * @param screen
     */
    public static boolean handleClick(HandledScreen<?> screen, double mouseX, double mouseY, int button){
        if (currentLocation != Location.Dungeon)return false;
        if (button != 0 && button != 1) return false;
        if (!enabled)return false;
        if (!(isInGui(screen, "Chest")))return false;

        closeGui();
        return true;
    }
}
