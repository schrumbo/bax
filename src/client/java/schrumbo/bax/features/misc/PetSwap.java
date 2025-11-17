package schrumbo.bax.features.misc;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;
import schrumbo.bax.config.Config;
import schrumbo.bax.config.ConfigManager;
import schrumbo.bax.utils.ChatUtils;
import schrumbo.bax.utils.InventoryUtils;
import schrumbo.bax.utils.ItemUtils;
import schrumbo.bax.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static schrumbo.bax.BaxClient.client;
import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.utils.InventoryUtils.*;

/**
 * Swaps to a pet by pressing keybinds in the pet menu
 */
public class PetSwap {
    private static final List<KeyBinding> keybinds = new ArrayList<>();
    private static final String PET_CATEGORY = "Bax - Pet Keys";

    private static final Map<Integer, PetData> pets = new HashMap<>();

    public static void register(){
        initKeybinds();
    }

    /**
     * Registers all pet keybinds
     */
    private static void initKeybinds(){
        for (int i = 1; i <= 9; i++) {
            KeyBinding petKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "Pet " + i,
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_UNKNOWN,
                    PET_CATEGORY
            ));
            keybinds.add(petKey);
        }
    }

    /**
     * Handles keybind presses IN THE PET MENU
     */
    public static boolean handleKeyPress(HandledScreen<?> screen, int keyCode, int scanCode, int modifiers) {
        if (!config.petSwap)return false;
        if (!isInGui(screen, "Pets")) return false;
        for (int i = 0; i < keybinds.size(); i++) {
            KeyBinding keybind = keybinds.get(i);
            if (keybind.matchesKey(keyCode, scanCode)) {
                int petSlot = i + 1;
                swapToPet(petSlot);
                return true;
            }
        }
        return false;
    }

    /**
     * Swaps to the specified pet
     */
    private static void swapToPet(int petSlot) {
        if (client.player == null) return;

        PetData pet = pets.get(petSlot);
        if (pet == null) {
            ChatUtils.modMessage("§cNo pet saved for slot " + petSlot + "!");
            ChatUtils.modMessage("§eUse §6/bax pet set " + petSlot + " §ewhile holding a pet");
            return;
        }

        int slotId = ItemUtils.getSlotByUUID(pet.uuid);

        if (slotId == -1) {
            ChatUtils.modMessage("§cCouldn't find pet §6" + pet.name + " §cin menu!");
            return;
        }

        if (client.player.currentScreenHandler == null) return;
        ItemStack petItem = client.player.currentScreenHandler.getSlot(slotId).getStack();

        if (isSummoned(petItem)) {
            closeGui();
            return;
        }
        clickSlotAt(slotId);
    }


    /**
     * Sets a pet for a slot from the player's hand
     */
    public static void setPetFromHand(int slot) {
        if (client.player == null) {
            return;
        }

        if (slot < 1 || slot > 9) {
            ChatUtils.modMessage("§cSlot must be between 1 and 9!");
            return;
        }

        ItemStack heldItem = ItemUtils.getHeldItem();
        if (heldItem == null || heldItem.isEmpty()) {
            ChatUtils.modMessage("§cYou're not holding an item!");
            return;
        }

        String uuid = ItemUtils.getItemUUID(heldItem);
        if (uuid == null) {
            ChatUtils.modMessage("§cThis item doesn't have a UUID! Not a pet?");
            return;
        }

        String name = StringUtils.noColorCodes(heldItem.getName().getString());

        PetData petData = new PetData(name, uuid, slot);
        pets.put(slot, petData);

        saveToConfig();

        ChatUtils.modMessage("§aSaved pet §6" + name + " §ato slot §6" + slot + "§a!");
    }

    /**
     * Removes a pet from a slot
     */
    public static void removePet(int slot) {
        if (slot < 1 || slot > 9) {
            ChatUtils.modMessage("§cSlot must be between 1 and 9!");
            return;
        }

        PetData removed = pets.remove(slot);
        if (removed != null) {
            saveToConfig();
            ChatUtils.modMessage("§aRemoved pet §6" + removed.name + " §afrom slot §6" + slot + "§a!");
        } else {
            ChatUtils.modMessage("§cNo pet saved in slot " + slot + "!");
        }
    }

    /**
     * Lists all saved pets
     */
    public static void listPets() {
        if (pets.isEmpty()) {
            ChatUtils.modMessage("§cNo pets saved yet!");
            ChatUtils.modMessage("§eUse §6/bax pet set <slot> §ewhile holding a pet");
            return;
        }

        ChatUtils.modMessage("§6§l=== Saved Pets ===");
        for (int i = 1; i <= 9; i++) {
            PetData pet = pets.get(i);
            if (pet != null) {
                KeyBinding keybind = getKeybind(i);
                String keyName = keybind != null ? keybind.getBoundKeyLocalizedText().getString() : "Not bound";

                ChatUtils.modMessage("§6Slot " + i + " §7[" + keyName + "]§f: §e" + pet.name);
            }
        }
    }

    /**
     * Shows details for a pet: slot, name, uuid, kebinding
     */
    public static void showPetInfo(int slot) {
        if (slot < 1 || slot > 9) {
            ChatUtils.modMessage("§cSlot must be between 1 and 9!");
            return;
        }

        PetData pet = pets.get(slot);
        if (pet == null) {
            ChatUtils.modMessage("§cNo pet saved in slot " + slot + "!");
            return;
        }

        KeyBinding keybind = getKeybind(slot);
        String keyName = keybind != null ? keybind.getBoundKeyLocalizedText().getString() : "Not bound";

        ChatUtils.modMessage("§6§l=== Pet Slot " + slot + " ===");
        ChatUtils.modMessage("§6Name: §e" + pet.name);
        ChatUtils.modMessage("§6UUID: §7" + pet.uuid);
        ChatUtils.modMessage("§6Keybind: §7" + keyName);
    }

    /**
     * clears pets list
     */
    public static void clearAllPets() {
        if (pets.isEmpty()) {
            ChatUtils.modMessage("§cNo pets to clear!");
            return;
        }
        int count = pets.size();
        pets.clear();

        saveToConfig();
        ChatUtils.modMessage("§aCleared all " + count + " pet(s)!");
    }

    /**
     * Gets a keybind for a pet number
     */
    public static KeyBinding getKeybind(int petNumber) {
        if (petNumber >= 1 && petNumber <= 9) {
            return keybinds.get(petNumber - 1);
        }
        return null;
    }


    /**
     * Loads pets from config
     */
    public static void loadFromConfig(Map<Integer, PetData> savedPets) {
        pets.clear();
        if (savedPets != null) {
            pets.putAll(savedPets);
        }
    }
    private static void saveToConfig() {
        config.savedPets.clear();
        config.savedPets.putAll(pets);
        ConfigManager.save();
    }

    /**
     * Pet data class
     */
    public static class PetData {
        public String name;
        public String uuid;
        public int index;

        public PetData(String name, String uuid, int index) {
            this.name = name;
            this.uuid = uuid;
            this.index = index;
        }
    }

    /**
     * checks if a pet is currently summoned
     * @param item pet Item
     * @return true if summoned
     */
    private static boolean isSummoned(ItemStack item){
        return ItemUtils.loreContains(item, "Click to despawn!");
    }

    /**
     * updates pet names, important for the lvl to update
     */
    public static void updatePetNames() {
        if (!isInGui("Pets")) return;
        if (client.player == null || client.player.currentScreenHandler == null) return;

        boolean updated = false;

        for (Map.Entry<Integer, PetData> entry : pets.entrySet()) {
            PetData pet = entry.getValue();
            int slotId = ItemUtils.getSlotByUUID(pet.uuid);

            if (slotId != -1) {
                ItemStack petItem = client.player.currentScreenHandler.getSlot(slotId).getStack();
                String currentName = StringUtils.noColorCodes(petItem.getName().getString());

                if (!currentName.equals(pet.name)) {
                    pet.name = currentName;
                    updated = true;
                }
            }
        }

        if (updated) {
            saveToConfig();
        }
    }
}