package schrumbo.bax.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.bax.BaxClient.client;

public class ItemUtils {

    /**
     * gets the currently held item in the player's main hand
     * @return
     */
    public static ItemStack getHeldItem(){
        if (client.player == null)return null;
        return client.player.getMainHandStack();
    }

    /**
     * checks if an item contains a string in its lore (without color codes)
     * @param stack
     * @param string
     * @return true if the given string is present in the item's lore
     */
    public static boolean loreContains(ItemStack stack, String string) {
        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore == null) return false;

        for (Text line : lore.lines()) {
            if (StringUtils.noColorCodes(line.getString().toLowerCase()).contains(string.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    /**
     * checks if the player is holding a fishing rod
     * @return true if player is holding a fishing Rod
     */
    public static boolean holdingRod(){
        if (client.player == null) return false;
        ItemStack activeItem = getHeldItem();
        if (activeItem == null) return false;
        return loreContains(activeItem, "FISHING ROD");
    }

    /**
     * uses the currently held item
     */
    public static void useItem(){
        if(client.interactionManager == null)return;
        client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
    }

    /**
     * checks if an item with a given name is present in the hotbar
     * @param name
     * @return slot of the item. if no match found return -1
     */
    public static int getItemSlotByName(String name){
        assert client.player != null;

        for (int i = 0; i < 9; i++){
            ItemStack item = client.player.getInventory().getStack(i);
            String itemName = StringUtils.noColorCodes(item.getName().getString().toLowerCase());
            if (itemName.contains(name.toLowerCase())){
                return i;
            }
        }
        return -1;
    }

    /**
     * checks if an item with a given string is present in the hotbar
     * @param lore
     * @return slot of the found item
     */
    public static int getItemSlotByLore(String lore){
        assert client.player != null;
        for (int i = 0; i < 9; i++){
            ItemStack item = client.player.getInventory().getStack(i);
            if (loreContains(item, lore)){
                return i;
            }
        }
        return -1;
    }

    /**
     * switches to a given hotbar slot
     * @param slot
     */
    public static void switchToSlot(int slot){
        if (client.player == null)return;
        if (slot >= 0 && slot <= 8){
            client.execute(() -> client.player.getInventory().setSelectedSlot(slot));
        }
    }

    /**
     * gets the uuid for an item
     * @param item
     * @return
     */
    public static String getItemUUID(ItemStack item) {
        NbtComponent customData = item.get(DataComponentTypes.CUSTOM_DATA);
        if (customData == null) return null;

        NbtCompound nbt = customData.copyNbt();

        return nbt.getString("uuid").orElse(null);
    }

    /**
     * gets a slot of an item with a specific uuid
     * @param uuid
     * @return
     */
    public static int getSlotByUUID(String uuid) {
        if (client.player == null) return -1;

        ScreenHandler handler = client.player.currentScreenHandler;
        if (handler == null) return -1;

        for (int i = 0; i < handler.slots.size(); i++) {
            ItemStack stack = handler.getSlot(i).getStack();
            if (stack.isEmpty()) continue;

            String itemUUID = getItemUUID(stack);
            if (uuid.equals(itemUUID)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * gets the player's armor
     * @return List of all armor items
     */
    public static List<ItemStack> getPlayerArmor(){
        List<ItemStack> result = new ArrayList<>();
        if (client.player == null) return result;
        result.add(client.player.getEquippedStack(EquipmentSlot.HEAD));
        result.add(client.player.getEquippedStack(EquipmentSlot.BODY));
        result.add(client.player.getEquippedStack(EquipmentSlot.LEGS));
        result.add(client.player.getEquippedStack(EquipmentSlot.FEET));
        return result;
    }



}
