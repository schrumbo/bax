package schrumbo.bax.features.mining;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.bax.utils.ChatUtils.modMessage;

public class CommissionClaiming {

    private static int currentIndex = 0;
    private static List<Slot> cachedCompletedCommissions = new ArrayList<>();
    private static String lastGuiTitle = "";

    public static boolean handleClick(HandledScreen<?> screen, double mouseX, double mouseY, int button) {
        if (button != 0) return false;
        if (!isCommissionsGui(screen)) return false;

        String currentTitle = screen.getTitle().getString();
        if (!currentTitle.equals(lastGuiTitle)) {
            updateCache(screen.getScreenHandler());
            lastGuiTitle = currentTitle;
        }

        if (cachedCompletedCommissions.isEmpty()) {
            closeGui();
            return true;
        }

        Slot targetSlot = cachedCompletedCommissions.get(currentIndex);
        clickSlot(targetSlot);


        currentIndex = (currentIndex + 1) % cachedCompletedCommissions.size();

        updateCache(screen.getScreenHandler());

        return true;
    }

    private static void updateCache(ScreenHandler screenHandler) {
        cachedCompletedCommissions.clear();
        currentIndex = 0;

        for (Slot slot : screenHandler.slots) {
            ItemStack stack = slot.getStack();
            if (stack.isEmpty()) continue;

            if (isCommissionItem(stack) && isCompleted(stack)) {
                cachedCompletedCommissions.add(slot);
            }
        }
    }

    private static boolean isCommissionItem(ItemStack stack) {
        return stack.getName().getString().contains("Commission");
    }

    private static boolean isCompleted(ItemStack stack) {
        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore == null) return false;

        for (Text line : lore.lines()) {
            if (line.getString().contains("COMPLETED")) {
                modMessage(stack.getName().toString() + "is ready to claim!");
                return true;
            }
        }

        return false;
    }

    private static boolean isCommissionsGui(HandledScreen<?> screen) {
        return screen.getTitle().getString().contains("Commissions");
    }

    private static void clickSlot(Slot slot) {
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

    private static void closeGui() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.closeHandledScreen();
        }
    }

    public static void reset() {
        currentIndex = 0;
        cachedCompletedCommissions.clear();
        lastGuiTitle = "";
    }
}
