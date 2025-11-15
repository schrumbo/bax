package schrumbo.bax.features.chat;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.sound.SoundEvents;
import schrumbo.bax.utils.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static schrumbo.bax.BaxClient.config;

/**
 * sends a title to the player if any pet gets level 100 or level 200
 */
public class PetNotification {

    public static boolean enabled = config.getPetNotification();
    private static final Pattern pattern = Pattern.compile("Your (.+?) leveled up to level (\\d+)!");

    public static void register(){
        //note for me: GAME for multiplayer messages and CHAT for singleplayer
        ClientReceiveMessageEvents.GAME.register((message, overlay) -> {
            if (!enabled)return;
            String pet = "";
            String level = "";
            String messageString = message.getString();
            Matcher matcher = pattern.matcher(messageString);

            if (matcher.find()){
                pet = matcher.group(1);
                level = matcher.group(2);
            }

            if (level.equals("100") || level.equals("200")) {
                Utils.playSound(SoundEvents.BLOCK_BEACON_DEACTIVATE);
                Utils.sendTitle( "§fPet is Maxed!",  "§8[§b" + pet + "§8]" , 100);
            }

        });

    }
}
