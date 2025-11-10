package schrumbo.bax.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ItemUtils {

    public static boolean loreContains(ItemStack stack, String string) {
        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore == null) return false;

        for (Text line : lore.lines()) {
            if (line.getString().contains(string)) {
                return true;
            }
        }

        return false;
    }
}
