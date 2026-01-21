package schrumbo.bax.clickgui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import schrumbo.bax.clickgui.categories.*;
import schrumbo.bax.clickgui.widgets.SearchBar;
import schrumbo.bax.clickgui.widgets.Widget;
import schrumbo.bax.config.ConfigManager;
import schrumbo.bax.utils.render.RenderUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static schrumbo.bax.BaxClient.config;


/**
 * Main ClickGUI screen for configuration.
 * Handles rendering input and category management
 */
public class ClickGuiScreen extends Screen {
    private final MinecraftClient client = MinecraftClient.getInstance();
    private final TextRenderer textRenderer = client.textRenderer;
    private static int panelX = 0;
    private static int panelY = 0;
    private static int PANEL_WIDTH = 1000;
    private static int PANEL_HEIGHT = 700;
    private static final int TITLE_BAR_HEIGHT = 25;
    private static final int PADDING = 10;
    int categoriesWidth = PANEL_WIDTH / 5;

    private SearchBar searchBar;
    private List<Widget> filteredWidgets = new ArrayList<>();
    private String lastSearchQuery = "";

    private boolean draggingPanel = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;

    public static final List<Category> categories = new ArrayList<>();
    private Category selectedCategory;

    private int scrollOffset = 0;
    private int widgetScrollOffset = 0;

    private int categoriesHeight = 0;
    public static int widgetX;
    public static int widgetWidth;

    /**
     * Initializes the ClickGUI with all configuration categories.
     */
    public ClickGuiScreen() {
        super(Text.literal("Bax"));

        categories.clear();
        categories.add(new GeneralCategory());
        categories.add(new MiscCategory());
        if (config.selectedCategory != null){
            for (Category c : categories){
                if (Objects.equals(c.getName(), config.selectedCategory)){
                    selectedCategory = c;
                }
            }

        }else{
            selectedCategory = categories.getFirst();
        }

    }

