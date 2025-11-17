package schrumbo.bax.mixin.client;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import schrumbo.bax.features.dungeons.SecretClose;
import schrumbo.bax.features.mining.CommissionClaiming;
import schrumbo.bax.features.mining.MineshaftEnter;
import schrumbo.bax.features.misc.PetSwap;
import schrumbo.bax.utils.InventoryUtils;

@Mixin(HandledScreen.class)
public class HandledScreenClickMixin {
    @Unique
    private int ticksSinceOpen = 0;
    @Unique
    private boolean hasUpdatedPets = false;

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

    /**
     * handles key inputs for {@link PetSwap}
     * @param keyCode
     * @param scanCode
     * @param modifiers
     * @param cir
     */
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;

        if (PetSwap.handleKeyPress(screen, keyCode, scanCode, modifiers)) {
            cir.setReturnValue(true);
        }
    }

    /**
     * used to auto update pet names in case of pet lvlup
     * @param ci
     */
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;

        if (InventoryUtils.isInGui(screen, "Pets")) {
            ticksSinceOpen++;

            if (!hasUpdatedPets && ticksSinceOpen >= 10) {
                PetSwap.updatePetNames();
                hasUpdatedPets = true;
            }
        } else {
            ticksSinceOpen = 0;
            hasUpdatedPets = false;
        }
    }

}
