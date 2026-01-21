package schrumbo.bax;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import schrumbo.bax.config.Config;
import schrumbo.bax.config.ConfigManager;
import schrumbo.bax.features.chat.PetNotification;
import schrumbo.bax.utils.KeybindHandler;
import schrumbo.bax.utils.location.LocationManager;

public class BaxClient implements ClientModInitializer {
	public static Config config;

	public static ConfigManager configManager;
	public static MinecraftClient client = MinecraftClient.getInstance();


	@Override
	public void onInitializeClient() {

		//CONFIG INIT HERE
		configManager = new ConfigManager();
		config = ConfigManager.load();

		LocationManager.register();
		KeybindHandler.register();
		PetNotification.register();

	}
}