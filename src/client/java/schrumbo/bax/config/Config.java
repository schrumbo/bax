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


    public boolean starredESP = false;
    public boolean getStarredESP(){
        return starredESP;
    }
    public void setStarredESP(boolean value){
        starredESP = value;
    }

    public boolean maniacESP = false;
    public boolean getManiacESP(){
        return maniacESP;
    }
    public void setManiacESP(boolean value){
        maniacESP = value;
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
