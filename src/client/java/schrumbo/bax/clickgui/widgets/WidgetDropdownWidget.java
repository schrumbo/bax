package schrumbo.bax.clickgui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import schrumbo.bax.clickgui.categories.Category;
import schrumbo.bax.utils.render.RenderUtils;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.bax.BaxClient.config;

public class WidgetDropdownWidget extends Widget {
    private boolean expanded;
    private List<Widget> children;
    private int collapsedHeight;
    private int expandedHeight;
    private Category parentCategory;

    private static final int ARROW_SIZE = 8;

    private MinecraftClient client = MinecraftClient.getInstance();

    protected WidgetDropdownWidget(Builder builder) {
        super(builder);
        this.expanded = builder.expanded;
        this.children = new ArrayList<>(builder.children);
        this.collapsedHeight = builder.height;
        calculateExpandedHeight();
    }

    private void calculateExpandedHeight() {
        expandedHeight = collapsedHeight;
        for (Widget child : children) {
            expandedHeight += child.getHeight() + WIDGET_SPACING;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        hovered = isHovered(mouseX, mouseY);

        RenderUtils.fillRoundedRect(context, x, y, width, collapsedHeight, 0, config.guicolors.widgetBackground);

        int textColor = hovered ? config.colorWithAlpha(config.guicolors.accent, config.guicolors.hoveredTextOpacity) : config.guicolors.text;

        int labelX = x + PADDING;
        int labelY = y - client.textRenderer.fontHeight + height / 2;

        var matrices = context.getMatrices();
        matrices.pushMatrix();
        matrices.translate(labelX, labelY);
        matrices.scale(config.guicolors.textSize, config.guicolors.textSize);

        context.drawText(client.textRenderer, Text.literal(label), 0, 0, textColor, false);

        matrices.popMatrix();
        drawArrow(context, expanded);

        if (expanded) {
            int childY = y + collapsedHeight + WIDGET_SPACING;
            for (Widget child : children) {
                child.setPosition(x + 10, childY);
                child.render(context, mouseX, mouseY, delta);
                childY += child.getHeight() + WIDGET_SPACING;
            }
        }
    }

    private void drawArrow(DrawContext context, boolean expanded) {
        int arrowX = x + width - PADDING - ARROW_SIZE;
        int arrowY = y + (collapsedHeight - ARROW_SIZE) / 2;

        int color = hovered ? config.colorWithAlpha(config.guicolors.accent, config.guicolors.hoveredTextOpacity) : config.guicolors.text;

        if (expanded) {
            context.fill(arrowX, arrowY + ARROW_SIZE, arrowX + ARROW_SIZE, arrowY + ARROW_SIZE - 1, color);
            context.fill(arrowX + 1, arrowY + ARROW_SIZE - 2, arrowX + ARROW_SIZE - 1, arrowY + ARROW_SIZE - 3, color);
            context.fill(arrowX + 2, arrowY + ARROW_SIZE - 4, arrowX + ARROW_SIZE - 2, arrowY + ARROW_SIZE - 5, color);
            context.fill(arrowX + 3, arrowY + ARROW_SIZE - 6, arrowX + ARROW_SIZE - 3, arrowY + ARROW_SIZE - 7, color);
        } else {
            context.fill(arrowX, arrowY, arrowX + ARROW_SIZE, arrowY + 1, color);
            context.fill(arrowX + 1, arrowY + 2, arrowX + ARROW_SIZE - 1, arrowY + 3, color);
            context.fill(arrowX + 2, arrowY + 4, arrowX + ARROW_SIZE - 2, arrowY + 5, color);
            context.fill(arrowX + 3, arrowY + 6, arrowX + ARROW_SIZE - 3, arrowY + 7, color);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isHeaderHovered(mouseX, mouseY)) {
            expanded = !expanded;
            if (parentCategory != null) {
                parentCategory.recalculateWidgetPositions();
            }
            return true;
        }

        if (expanded) {
            for (Widget child : children) {
                if (child.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (expanded) {
            for (Widget child : children) {
                if (child.mouseReleased(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (expanded) {
            for (Widget child : children) {
                if (child.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (expanded) {
            for (Widget child : children) {
                if (child.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isHeaderHovered(double mouseX, double mouseY) {
        return mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + collapsedHeight;
    }

    @Override
    public int getHeight() {
        return expanded ? expandedHeight : collapsedHeight;
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        if (expanded) {
            updateChildPositions();
        }
    }

    @Override
    public void setOriginalY(int y) {
        super.setOriginalY(y);
        if (expanded) {
            updateChildPositions();
        }
    }

    private void updateChildPositions() {
        int childY = y + collapsedHeight + WIDGET_SPACING;
        for (Widget child : children) {
            child.setY(childY);
            child.setOriginalY(childY);
            childY += child.getHeight() + WIDGET_SPACING;
        }
    }

    public void setParentCategory(Category category) {
        this.parentCategory = category;
    }

    public void addChild(Widget widget) {
        children.add(widget);
        calculateExpandedHeight();
    }

    public static class Builder extends Widget.Builder<Builder> {
        private boolean expanded = false;
        private List<Widget> children = new ArrayList<>();

        public Builder expanded(boolean expanded) {
            this.expanded = expanded;
            return this;
        }

        public Builder addChild(Widget widget) {
            this.children.add(widget);
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public WidgetDropdownWidget build() {
            return new WidgetDropdownWidget(this);
        }
    }
}
