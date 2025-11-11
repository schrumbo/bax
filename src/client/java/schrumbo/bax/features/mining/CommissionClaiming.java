package schrumbo.bax.features.mining;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import schrumbo.bax.utils.location.Location;
import schrumbo.bax.utils.location.LocationManager;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.utils.InventoryUtils.*;
import static schrumbo.bax.utils.ItemUtils.loreContains;

/**
 * lets the player claim commissions without directly clicking them
 */
public class CommissionClaiming {

    private static int currentIndex = 0;
    private static List<Slot> cachedCompletedCommissions = new ArrayList<>();
    private static String lastGuiTitle = "";

    /**
     * handles whether to claim a commission or close the gui
     * @param screen
     * @param mouseX
     * @param mouseY
     * @param button
     * @return
     */
    public static boolean handleClick(HandledScreen<?> screen, double mouseX, double mouseY, int button) {
        if (!config.easyCommissions)return false;
        if (LocationManager.currentLocation != Location.DwarvenMines && LocationManager.currentLocation != Location.CrystalHollows)return false;
        if (button != 0 && button != 1) return false;
        if (!isInGui(screen, "Commissions")) return false;


        Slot hoveredSlot = getSlotAt(screen, mouseX, mouseY);
        if (hoveredSlot != null && shouldIgnoreSlot(hoveredSlot)) {
            return false;
        }

        updateCache(screen.getScreenHandler());

        if (cachedCompletedCommissions.isEmpty()) {
            closeGui();
            return true;
        }

        Slot targetSlot = cachedCompletedCommissions.get(currentIndex);
        clickSlot(targetSlot);

        currentIndex = (currentIndex + 1) % cachedCompletedCommissions.size();

        return true;
    }

    /**
     * Doesnt Close the gui container if trying to click the Milestones or Filter
     * @param slot
     * @return
     */
    private static boolean shouldIgnoreSlot(Slot slot) {
        ItemStack stack = slot.getStack();
        if (stack.isEmpty()) return false;

        String name = stack.getName().getString();
        return name.contains("Filter") || name.contains("Commission Milestones");
    }


    private static void updateCache(ScreenHandler screenHandler) {
        cachedCompletedCommissions.clear();
        currentIndex = 0;

        for (Slot slot : screenHandler.slots) {
            ItemStack stack = slot.getStack();
            if (stack.isEmpty()) continue;

            if (itemNameContains(stack, "Commission") && loreContains(stack, "COMPLETED")) {
                cachedCompletedCommissions.add(slot);
            }
        }
    }

    public static void reset() {
        currentIndex = 0;
        cachedCompletedCommissions.clear();
        lastGuiTitle = "";
    }
}
