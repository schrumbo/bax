package schrumbo.bax.clickgui.categories;

import schrumbo.bax.clickgui.widgets.ColorPickerWidget;
import schrumbo.bax.clickgui.widgets.ToggleWidget;
import schrumbo.bax.clickgui.widgets.WidgetDropdownWidget;

import static schrumbo.bax.BaxClient.config;

public class DungeonCategory extends Category{
    public DungeonCategory() {
        super("Dungeon");
    }

    @Override
    public void initializeWidgets(int startX, int startY, int width) {
        widgets.clear();
        currentY = 0;

        WidgetDropdownWidget minibossDropdown = new WidgetDropdownWidget.Builder()
                .y(startY + currentY)
                .width(width)
                .label("Miniboss Highlight")
                .addChild(ToggleWidget.builder()
                        .width(width)
                        .label("Enable")
                        .value(config::getMinibossHighlight, config::setMinibossHighlight)
                        .build())
                .addChild(ColorPickerWidget.builder()
                        .width(width)
                        .label("Shadow Assassin")
                        .color(() -> config.shadowAssassinColor, config::setShadowAssassinColor)
                        .build())
                .addChild(ColorPickerWidget.builder()
                        .width(width)
                        .label("Lost Adventurer")
                        .color(() -> config.lostAdventurerColor, config::setLostAdventurerColor)
                        .build())
                .addChild(ColorPickerWidget.builder()
                        .width(width)
                        .label("Angry Archeologist")
                        .color(() -> config.angryArcheologistColor, config::setAngryArcheologistColor)
                        .build())
                .addChild(ColorPickerWidget.builder()
                        .width(width)
                        .label("Frozen Adventurer")
                        .color(() -> config.frozenAdventurerColor, config::setFrozenAdventurerColor)
                        .build())
                .build();
        minibossDropdown.setParentCategory(this);
        widgets.add(minibossDropdown);
        currentY += minibossDropdown.getHeight() + WIDGET_SPACING;

        WidgetDropdownWidget starredDropdown = new WidgetDropdownWidget.Builder()
                .y(startY + currentY)
                .width(width)
                .label("Starred Mob Highlight")
                .addChild(ToggleWidget.builder()
                        .width(width)
                        .label("Enable")
                        .value(config::getStarredHighlight, config::setStarredHighlight)
                        .build())
                .addChild(ColorPickerWidget.builder()
                        .width(width)
                        .label("Color")
                        .color(() -> config.starredColor, config::setStarredColor)
                        .build())
                .build();
        starredDropdown.setParentCategory(this);
        widgets.add(starredDropdown);
        currentY += starredDropdown.getHeight() + WIDGET_SPACING;

        WidgetDropdownWidget witherDropdown = new WidgetDropdownWidget.Builder()
                .y(startY + currentY)
                .width(width)
                .label("Wither Highlight")
                .addChild(ToggleWidget.builder()
                        .width(width)
                        .label("Enable")
                        .value(config::getWitherHighlight, config::setWitherHighlight)
                        .build())
                .addChild(ColorPickerWidget.builder()
                        .width(width)
                        .label("Color")
                        .color(() -> config.witherColor, config::setWitherColor)
                        .build())
                .build();
        witherDropdown.setParentCategory(this);
        widgets.add(witherDropdown);
        currentY += minibossDropdown.getHeight() + WIDGET_SPACING;

        calculateWidgetsHeight();
        updateWidgetPositions(startX,startY);
    }
}
