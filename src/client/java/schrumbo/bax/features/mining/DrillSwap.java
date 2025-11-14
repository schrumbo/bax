package schrumbo.bax.features.mining;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import schrumbo.bax.Bax;
import schrumbo.bax.utils.ItemUtils;
import schrumbo.bax.utils.StringUtils;
import schrumbo.bax.utils.Utils;
import schrumbo.bax.utils.location.Location;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static schrumbo.bax.BaxClient.client;
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
                switchToDrill();
            }

            return ActionResult.PASS;
        });

        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (currentLocation != Location.DwarvenMines && currentLocation != Location.CrystalHollows && currentLocation != Location.Mineshaft)return ActionResult.PASS;

            ItemStack stack = player.getStackInHand(hand);

            if (config.pigeonDrillSwap && stack.getName().getString().contains("Royal Pigeon")) {
                switchToDrill();
            }

            if (config.rodDrillSwap && stack.getName().getString().contains("Rod")){
                switchToDrill();
            }

            return ActionResult.PASS;
        });

    }

    /**
     * switches to the drill slot after a short delay to avoid not clicking the royal pigeon
     */
    private static void switchToDrill() {
       Utils.runAfterDelay(() -> ItemUtils.switchToSlot(findBestDrill()), 70);
    }


    /**
     * finds the best drill in the hotbar (best drill -> item with the most mining wisdomString)
     * @return slot
     */
    private static int findBestDrill(){
        assert client.player != null;
        Pattern pattern = Pattern.compile("Mining Wisdom: \\+(\\d+)");
        int bestWisdom = 0;
        int bestSlot = -1;

        for (int i = 0; i < 9; i++){
            ItemStack currentItem = client.player.getInventory().getStack(i);

            LoreComponent lore = currentItem.get(DataComponentTypes.LORE);
            if (lore == null) continue;

            for (Text line : lore.lines()) {
                String cleanLine = StringUtils.noColorCodes(line.getString());

                if (cleanLine.toLowerCase().contains("mining wisdom")) {
                    Matcher matcher = pattern.matcher(cleanLine);

                    if (matcher.find()){
                        int currentWisdom = Integer.parseInt(matcher.group(1));
                        Bax.LOGGER.info("[Drill-Swap] Found Tool in slot {} with {} wisdom.", i, currentWisdom);

                        if (currentWisdom > bestWisdom){
                            bestWisdom = currentWisdom;
                            bestSlot = i;
                        }
                    }
                    break;
                }
            }
        }
        return bestSlot;
    }

}