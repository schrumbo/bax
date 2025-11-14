package schrumbo.bax.features.mining;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import schrumbo.bax.Bax;
import schrumbo.bax.utils.Utils;
import schrumbo.bax.utils.location.Location;

import java.util.Timer;
import java.util.TimerTask;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.utils.location.LocationManager.currentLocation;

/**
 * swaps to the drill after opening the royal pigeon
 */
public class DrillSwap {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    /**
     * registers events for drill swapping. UseBlockCallback is needed because the player places the pigeon if close to a block
     */
    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (currentLocation != Location.DwarvenMines && currentLocation != Location.CrystalHollows && currentLocation != Location.Mineshaft)return ActionResult.PASS;
            if (!config.pigeonDrillSwap)return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);

            if (stack.getName().getString().contains("Royal Pigeon")) {
                scheduleSlotSwitch();
            }

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (currentLocation != Location.DwarvenMines && currentLocation != Location.CrystalHollows && currentLocation != Location.Mineshaft)return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);

            if (config.pigeonDrillSwap && stack.getName().getString().contains("Royal Pigeon")) {
                scheduleSlotSwitch();
            }

            if (config.rodDrillSwap && stack.getName().getString().contains("Rod")){
                scheduleSlotSwitch();
            }

            return ActionResult.PASS;
        });

    }


    /**
     * switches to the drill slot after a short delay to avoid not clicking the royal pigeon
     */
    private static void scheduleSlotSwitch() {

        new Thread(() -> {
            try {
                Thread.sleep(Utils.randomInt(50, 80));
                if (mc.player != null) {
                    int drillSlot = getDrillSlot();
                    if (drillSlot != -1) {
                        mc.execute(() -> mc.player.getInventory().setSelectedSlot(drillSlot));
                    }
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }

    /**
     * gets the drill slot (will always take a refined drill if possible)
     * @return
     */
    private static int getDrillSlot() {
        assert mc.player != null;
        PlayerInventory inv = mc.player.getInventory();
        //if no drill in inventory
        int fallbackDrill = mc.player.getInventory().getSelectedSlot();

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;

            String name = stack.getName().getString();
            if (!name.contains("Drill")) continue;

            if (name.contains("Refined")) {
                return i;
            }

            if (fallbackDrill == mc.player.getInventory().getSelectedSlot()) {
                fallbackDrill = i;
            }
            Bax.LOGGER.info("Drill:" + fallbackDrill);

        }

        return fallbackDrill;
    }
}