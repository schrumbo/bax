package schrumbo.bax.features.mining;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.utils.InventoryUtils.*;
import static schrumbo.bax.utils.InventoryUtils.clickSlot;
import static schrumbo.bax.utils.ItemUtils.loreContains;

/**
 * lets the player enter a mineshaft without having to precisely click the right slot
 */
public class MineshaftEnter {
    private static Slot targetSlot = null;

    public static boolean handleClick(HandledScreen<?> screen, double mouseX, double mouseY, int button) {
        if (!config.easyShaftEnter)return false;

        if (button != 0 && button != 1) return false;
        if (!isInGui(screen, "Glacite Mineshaft")) return false;


        updateCache(screen.getScreenHandler());

        if (targetSlot == null)return false;
        clickSlot(targetSlot);

        return true;
    }

    private static void updateCache(ScreenHandler screenHandler) {
        for (Slot slot : screenHandler.slots) {
            ItemStack stack = slot.getStack();
            if (stack.isEmpty()) continue;
            if (itemNameContains(stack, "Enter Mineshaft")) {
                targetSlot = slot;
            }
        }
    }



}
