package schrumbo.bax.clickgui.categories;


import schrumbo.bax.clickgui.widgets.ColorPickerWidget;
import schrumbo.bax.clickgui.widgets.ToggleWidget;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.BaxClient.highlightConfig;

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
                .label("GUI Accent Color")
                .color(() -> config.guicolors.accent, config::setAccentColor)
                .build();
        widgets.add(accentColorPicker);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        ToggleWidget depthCheck = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Highlighting Depth Check")
                .value(highlightConfig::getDepthCheck, highlightConfig::setDepthCheck)
                .build();
        widgets.add(depthCheck);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        updateWidgetPositions(startX, startY);
    }

}