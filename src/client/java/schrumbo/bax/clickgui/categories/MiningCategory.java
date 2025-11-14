package schrumbo.bax.clickgui.categories;


import schrumbo.bax.clickgui.widgets.ColorPickerWidget;
import schrumbo.bax.clickgui.widgets.ToggleWidget;
import schrumbo.bax.clickgui.widgets.WidgetDropdownWidget;

import static schrumbo.bax.BaxClient.config;

public class MiningCategory extends Category {

    public MiningCategory() {
        super("Mining");
    }

    @Override
    public void initializeWidgets(int startX, int startY, int width) {

        currentY = startY;

        ToggleWidget lazyComms = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Lazy Commission claiming")
                .value(config::getEasyCommissions, config::setEasyCommissions)
                .build();
        widgets.add(lazyComms);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        ToggleWidget lazyShaftEnter = ToggleWidget.builder()
                .y(currentY)
                .width(width)
                .label("Lazy Shaft Enter")
                .value(config::getEasyShaftEnter, config::setEasyShaftEnter)
                .build();
        widgets.add(lazyShaftEnter);

        currentY += widgets.get(widgets.size() - 1).getHeight() + WIDGET_SPACING;

        //DRILL SWAP
        ToggleWidget pigeonDrillSwap = ToggleWidget.builder()
                .width(width - 10)
                .label("Pigeon Drill Swap")
                .value(config::getPigeonDrillSwap, config::setPigeonDrillSwap)
                .build();

        ToggleWidget rodDrillSwap = ToggleWidget.builder()
                .width(width - 10)
                .label("Rod Drill Swap")
                .value(config::getRodDrillSwap, config::setRodDrillSwap)
                .build();



        WidgetDropdownWidget drillSwapDropdown = new WidgetDropdownWidget.Builder()
                .y(startY + currentY)
                .width(width)
                .label("Drill Swap")
                .addChild(pigeonDrillSwap)
                .addChild(rodDrillSwap)
                .build();

        drillSwapDropdown.setParentCategory(this);
        widgets.add(drillSwapDropdown);
        currentY += drillSwapDropdown.getHeight() + WIDGET_SPACING;

        //MANIAC HIGHLIGHT
        ToggleWidget maniacHighlight = ToggleWidget.builder()
                .width(width - 10)
                .label("Maniac ESP")
                .value(config::getManiacHighlight, config::setManiacHighlight)
                .build();

        ColorPickerWidget maniacColor = ColorPickerWidget.builder()
                .width(width - 10)
                .label("Maniac Highlight Color")
                .color(() -> config.maniacColor, config::setManiacColor)
                .build();



        WidgetDropdownWidget maniacHighlightDropdown = new WidgetDropdownWidget.Builder()
                .y(startY + currentY)
                .width(width)
                .label("Maniac Highlight")
                .addChild(maniacHighlight)
                .addChild(maniacColor)
                .build();

        maniacHighlightDropdown.setParentCategory(this);
        widgets.add(maniacHighlightDropdown);
        currentY += maniacHighlightDropdown.getHeight() + WIDGET_SPACING;



        updateWidgetPositions(startX, startY);
    }

}