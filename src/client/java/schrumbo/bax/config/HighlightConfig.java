package schrumbo.bax.config;

import schrumbo.bax.utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;

public class HighlightConfig {

    /**
     * global toggle for the Mob highlighting
     */
    public boolean enabled = false;
    public boolean getEnabled(){
        return enabled;
    }
    public void setEnabled(boolean value){
        enabled = value;
    }


    /**
     * list of all mob names which should be highlighted
     */
    public List<String> highlightList = new ArrayList<>();

    /**
     * used to add a name to the List of highlighted entities
     * @param name
     */

    public void addName(String name){
        highlightList.add(name.toLowerCase());
        ConfigManager.saveHighlightConfig();
        ChatUtils.modMessage("Added " + name + " to list.");
    }

    /**
     * used to remove a name from the highlighted entities
     * @param name
     */
    public void removeName(String name){
        highlightList.remove(name.toLowerCase());
        ConfigManager.saveHighlightConfig();
        ChatUtils.modMessage("Removed " + name + " from list.");
    }

    /**
     * gets the list of highlighted entities
     * @return List of names which should get highlighted
     */
    public List<String> getHighlightList(){
        return highlightList;
    }

    /**
     * clears all entries in the list
     */
    public void clearHighlightList(){
        highlightList.clear();
        ConfigManager.saveHighlightConfig();
        ChatUtils.modMessage("Cleared list.");
    }


    /**
     * If enabled the highlight will NOT go through walls
     */
    public boolean depthCheck = true;

    public boolean getDepthCheck(){
        return depthCheck;
    }
    public void setDepthCheck(boolean value){
        depthCheck = value;
    }


    /**
     * color for the mob highlighting
     */
    public int highlightColor = 0xFFFFFFFF;
    public int getHighlightColor(){
        return highlightColor;
    }
    public void setHighlightColor(int color){
        highlightColor = color;
    }

    public float highlightOpacity = 1.0f;
    public float getHighlightOpacity(){
        return highlightOpacity;
    }
    public void setHighlightOpacity(float opacity){
        highlightOpacity = opacity;
    }




}
