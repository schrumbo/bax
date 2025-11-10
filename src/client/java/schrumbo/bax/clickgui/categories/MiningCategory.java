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

        ToggleWidget drillSwap = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Drill Swap")
                .value(config::getDrillSwap, config::setDrillSwap)
                .build();
        widgets.add(drillSwap);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        ToggleWidget lazyShaftEnter = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Lazy Shaft Enter")
                .value(config::getEasyShaftEnter, config::setEasyShaftEnter)
                .build();
        widgets.add(lazyShaftEnter);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        ToggleWidget maniacESP = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Maniac ESP")
                .value(config::getManiacESP, config::setManiacESP)
                .build();
        widgets.add(maniacESP);
        updateWidgetPositions(startX, startY);
    }

}