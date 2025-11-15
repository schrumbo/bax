package schrumbo.bax.features.render;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

import static net.fabricmc.fabric.impl.tag.TagAliasLoader.ID;

public class HudRenderTesting implements HudElement {

    private static int POS_X = 200;
    private static int POS_Y = 200;

    private static final float WIDTH_RATIO  = 0.5f;
    private static final float HEIGHT_RATIO = 0.6f;

    private static int WIDTH = 200;
    private static int HEIGHT = 200;
    private static int FADE = 1;

    private static final int COLOR = 0xFF333235;


    /**
     * registers hud element
     */
    public static void register() {
        HudElementRegistry.attachElementAfter(
                VanillaHudElements.BOSS_BAR,
                ID,
                new HudRenderTesting()
        );
    }

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        handleWidget(context);
    }

    /**
     * handles the widget
     * @param context
     */
    private static void handleWidget(DrawContext context){
        calculateDimensions(context);
        calculatePosition(context);

        renderSmoothPanel(context);
    }

    /**
     * dynamically resizes the widget based on the screen size
     */
    private static void calculateDimensions(DrawContext context){
        WIDTH = (int) (context.getScaledWindowWidth() * WIDTH_RATIO);
        HEIGHT = (int) (context.getScaledWindowHeight() * HEIGHT_RATIO);
    }

    /**
     * calculates the Widget x and y so its centered.
     * needs to be called after {@link HudRenderTesting#calculateDimensions(DrawContext)}
     * @param context
     */
    private static void calculatePosition(DrawContext context){
        POS_X = context.getScaledWindowWidth() / 2 - WIDTH / 2;
        POS_Y = context.getScaledWindowHeight() / 2 - HEIGHT / 2;
    }

    /**
     * renders simple triangle on the hud
     * @param context
     */
    private static void renderSmoothPanel(DrawContext context) {
        context.fill(POS_X, POS_Y, POS_X + WIDTH, POS_Y + HEIGHT, COLOR);

    }




}
