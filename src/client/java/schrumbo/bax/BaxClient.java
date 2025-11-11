package schrumbo.bax;

import net.fabricmc.api.ClientModInitializer;
import schrumbo.bax.config.Config;
import schrumbo.bax.config.ConfigManager;
import schrumbo.bax.config.HighlightConfig;
import schrumbo.bax.features.mining.DrillSwap;
import schrumbo.bax.features.misc.Commands;
import schrumbo.bax.features.misc.MobHighlight;
import schrumbo.bax.utils.KeybindHandler;
import schrumbo.bax.utils.location.LocationManager;

public class BaxClient implements ClientModInitializer {
	public static Config config;
	public static HighlightConfig highlightConfig;
	public static ConfigManager configManager;


	@Override
	public void onInitializeClient() {

		//CONFIG INIT HERE
		configManager = new ConfigManager();
		config = ConfigManager.load();
		highlightConfig = ConfigManager.loadHighlightConfig();

		//FEATURE REGISTRATION HERE
		DrillSwap.register();
		MobHighlight.register();
		LocationManager.register();
		KeybindHandler.register();
		Commands.register();
	}
}