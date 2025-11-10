package schrumbo.bax;

import net.fabricmc.api.ClientModInitializer;
import schrumbo.bax.config.Config;
import schrumbo.bax.config.ConfigManager;
import schrumbo.bax.features.dungeons.MobEsp;
import schrumbo.bax.features.mining.CommissionClaiming;
import schrumbo.bax.features.mining.DrillSwap;
import schrumbo.bax.utils.KeybindHandler;

public class BaxClient implements ClientModInitializer {
	public static final CommissionClaiming commissionClaiming = new CommissionClaiming();

	public static Config config;
	public static ConfigManager configManager;


	@Override
	public void onInitializeClient() {
		configManager = new ConfigManager();
		config = configManager.load();
		DrillSwap.register();
		MobEsp.register();
		KeybindHandler.register();
	}
}