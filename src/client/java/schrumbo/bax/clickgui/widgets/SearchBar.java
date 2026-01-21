package schrumbo.bax.clickgui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;

import static schrumbo.bax.BaxClient.config;

public class SearchBar {
    private final MinecraftClient client = MinecraftClient.getInstance();

    private int x;
    private int y;
    private int width;
    private int height;

    private String text = "";
    private boolean focused = false;
    private int cursorPosition = 0;
    private long lastCursorBlink = 0;
    private boolean cursorVisible = true;

    private static final int PADDING = 5;
    private static final int CURSOR_BLINK_SPEED = 530;

    public SearchBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        int bgColor = config.colorWithAlpha(config.guicolors.panelBackground, 1.0f);

        //RenderUtils.fillRoundedRect(context, x, y, width, height, 0f, bgColor);

        //RenderUtils.drawOutlineWithCutCorners(context, x, y, width, height,2, config.colorWithAlpha(config.guicolors.accent, config.guicolors.panelBorderOpacity));


        String displayText = text.isEmpty() && !focused ? "Search..." : text;
        int textColor = text.isEmpty() && !focused ?
                config.colorWithAlpha(config.guicolors.text, 0.5f) :
                config.guicolors.text;

        int textX = x + PADDING;
        int textY = y + (height - client.textRenderer.fontHeight) / 2;

        context.enableScissor(x + PADDING, y, x + width - PADDING, y + height);
        context.drawText(client.textRenderer, displayText, textX, textY, textColor, false);

        if (focused && cursorVisible) {
            int cursorX = textX + client.textRenderer.getWidth(text.substring(0, Math.min(cursorPosition, text.length())));
            context.fill(cursorX, textY, cursorX + 1, textY + client.textRenderer.fontHeight, config.guicolors.text);
        }

        context.disableScissor();

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCursorBlink > CURSOR_BLINK_SPEED) {
            cursorVisible = !cursorVisible;
            lastCursorBlink = currentTime;
        }
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return false;

        boolean clicked = mouseX >= x && mouseX <= x + width &&
                mouseY >= y && mouseY <= y + height;

        if (clicked) {
            if (!focused) {
                focused = true;
                cursorPosition = text.length();
            } else {
                int relativeX = (int) (mouseX - x - PADDING);
                cursorPosition = getClickedPosition(relativeX);
            }
            resetCursor();
            return true;
        } else {
            focused = false;
            return false;
        }
    }

    private int getClickedPosition(int clickX) {
        if (clickX <= 0) return 0;

        for (int i = 0; i <= text.length(); i++) {
            int width = client.textRenderer.getWidth(text.substring(0, i));
            if (clickX < width) {
                return Math.max(0, i - 1);
            }
        }
        return text.length();
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!focused) return false;

        if (keyCode == 259) {
            if (cursorPosition > 0 && !text.isEmpty()) {
                text = text.substring(0, cursorPosition - 1) + text.substring(cursorPosition);
                cursorPosition--;
                resetCursor();
            }
            return true;
        } else if (keyCode == 261) {
            if (cursorPosition < text.length()) {
                text = text.substring(0, cursorPosition) + text.substring(cursorPosition + 1);
                resetCursor();
            }
            return true;
        } else if (keyCode == 263) {
            if (cursorPosition > 0) {
                cursorPosition--;
                resetCursor();
            }
            return true;
        } else if (keyCode == 262) {
            if (cursorPosition < text.length()) {
                cursorPosition++;
                resetCursor();
            }
            return true;
        } else if (keyCode == 268) {
            cursorPosition = 0;
            resetCursor();
            return true;
        } else if (keyCode == 269) {
            cursorPosition = text.length();
            resetCursor();
            return true;
        }

        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if (!focused) return false;

        if (Screen.isPaste(modifiers)) {
            String clipboard = MinecraftClient.getInstance().keyboard.getClipboard();
            insertText(clipboard);
            return true;
        }

        if (Character.isISOControl(chr)) return false;

        insertText(String.valueOf(chr));
        return true;
    }

    private void insertText(String toInsert) {
        if (toInsert == null || toInsert.isEmpty()) return;

        text = text.substring(0, cursorPosition) + toInsert + text.substring(cursorPosition);
        cursorPosition += toInsert.length();
        resetCursor();
    }

    private void resetCursor() {
        cursorVisible = true;
        lastCursorBlink = System.currentTimeMillis();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        cursorPosition = Math.min(cursorPosition, text.length());
    }

    public void clear() {
        text = "";
        cursorPosition = 0;
        resetCursor();
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
        if (focused) resetCursor();
    }
}
