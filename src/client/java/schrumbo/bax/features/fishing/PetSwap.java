package schrumbo.bax.features.fishing;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.item.ItemStack;
import schrumbo.bax.Bax;
import schrumbo.bax.utils.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.utils.InventoryUtils.*;

/**
 * swaps to a pet after casting the rod
 */
public class PetSwap {
    private static final String throttledMessage = "This menu has been throttled! Please slow down...";
    private static final String petUUID = config.getPetUUID();

    private static boolean rodWasThrown = false;
    private static boolean shouldSkip = false;

    /**
     * entrypoint
     */
    public static void register(){
        onChatMessage();
        onRodThrow();
    }

    /**
     * used to detect if the pet menu got throttled
     */
    private static void onChatMessage(){
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            String messageString = StringUtils.noColorCodes(message.getString()).toLowerCase();
            if (messageString.equals(throttledMessage.toLowerCase())){
                shouldSkip = true;
                Bax.LOGGER.warn("Throttled, skipping next");
                ChatUtils.modMessage("Throttled!");
            }
        });
    }

    /**
     * gets triggered after casting the rod
     */
    private static void onRodThrow(){
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!config.getPetSwap())return;
            if (!isInFishingArmor())return;
            if (client.player == null)return;
            if (!ItemUtils.holdingRod()) return;

            boolean hasFishHook = client.player.fishHook != null;

            if (hasFishHook && !rodWasThrown) {
                rodWasThrown = true;
                Utils.runAfterDelay(PetSwap::openMenu, 450);
                Utils.runAfterDelay(PetSwap::swapToPet, 830);
            } else if (!hasFishHook) {
                rodWasThrown = false;
            }
        });
    }

    /**
     * opens pet menu
     */
    private static void openMenu(){
        if (shouldSkip){
            shouldSkip = false;
            return;
        }
        ChatUtils.sendCommand("pets");
    }

    /**
     * swaps to a pet which matches the given uuid
     */
    private static void swapToPet(){
        if (!isInGui( "Pets")){
            Bax.LOGGER.warn("not in pet menu, probably due to being throttled");
            return;
        }
        if (petUUID.isEmpty()){
            ChatUtils.modMessage("Please choose a pet first: /bax petswap");
            InventoryUtils.closeGui();
            return;
        }
        int slotIndex = ItemUtils.getSlotByUUID(petUUID);
        if (slotIndex == -1){
            ChatUtils.modMessage("Couldn't find matching pet in your petmenu");
            Bax.LOGGER.info("Couldnt find pet with the UUID: {} in the petmenu", petUUID);
        }
        Bax.LOGGER.info("Swapping to slot: {}", slotIndex);
        clickSlotAt(slotIndex);
    }

    /**
     * checks if the player is currently wearing a fishing related armor set
     * @return true if in fishing armor
     */
    private static boolean isInFishingArmor(){
        List<ItemStack> armor = ItemUtils.getPlayerArmor();
        if (armor.isEmpty())return false;

        for (ItemStack item : armor){
            if (ItemUtils.loreContains(item, "Sea Creature Chance")){
                return true;
            }
        }
        return false;
    }
}









































