package schrumbo.bax.mixin.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import schrumbo.bax.features.mining.CommissionClaiming;

@Mixin(HandledScreen.class)
public class HandledScreenCloseMixin {

    @Inject(method = "close", at = @At("HEAD"))
    private void onClose(CallbackInfo ci) {
       CommissionClaiming.reset();
    }
}
