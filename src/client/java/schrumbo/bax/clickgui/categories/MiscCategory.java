package schrumbo.bax.clickgui.categories;

import schrumbo.bax.clickgui.widgets.ToggleWidget;

import static schrumbo.bax.BaxClient.config;


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

        updateWidgetPositions(startX, startY);
    }
}
