package schrumbo.bax.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TablistUtils {

    public static List<String> getTablistLines() {
        List<String> lines = new ArrayList<>();
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null) {
            return lines;
        }

        if (client.getNetworkHandler() == null) {
            return lines;
        }

        Collection<PlayerListEntry> playerListEntries = client.getNetworkHandler().getPlayerList();

        for (PlayerListEntry entry : playerListEntries){
            String displayName = entry.getDisplayName() != null ? entry.getDisplayName().getString() : entry.getProfile().getName();

            lines.add(displayName);
        }

        return lines;
    }

}
