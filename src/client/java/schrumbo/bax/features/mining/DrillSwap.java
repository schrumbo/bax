package schrumbo.bax.features.mining;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

/**
 * swaps to the drill after opening the royal pigeon
 */
public class DrillSwap {
    private static final MinecraftClient mc = MinecraftClient.getInstance();


    public static void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (stack.getName().getString().contains("Royal Pigeon")) {
                scheduleSlotSwitch();
            }

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (stack.getName().getString().contains("Royal Pigeon")) {
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
                Thread.sleep(50);
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
        int fallbackDrill = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;

            String name = stack.getName().getString();
            if (!name.contains("Drill")) continue;

            if (name.contains("Refined")) {
                return i;
            }

            if (fallbackDrill == -1) {
                fallbackDrill = i;
            }
        }

        return fallbackDrill;
    }
}