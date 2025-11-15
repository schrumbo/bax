package schrumbo.bax.clickgui.categories;

import schrumbo.bax.clickgui.widgets.ColorPickerWidget;
import schrumbo.bax.clickgui.widgets.ToggleWidget;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.BaxClient.highlightConfig;

public class FishingCategory extends Category {
    public FishingCategory() {super("Fishing");}

    @Override
    public void initializeWidgets(int startX, int startY, int width) {
        currentY = startY;

        ToggleWidget petSwap = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Pet Swap")
                .value(config::getPetSwap, config::setPetSwap)
                .build();
        widgets.add(petSwap);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        updateWidgetPositions(startX, startY);
    }
}
