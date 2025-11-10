package schrumbo.bax.clickgui.categories;


import schrumbo.bax.clickgui.widgets.ColorPickerWidget;
import schrumbo.bax.clickgui.widgets.ToggleWidget;

import static schrumbo.bax.BaxClient.config;

public class MiningCategory extends Category {

    public MiningCategory() {
        super("Mining");
    }

    @Override
    public void initializeWidgets(int startX, int startY, int width) {

        int currentY = startY;

        ToggleWidget lazyComms = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Lazy Commission claiming")
                .value(config::getEasyCommissions, config::setEasyCommissions)
                .build();
        widgets.add(lazyComms);
        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;
        updateWidgetPositions(startX, startY);
    }

}