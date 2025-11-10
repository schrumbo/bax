package schrumbo.bax.clickgui.categories;


import schrumbo.bax.clickgui.widgets.ColorPickerWidget;
import schrumbo.bax.clickgui.widgets.SliderWidget;
import schrumbo.bax.clickgui.widgets.ToggleWidget;

import static schrumbo.bax.BaxClient.config;

public class DungeonsCategory extends Category {

    public DungeonsCategory() {
        super("Dungeons");
    }

    @Override
    public void initializeWidgets(int startX, int startY, int width) {

        int currentY = startY;

        ToggleWidget starredESP = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Starred Mob ESP")
                .value(config::getStarredESP, config::setStarredESP)
                .build();
        widgets.add(starredESP);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;




        updateWidgetPositions(startX, startY);
    }

}