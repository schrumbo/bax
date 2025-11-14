package schrumbo.bax.clickgui.categories;

import schrumbo.bax.clickgui.widgets.ColorPickerWidget;
import schrumbo.bax.clickgui.widgets.ToggleWidget;

import static schrumbo.bax.BaxClient.highlightConfig;

public class HighlightCategory extends Category{
    public HighlightCategory() {
        super("Custom Highlight");
    }

    @Override
    public void initializeWidgets(int startX, int startY, int width) {

        currentY = startY;

        ToggleWidget mobHighlight = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Custom Highlight")
                .value(highlightConfig::getEnabled, highlightConfig::setEnabled)
                .build();
        widgets.add(mobHighlight);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        ColorPickerWidget highlightColor = ColorPickerWidget.builder()
                .width(width)
                .y(currentY)
                .label("Custom Highlight Color")
                .color(() -> highlightConfig.getHighlightColor(), highlightConfig::setHighlightColor)
                .opacity(() -> highlightConfig.getHighlightOpacity(), highlightConfig::setHighlightOpacity)
                .build();
        widgets.add(highlightColor);


        updateWidgetPositions(startX, startY);
    }
}
