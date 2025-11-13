package schrumbo.bax.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import schrumbo.bax.clickgui.ClickGuiScreen;
import schrumbo.bax.utils.entity.EntityUtils;


/**
 * handles keybindings
 */
public class KeybindHandler {
    private static KeyBinding debugKey;
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

        debugKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Debug Entities",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if(configKey.wasPressed()){
                client.setScreen(new ClickGuiScreen());
            }
            if (debugKey.wasPressed()){
                EntityUtils.debugEntities();
            }
        });


    }

}