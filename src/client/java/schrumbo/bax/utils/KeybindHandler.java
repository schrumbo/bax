package schrumbo.bax.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import schrumbo.bax.clickgui.ClickGuiScreen;
import schrumbo.bax.utils.entity.EntityUtils;

import java.util.Objects;

import static schrumbo.bax.BaxClient.config;


/**
 * handles keybindings
 */
public class KeybindHandler {
    private static KeyBinding entityDebugKey;
    private static KeyBinding configKey;
    private static final String CATEGORY = "Bax";


    private static final String PET_CATEGORY = "Bax - Pet Keys";
    private static KeyBinding pet1;
    private static KeyBinding pet2;
    private static KeyBinding pet3;
    private static KeyBinding pet4;
    private static KeyBinding pet5;
    private static KeyBinding pet6;
    private static KeyBinding pet7;
    private static KeyBinding pet8;
    private static KeyBinding pet9;

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

        entityDebugKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Debug Entities",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_DOWN,
                CATEGORY
        ));
        //PET KEYBINDINGS
        pet1 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 1",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_1,
                PET_CATEGORY
        ));
        pet2 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 2",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_2,
                PET_CATEGORY
        ));
        pet3 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 3",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_3,
                PET_CATEGORY
        ));
        pet4 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 4",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_4,
                PET_CATEGORY
        ));
        pet5 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 5",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_5,
                PET_CATEGORY
        ));
        pet6 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 6",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_6,
                PET_CATEGORY
        ));
        pet7 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 7",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_7,
                PET_CATEGORY
        ));
        pet8 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 8",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_8,
                PET_CATEGORY
        ));
        pet9 = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "Pet 9",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_9,
                PET_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if(configKey.wasPressed()){
                client.setScreen(new ClickGuiScreen());
            }
            if (entityDebugKey.wasPressed()){
                EntityUtils.debugWitherEntities();
            }

        });


    }

}