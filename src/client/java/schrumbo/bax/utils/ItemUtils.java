package schrumbo.bax.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    public boolean hasLoreContaining(ItemStack item, String string) {
        if (item.isEmpty()) return false;

        LoreComponent lore = item.get(DataComponentTypes.LORE);
        if (lore == null) return false;

        for (Text line : lore.lines()) {
            if (line.getString().contains(string)) {
                return true;
            }
        }
        return false;
    }
}
