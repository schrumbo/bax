package schrumbo.bax.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static schrumbo.bax.BaxClient.config;

public class ChatUtils {
    private static final MinecraftClient client = MinecraftClient.getInstance();

    private static final Text PREFIX = Text.literal("§8[").append(Text.literal("Bax").styled(style -> style.withColor(config.guicolors.accent))).append(Text.literal("§8] "));

    public static void modMessage(String message){
        if(client.player == null)return;
        client.player.sendMessage( PREFIX.copy().append(Text.literal("§f" + message)), false);
    }

}
