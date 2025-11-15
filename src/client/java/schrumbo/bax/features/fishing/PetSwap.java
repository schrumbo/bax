package schrumbo.bax.features.fishing;

import net.minecraft.item.ItemStack;
import schrumbo.bax.Bax;
import schrumbo.bax.config.Config;
import schrumbo.bax.utils.*;

import static schrumbo.bax.BaxClient.client;

/**
 * swaps to a pet after casting the rod
 */
public class PetSwap {

    public static void changePet(Config.Pet pet){
        if (client.player == null)return;
        ItemStack itemStack = client. player.getMainHandStack();
        if (!ItemUtils.loreContains(itemStack, "pet")){
            Bax.LOGGER.info("Can't add non-pets to the petkeys list");
            return;
        }
        pet.changeEntry(StringUtils.noColorCodes(itemStack.getItemName().getString()), ItemUtils.getItemUUID(itemStack));
    }


}









































