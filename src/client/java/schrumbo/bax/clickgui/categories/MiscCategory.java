package schrumbo.bax.clickgui.categories;

import schrumbo.bax.clickgui.widgets.ColorPickerWidget;
import schrumbo.bax.clickgui.widgets.ToggleWidget;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.BaxClient.highlightConfig;

public class MiscCategory extends Category{
    public MiscCategory() {
        super("Miscellaneous");
    }

    @Override
    public void initializeWidgets(int startX, int startY, int width) {

        currentY = startY;

        ToggleWidget petNoti = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Pet Leveling Notification")
                .value(config::getPetNotification, config::setPetNotification)
                .build();
        widgets.add(petNoti);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        ToggleWidget petSwap = ToggleWidget
                .builder()
                .y(currentY).width(width).label("Pet Keybinds")
                .value(config::getPetSwap, config::setPetSwap)
                .build();
        widgets.add(petSwap);

        updateWidgetPositions(startX, startY);
    }
}
