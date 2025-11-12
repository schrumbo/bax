package schrumbo.bax.clickgui.categories;


import schrumbo.bax.clickgui.widgets.ColorPickerWidget;

import static schrumbo.bax.BaxClient.config;

public class GeneralCategory extends Category {

    public GeneralCategory() {
        super("General");
    }

    @Override
    public void initializeWidgets(int startX, int startY, int width) {

        currentY = startY;

        ColorPickerWidget accentColorPicker = ColorPickerWidget.builder()
                .width(width)
                .y(currentY)
                .label("Accent Color")
                .color(() -> config.guicolors.accent, config::setAccentColor)
                .build();
        widgets.add(accentColorPicker);
        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;
        updateWidgetPositions(startX, startY);
    }

}