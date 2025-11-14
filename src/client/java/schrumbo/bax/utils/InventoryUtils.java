package schrumbo.bax.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

public class InventoryUtils {

    /**
     * clicks a slot in a gui or inventory
     * @param slot slot to click
     */
    public static void clickSlot(Slot slot) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.interactionManager == null || client.player == null) return;

        client.interactionManager.clickSlot(
                client.player.currentScreenHandler.syncId,
                slot.id,
                0,
                SlotActionType.PICKUP,
                client.player
        );
    }

    /**
     * clicks slot at an index
     * @param slotIndex
     */
    public static void clickSlotAt(int slotIndex) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        ScreenHandler handler = client.player.currentScreenHandler;
        if (handler == null) return;

        assert client.interactionManager != null;
        client.interactionManager.clickSlot(
                handler.syncId,
                slotIndex,
                0,
                SlotActionType.PICKUP,
                client.player
        );
    }

    /**
     * checks if a certain slot is hovered
     * @param screen
     * @param slot
     * @param mouseX
     * @param mouseY
     * @return true if mouse is over the given slot
     */
    public static boolean isMouseOverSlot(HandledScreen<?> screen, Slot slot, double mouseX, double mouseY) {
        try {
            int screenX = (Integer) HandledScreen.class.getDeclaredField("x").get(screen);
            int screenY = (Integer) HandledScreen.class.getDeclaredField("y").get(screen);

            int x = screenX + slot.x;
            int y = screenY + slot.y;
            return mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * gets the currently hovered slot
     * @param screen
     * @param mouseX
     * @param mouseY
     * @return slot which is currently hovered
     */
    public static Slot getSlotAt(HandledScreen<?> screen, double mouseX, double mouseY) {
        for (Slot slot : screen.getScreenHandler().slots) {
            if (isMouseOverSlot(screen, slot, mouseX, mouseY)) {
                return slot;
            }
        }
        return null;
    }

    /**
     * closes the gui which the player is currently in
     */
    public static void closeGui() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.currentScreen != null) {
            client.player.closeHandledScreen();
        }
    }

    /**
     * checks if an item name contains a string
     * @param stack
     * @param itemName
     * @return true if the item has the string in its name
     */
    public static boolean itemNameContains(ItemStack stack, String itemName) {
        return stack.getName().getString().contains(itemName);
    }

    /**
     * checks if the current gui has a certain name
     * @param screen
     * @param string
     * @return true if the string is contained in the gui title
     */
    public static boolean isInGui(HandledScreen<?> screen, String string) {
        return screen.getTitle().getString().contains(string);
    }

    public static boolean isInGui() {
        return isInGui(null);
    }

    public static boolean isInGui(String string) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen == null) return false;

        if (client.currentScreen instanceof HandledScreen<?> handledScreen) {
            return handledScreen.getTitle().getString().contains(string);
        }

        return false;
    }

}