    @Override
    protected void init() {
        super.init();

        calcScale();
        centerPosX();
        centerPosY();
        if(PANEL_WIDTH >= client.getWindow().getWidth() || PANEL_HEIGHT >= client.getWindow().getHeight()){
            PANEL_WIDTH = client.getWindow().getWidth();
            PANEL_HEIGHT = client.getWindow().getHeight();
            panelX = 0;
            panelY = 0;
        }
        initializeCategories();

        int searchBarWidth = PANEL_WIDTH / 5 + 2 * PADDING;
        int searchBarHeight = TITLE_BAR_HEIGHT - 2;
        int searchBarX = panelX + 2;
        int searchBarY = panelY + 1;
        searchBar = new SearchBar(searchBarX, searchBarY, searchBarWidth, searchBarHeight);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {}

    @Override
    public void resize(MinecraftClient client, int width, int height) {}

    /**
     * Calculates and sets positions for all categories.
     */
    private void initializeCategories() {
        int currentY = 0;

        calcScale();

        widgetX = panelX + categoriesWidth + (3 * PADDING);
        widgetWidth = PANEL_WIDTH - categoriesWidth - (4 * PADDING);

        if (searchBar != null) {
            int searchBarX = panelX;
            int searchBarY = panelY;
            searchBar.setPosition(searchBarX, searchBarY);
        }

        List<Category> categoriesToPosition = getFilteredCategories();
        for (Category category : categoriesToPosition) {
            category.setPosition(panelX + 10, panelY + TITLE_BAR_HEIGHT + 10 + currentY, categoriesWidth);
            currentY += category.getHeaderHeight() + 5;
        }

        categoriesHeight = currentY;

        if (selectedCategory != null) {
            int widgetStartY = panelY + TITLE_BAR_HEIGHT + 10;
            selectedCategory.initializeWidgetsIfNeeded(widgetX, widgetStartY, widgetWidth);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {

        super.render(context, mouseX, mouseY, delta);

        float scale = config.configScale;
        calcScale();
        if(PANEL_WIDTH >= client.getWindow().getWidth() || PANEL_HEIGHT >= client.getWindow().getHeight()){
            PANEL_WIDTH = client.getWindow().getWidth();
            PANEL_HEIGHT = client.getWindow().getHeight();
            panelX = 0;
            panelY = 0;
        }else{
            PANEL_WIDTH = 1000;
            PANEL_HEIGHT = 700;
        }

        float scaledMouseX = (float) mouseX / scale;
        float scaledMouseY = (float) mouseY / scale;

        var matrices = context.getMatrices();

        matrices.pushMatrix();
        matrices.scale(scale, scale);
        renderPanel(context, mouseX, mouseY);
        renderCategoriesScrollbar(context);
        renderWidgetScrollbar(context);
        matrices.popMatrix();

        int contentX = panelX + 10;
        int contentY = panelY + TITLE_BAR_HEIGHT + 10;
        int contentWidth = PANEL_WIDTH - 20;
        int contentAreaHeight = PANEL_HEIGHT - TITLE_BAR_HEIGHT - 20;

        int scissorX = (int) (contentX * scale);
        int scissorY = (int) (contentY * scale);
        int scissorX2 = (int) ((contentX + contentWidth) * scale);
        int scissorY2 = (int) ((contentY + contentAreaHeight) * scale);

        context.enableScissor(scissorX, scissorY, scissorX2, scissorY2);

        matrices.pushMatrix();
        matrices.scale(scale, scale);



        List<Category> categoriesToRender = getFilteredCategories();
        int yOffset = contentY - scrollOffset;
        for (Category category : categoriesToRender) {
            category.setPosition(panelX + 10, yOffset, categoriesWidth);
            category.renderHeader(context, (int) scaledMouseX, (int) scaledMouseY, category == selectedCategory);
            yOffset += category.getHeaderHeight() + 5;
        }
        context.disableScissor();
        context.fill(panelX + categoriesWidth + (2 * PADDING), panelY + TITLE_BAR_HEIGHT, panelX + categoriesWidth + (2 * PADDING) + 2, panelY + PANEL_HEIGHT, config.colorWithAlpha(config.guicolors.accent, 0.8f));
        context.fill(panelX, panelY + TITLE_BAR_HEIGHT - 2, panelX + PANEL_WIDTH, panelY + TITLE_BAR_HEIGHT, config.colorWithAlpha(config.guicolors.accent, 0.8f));
        matrices.popMatrix();


        renderWidgets(context, scale, contentY, contentAreaHeight, scaledMouseX, scaledMouseY, delta);
    }

    /**
     * renders the widgets of the currently selected category
     * @param context
     * @param scale
     * @param contentY
     * @param contentAreaHeight
     * @param scaledMouseX
     * @param scaledMouseY
     * @param delta
     */
    private void renderWidgets(DrawContext context, float scale, float contentY, int contentAreaHeight, float scaledMouseX, float scaledMouseY, float delta){
        var matrices = context.getMatrices();

        String currentQuery = searchBar.getText().toLowerCase().trim();
        if (!currentQuery.equals(lastSearchQuery)) {
            lastSearchQuery = currentQuery;
            updateFilteredWidgets(currentQuery);
        }

        if (selectedCategory == null) return;

        selectedCategory.setWidgetScrollOffset(widgetScrollOffset);

        int widgetScissorX = (int) (widgetX * scale);
        int widgetScissorY = (int) (contentY * scale);
        int widgetScissorX2 = (int) ((widgetX + widgetWidth) * scale);
        int widgetScissorY2 = (int) ((contentY + contentAreaHeight) * scale);

        context.enableScissor(widgetScissorX, widgetScissorY, widgetScissorX2, widgetScissorY2);

        matrices.pushMatrix();
        matrices.scale(scale, scale);

        if (currentQuery.isEmpty()) {
            selectedCategory.renderWidgets(context, (int) scaledMouseX, (int) scaledMouseY, delta);
        } else {
            renderFilteredWidgets(context, (int) scaledMouseX, (int) scaledMouseY, delta);
        }

        matrices.popMatrix();

        context.disableScissor();
    }

    /**
     * updates search results
     * @param query
     */
    private void updateFilteredWidgets(String query) {
        filteredWidgets.clear();

        if (query.isEmpty()) {
            selectedCategory = categories.isEmpty() ? null : categories.getFirst();
            return;
        }

        Category bestMatchCategory = null;
        List<Widget> bestMatchWidgets = new ArrayList<>();

        for (Category category : categories) {
            if (category.widgets.isEmpty()) {
                category.initializeWidgetsIfNeeded(widgetX, panelY + TITLE_BAR_HEIGHT + 10, widgetWidth);
            }

            if (category.getName().toLowerCase().contains(query)) {
                if (bestMatchCategory == null) {
                    bestMatchCategory = category;
                }
            }

            List<Widget> matchingWidgets = new ArrayList<>();
            for (Widget widget : category.widgets) {
                if (widget.label.toLowerCase().contains(query)) {
                    matchingWidgets.add(widget);
                    filteredWidgets.add(widget);
                }
            }

            if (!matchingWidgets.isEmpty() && bestMatchCategory == null) {
                bestMatchCategory = category;
                bestMatchWidgets = matchingWidgets;
            } else if (bestMatchCategory != null && bestMatchCategory == category) {
                bestMatchWidgets = matchingWidgets;
            }
        }

        if (bestMatchCategory != null) {
            selectedCategory = bestMatchCategory;
        } else {
            selectedCategory = null;
        }
    }

    /**
     * Gets all categories which have widgets that match the user's search
     * - sets the selected category to the next best match if the current selected category is emtpy
     * @return List of all categories which should be shown
     */
    private List<Category> getFilteredCategories() {
        if (lastSearchQuery.isEmpty()) {
            return categories;
        }

        List<Category> filtered = new ArrayList<>();
        for (Category category : categories) {

            boolean hasMatchingWidget = false;
            for (Widget widget : category.widgets) {
                if (widget.label.toLowerCase().contains(lastSearchQuery)) {
                    hasMatchingWidget = true;
                    if(getFilteredWidgetsForCategory(selectedCategory).isEmpty()){
                        selectedCategory = category;
                    }
                    break;
                }
            }
            if (hasMatchingWidget) {
                filtered.add(category);
            }
        }
        return filtered;
    }

    /**
     * gets all widgets that should be visible for a category
     * @param category
     * @return List of Widgets
     */
    private List<Widget> getFilteredWidgetsForCategory(Category category) {
        if (lastSearchQuery.isEmpty()) {
            return category.widgets;
        }

        List<Widget> filtered = new ArrayList<>();
        for (Widget widget : category.widgets) {
            if (widget.label.toLowerCase().contains(lastSearchQuery)) {
                filtered.add(widget);
            }
        }
        return filtered;
    }

    /**
     * Renders only widgets that match the users search
     * @param context
     * @param mouseX
     * @param mouseY
     * @param delta
     */
    private void renderFilteredWidgets(DrawContext context, int mouseX, int mouseY, float delta) {
        if (selectedCategory == null) return;

        List<Widget> widgetsToRender = getFilteredWidgetsForCategory(selectedCategory);
        int startY = panelY + TITLE_BAR_HEIGHT + 10 - widgetScrollOffset;
        int currentY = startY;

        for (Widget widget : widgetsToRender) {
            widget.setY(currentY);
            widget.render(context, mouseX, mouseY, delta);
            currentY += widget.getHeight() + 5;
        }
    }


    /**
     * calculates the gui scale based on the GuiScale setting
     */
    public void calcScale(){
        int guiScale = client.options.getGuiScale().getValue();

        if(guiScale == 0) {
            guiScale = client.getWindow().getScaleFactor();
        }
        config.configScale = 1.0f / guiScale;

    }

    /**
     * centers x pos
     */
    public void centerPosX(){
        int windowWidth = (client.getWindow().getFramebufferWidth());
        panelX = (windowWidth / 2 - PANEL_WIDTH / 2);
    }

    /**
     * centers y pos
     */
    public void centerPosY(){
        int windowHeight = (client.getWindow().getFramebufferHeight());
        panelY = (windowHeight / 2 - PANEL_HEIGHT / 2);
    }

    /**
     * Renders the main panel background and title bar.
     */
    private void renderPanel(DrawContext context, int mouseX, int mouseY) {
        var matrices = context.getMatrices();

        RenderUtils.drawRectWithCutCorners(context, panelX + 1, panelY, PANEL_WIDTH - 2, PANEL_HEIGHT, 1, config.colorWithAlpha(config.guicolors.panelBackground, 1.0f));
        context.fill(panelX + 1, panelY + 1, panelX + 1 + PANEL_WIDTH / 5 + 2 * PADDING, panelY + TITLE_BAR_HEIGHT - 1, config.colorWithAlpha(0xFFFFFFFF, 0.05f));


        RenderUtils.drawOutlineWithCutCorners(context, panelX - 1, panelY - 1, PANEL_WIDTH + 2, PANEL_HEIGHT + 2,2, config.colorWithAlpha(config.guicolors.accent, config.guicolors.panelBorderOpacity));
        matrices.pushMatrix();
        //RenderUtils.drawRectWithCutCorners(context, panelX, panelY, PANEL_WIDTH, TITLE_BAR_HEIGHT, 1, config.colorWithAlpha(config.guicolors.accent, config.guicolors.panelTitleBarOpacity));
        matrices.popMatrix();
        String title = "";
        int titleX = panelX + (PANEL_WIDTH - client.textRenderer.getWidth(title)) / 2;
        int titleY = panelY + (TITLE_BAR_HEIGHT - 8) / 2;
        context.drawText(textRenderer, title, titleX, titleY, config.guicolors.text, true);

        searchBar.render(context, (int)(mouseX / config.configScale), (int)(mouseY / config.configScale));
    }

    /**
     * Renders the scrollbar based on content height.
     */
    private void renderCategoriesScrollbar(DrawContext context) {
        int scrollbarWidth = 4;
        int scrollbarX = panelX + categoriesWidth + 15;
        int scrollbarY = panelY + TITLE_BAR_HEIGHT + 10;
        int scrollbarHeight = PANEL_HEIGHT - TITLE_BAR_HEIGHT - 20;

        int trackColor = config.colorWithAlpha(0x000000, 0.3f);
        int maxScroll = Math.max(0, categoriesHeight - scrollbarHeight);

        context.enableScissor(scrollbarX, scrollbarY, scrollbarX + scrollbarWidth, scrollbarY + scrollbarHeight);
        context.fill(scrollbarX, scrollbarY, scrollbarX + scrollbarWidth, scrollbarY + scrollbarHeight, trackColor);
        if (maxScroll > 0) {
            float thumbHeightRatio = (float) (PANEL_HEIGHT - TITLE_BAR_HEIGHT - 20) / categoriesHeight;
            int thumbHeight = Math.max(20, (int) (scrollbarHeight * thumbHeightRatio));
            int thumbY = scrollbarY + (int) ((float) scrollOffset / maxScroll * (scrollbarHeight - thumbHeight));

            RenderUtils.fillRoundedRect(context, scrollbarX, thumbY, scrollbarWidth, thumbHeight, 2.0f, config.colorWithAlpha(config.guicolors.accent, config.guicolors.widgetAccentOpacity));
        }
        context.disableScissor();
    }

    /**
     * renders scrollbar for the widgets
     * @param context
     */
    private void renderWidgetScrollbar(DrawContext context) {
        if (selectedCategory == null) return;

        int scrollbarWidth = 4;
        int scrollbarX = panelX + PANEL_WIDTH - PADDING + 2;
        int scrollbarY = panelY + TITLE_BAR_HEIGHT + 10;
        int scrollbarHeight = PANEL_HEIGHT - TITLE_BAR_HEIGHT - 20;

        int trackColor = config.colorWithAlpha(0x000000, 0.3f);
        int widgetsContentHeight = selectedCategory.widgetsHeight;
        int maxScroll = Math.max(0, widgetsContentHeight - scrollbarHeight);

        context.enableScissor(scrollbarX, scrollbarY, scrollbarX + scrollbarWidth, scrollbarY + scrollbarHeight);
        context.fill(scrollbarX, scrollbarY, scrollbarX + scrollbarWidth, scrollbarY + scrollbarHeight, trackColor);

        if (maxScroll > 0) {
            float thumbHeightRatio = (float) scrollbarHeight / widgetsContentHeight;
            int thumbHeight = Math.max(20, (int) (scrollbarHeight * thumbHeightRatio));
            int thumbY = scrollbarY + (int) ((float) widgetScrollOffset / maxScroll * (scrollbarHeight - thumbHeight));

            RenderUtils.fillRoundedRect(context, scrollbarX, thumbY, scrollbarWidth, thumbHeight, 2.0f, config.colorWithAlpha(config.guicolors.accent, config.guicolors.widgetAccentOpacity));
        }
        context.disableScissor();
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);

        float scale = config.configScale;
        double scaledMouseX = mouseX / scale;
        double scaledMouseY = mouseY / scale;

        if (searchBar.mouseClicked(scaledMouseX, scaledMouseY, button)) {
            return true;
        }

        if (scaledMouseX >= panelX && scaledMouseX <= panelX + PANEL_WIDTH &&
                scaledMouseY >= panelY && scaledMouseY <= panelY + TITLE_BAR_HEIGHT) {
            draggingPanel = true;
            dragOffsetX = (int) (scaledMouseX - panelX);
            dragOffsetY = (int) (scaledMouseY - panelY);
            return true;
        }

        List<Category> categoriesToCheck = getFilteredCategories();
        for (Category category : categoriesToCheck) {
            if (category.isHeaderHovered(scaledMouseX, scaledMouseY)) {
                if (selectedCategory != category) {
                    selectedCategory = category;
                    widgetScrollOffset = 0;
                    initializeCategories();
                }
                return true;
            }
        }

        if (selectedCategory != null) {
            List<Widget> widgetsToCheck = getFilteredWidgetsForCategory(selectedCategory);

            for (Widget widget : widgetsToCheck) {
                boolean handled = widget.mouseClicked(scaledMouseX, scaledMouseY, button);
                if (handled) return true;
            }
        }

        if (selectedCategory != null) {
            if (selectedCategory.mouseClickedWidgets(scaledMouseX, scaledMouseY, button)) {
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button != 0) return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);

        float scale = config.configScale;
        double scaledMouseX = mouseX / scale;
        double scaledMouseY = mouseY / scale;
        double scaledOffsetX = deltaX / scale;
        double scaledOffsetY = deltaY / scale;

        if (selectedCategory != null) {
            for (Widget widget : selectedCategory.widgets) {

                boolean handled = widget.mouseDragged(scaledMouseX, scaledMouseY, button, scaledOffsetX, scaledOffsetY);
                if (handled) return true;

            }
        }

        if (selectedCategory != null) {
            if (selectedCategory.mouseDraggedWidgets(scaledMouseX, scaledMouseY, button, scaledOffsetX, scaledOffsetY)) {
                return true;
            }
        }

        if (draggingPanel) {
            panelX = (int) (scaledMouseX - dragOffsetX);
            panelY = (int) (scaledMouseY - dragOffsetY);
            initializeCategories();
            return true;
        }

        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseReleased(mouseX, mouseY, button);

        float scale = config.configScale;
        double scaledMouseX = mouseX / scale;
        double scaledMouseY = mouseY / scale;

        if (selectedCategory != null) {
            for (Widget widget : selectedCategory.widgets) {
                boolean handled = widget.mouseReleased(scaledMouseX, scaledMouseY, button);
                if (handled) return true;
            }
        }

        if (selectedCategory != null) {
            if (selectedCategory.mouseReleasedWidgets(scaledMouseX, scaledMouseY, button)) {
                return true;
            }
        }

        if (draggingPanel) {
            draggingPanel = false;
            return true;
        }

        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        float scale = config.configScale;
        double scaledMouseX = mouseX / scale;
        double scaledMouseY = mouseY / scale;

        if (scaledMouseX >= panelX && scaledMouseX <= panelX + PANEL_WIDTH &&
                scaledMouseY >= panelY + TITLE_BAR_HEIGHT && scaledMouseY <= panelY + PANEL_HEIGHT) {

            if (scaledMouseX >= widgetX && scaledMouseX <= widgetX + widgetWidth) {
                if (selectedCategory != null) {
                    int widgetsContentHeight = selectedCategory.currentY;
                    int maxScroll = Math.max(0, widgetsContentHeight - (PANEL_HEIGHT - TITLE_BAR_HEIGHT - 20));
                    widgetScrollOffset = Math.max(0, Math.min(maxScroll, widgetScrollOffset - (int) (verticalAmount * 20)));
                    return true;
                }
            } else if (scaledMouseX >= panelX + 10 && scaledMouseX <= panelX + categoriesWidth + 10) {
                int maxScroll = Math.max(0, categoriesHeight - (PANEL_HEIGHT - TITLE_BAR_HEIGHT - 20));
                scrollOffset = Math.max(0, Math.min(maxScroll, scrollOffset - (int) (verticalAmount * 20)));
                initializeCategories();
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchBar.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (searchBar.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }



    @Override
    public void close() {
        ConfigManager.save();
        if (selectedCategory != null){
            config.selectedCategory = selectedCategory.getName();
        }
        super.close();
    }

    @Override
    public boolean shouldPause() {
        return false;
    }


    public static int getPanelY(){
        return panelY;
    }
}