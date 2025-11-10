package schrumbo.bax.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import schrumbo.bax.clickgui.ClickGuiScreen;


/**
 * handles keybindings
 */
public class KeybindHandler {
    private static KeyBinding toggleHudKey;
    private static KeyBinding configKey;
    private static final String CATEGORY = "Bax";
    /**
     * registers all keybinds
     */
    public static void register(){
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Open Config",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if(configKey.wasPressed()){
                client.setScreen(new ClickGuiScreen());
            }
        });


    }

}