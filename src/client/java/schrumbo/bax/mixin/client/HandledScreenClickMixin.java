package schrumbo.bax.mixin.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import schrumbo.bax.features.dungeons.SecretClose;
import schrumbo.bax.features.mining.CommissionClaiming;
import schrumbo.bax.features.mining.MineshaftEnter;

@Mixin(HandledScreen.class)
public class HandledScreenClickMixin {
    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    private void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;
        //commission claiming handling
        if (CommissionClaiming.handleClick(screen, mouseX, mouseY, button)) {
            cir.setReturnValue(true);
        }
        //mineshaft enter handling
        if(MineshaftEnter.handleClick(screen, mouseX, mouseY, button)){
            cir.setReturnValue(true);
        }
        //secret click handling
        if (SecretClose.handleClick(screen, mouseX, mouseY, button)){
            cir.setReturnValue(true);
        }
    }
}
