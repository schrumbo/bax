package schrumbo.bax.config;

import com.google.gson.annotations.SerializedName;

public class Config {
    public boolean easyCommissions = false;
    public boolean getEasyCommissions(){
        return easyCommissions;
    }
    public void setEasyCommissions(boolean value){
        easyCommissions = value;
    }

    @SerializedName("selectedCategory")
    public String selectedCategory = null;


    public boolean drillSwap = false;
    public boolean getDrillSwap(){
        return drillSwap;
    }
    public void setDrillSwap(boolean value){
        drillSwap = value;
    }

    public boolean easyShaftEnter = false;
    public boolean getEasyShaftEnter(){
        return easyShaftEnter;
    }
    public void setEasyShaftEnter(boolean value){
        easyShaftEnter = value;
    }

    //STARRED HIGHLIGHT
    public boolean starredHighlight = false;
    public boolean getStarredHighlight(){
        return starredHighlight;
    }
    public void setStarredHighlight(boolean value){
        starredHighlight = value;
    }

    public int starredColor = 0xFFfce80c;
    public void setStarredColor(int color){
        starredColor = color;
    }


    //MANIAC HIGHLIGHT
    public boolean maniacHighlight = false;
    public boolean getManiacHighlight(){
        return maniacHighlight;
    }
    public void setManiacHighlight(boolean value){
        maniacHighlight = value;
    }

    public int maniacColor =  0xFF1baff9;
    public void setManiacColor(int color){
        maniacColor = color;
    }

    //MINIBOSS HIGHLIGHT
    public boolean minibossHighlight = false;
    public boolean getMinibossHighlight(){
        return minibossHighlight;
    };
    public void setMinibossHighlight(boolean value){
        minibossHighlight = value;
    }

    public int shadowAssassinColor = 0xFF6d6d6d;
    public void setShadowAssassinColor(int color){
        shadowAssassinColor = color;
    }

    public int lostAdventurerColor = 0xFFc62b6e;
    public void setLostAdventurerColor(int color){
        lostAdventurerColor = color;
    }

    public int angryArcheologistColor = 0xFF4ed5ed;
    public void setAngryArcheologistColor(int color){
        angryArcheologistColor = color;
    }

    public int frozenAdventurerColor = 0xFFd6d6d6;
    public void setFrozenAdventurerColor(int color){
        frozenAdventurerColor = color;
    }

    //WITHER HIGHLIGHT
    public boolean witherHighlight = false;
    public boolean getWitherHighlight(){
        return witherHighlight;
    }
    public void setWitherHighlight(boolean value){
        witherHighlight = value;
    }

    public int witherColor = 0xFFfc2105;
    public void setWitherColor(int color){
        witherColor = color;
    }

    @SerializedName("guicolors")
    public ClickGUIColors guicolors = new ClickGUIColors();

    //ClickGuiColors
    public class ClickGUIColors{
        //GENERAL ACCENT
        @SerializedName("accent")
        public int accent = 0x5DADE2;

        //WIDGETS
        @SerializedName("text")
        public int text = 0xFFFFFFFF;

        @SerializedName("textSize")
        public final float textSize = 2.0f;

        @SerializedName("headingSize")
        public final float headingSize = 2.0f;

        @SerializedName("hoveredTextOpacity")
        public final float hoveredTextOpacity =  1.0f;

        @SerializedName("widgetBorderOpacity")
        public final float widgetBorderOpacity =  0.6f;

        @SerializedName("panelBorderOpacity")
        public final float panelBorderOpacity = 1.0f;

        @SerializedName("panelTitleBarOpacity")
        public final float panelTitleBarOpacity = 0.3f;

        @SerializedName("widgetAccentOpacity")
        public final float widgetAccentOpacity = 0.8f;

        @SerializedName("widgetBackground")
        public int widgetBackground = colorWithAlpha(0x2a2a2a, 0.5f);

        @SerializedName("widgetBackgroundHovered")
        public int widgetBackgroundHovered = colorWithAlpha(0x3a3a3a, 0.9f);

        @SerializedName("widgetBackgroundClicked")
        public int widgetBackgroundClicked = colorWithAlpha(0x1a1a1a, 0.90f);

        @SerializedName("sliderHandle")
        public int sliderHandle = colorWithAlpha(0xCCCCCC, 1.0f);

        @SerializedName("sliderHandleHovered")
        public int sliderHandleHovered = colorWithAlpha(0xFFFFFF, 1.0f);

        //PANEL
        @SerializedName("panelBackground")
        public int panelBackground = colorWithAlpha(0x1a1a1a, 0.95f);

    }

    public void setAccentColor(int color) {
        guicolors.accent = color;
    }

    public int colorWithAlpha(int color, float opacity){
        int alpha = (int) (opacity * 255);
        return (alpha << 24) | (color & 0x00FFFFFF);
    }

    public float configScale = 1.0f;
}
