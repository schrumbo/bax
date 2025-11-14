package schrumbo.bax.utils;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;

import static schrumbo.bax.BaxClient.client;

public class Utils {

    /**
     * generates random int between min and max
     * @param min
     * @param max
     * @return
     */
    public static int randomInt(int min, int max){
        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }

    /**
     * plays a sound to the player
     * @param soundEvent
     */
    public static void playSound(SoundEvent soundEvent){
        assert client.player != null;
        client.player.playSound(soundEvent, client.options.getSoundVolume(SoundCategory.MASTER), 1.0f);
    }

    /**
     * sends a title to the player
     * @param title text to display
     * @param subtitle text to display in the subtitle
     * @param stay time in ticks for how long the title should stay on screen
     */
    public static void sendTitle(String title, String subtitle, int stay){
        client.inGameHud.setTitle(Text.literal(title));
        if (!subtitle.isEmpty()){
            client.inGameHud.setSubtitle(Text.literal(subtitle));
        }
        client.inGameHud.setTitleTicks(10, stay, 10);
    }

    /**
     * executes a runnable after a given delay
     * @param ms
     */
    public static void runAfterDelay(Runnable action, int ms){
        int minDelay = ms - ms / 5;
        int maxDelay = ms + ms / 5;
        int rndDelay = randomInt(minDelay, maxDelay);
        new Thread(() -> {
            try {
                Thread.sleep(rndDelay);
                client.execute(action);
            } catch (InterruptedException ignored){}
        }).start();
    }

}
