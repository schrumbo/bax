package schrumbo.bax.features.fishing;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.sound.SoundEvents;
import schrumbo.bax.Bax;
import schrumbo.bax.utils.ChatUtils;
import schrumbo.bax.utils.ItemUtils;
import schrumbo.bax.utils.Utils;
import schrumbo.bax.utils.entity.EntityUtils;

import static schrumbo.bax.BaxClient.client;
import static schrumbo.bax.BaxClient.config;


public class AutoReel {

    private static double lastReelTime = 0;
    //reel cd in ms
    private static final double REEL_COOLDOWN = 1000;

    /**
     * registers auto reel in module
     */
    public static void register(){
        ClientTickEvents.END_CLIENT_TICK.register(AutoReel::onClientTick);
    }

    /**
     * checks if theres a fish every tick and if so, reels in the rod
     * @param client the client
     */
    private static void onClientTick(MinecraftClient client){
        if(!config.getAutoReelIn())return;

        if (System.currentTimeMillis() - lastReelTime < REEL_COOLDOWN)return;
        if (detectFish()){
            cast();
            lastReelTime = System.currentTimeMillis();
        }
    }

    /**
     * checks if the rod is ready to reel in
     * @return true if should reel in
     */
    private static boolean detectFish(){
        if (!ItemUtils.holdingRod())return false;

        Entity fish = EntityUtils.getArmorStand("!!!");
        if (fish != null){
            Bax.LOGGER.info("detected fish on hook");
            return true;
        }
        return false;
    }

    /**
     * casts rod
     */
    private static void cast(){
        if (!ItemUtils.holdingRod())return;
        Bax.LOGGER.info("casting Rod...");
        Utils.runAfterDelay(ItemUtils::useItem, 200);
    }

    /**
     * handles bobber state
     */
    private static void handleBobberState(){
        if (client.player ==  null || client.player.fishHook == null)return;
        FishingBobberEntity bobber = client.player.fishHook;
        BobberState bobberState = getBobberState(bobber);
        Bax.LOGGER.info(String.valueOf(bobberState));
        if (bobberState == BobberState.IN_WATER || bobberState == BobberState.IN_AIR)return;

        if (bobberState == BobberState.ON_GROUND){
            config.setAutoReelIn(false);
            ChatUtils.modMessage("Bobber on Ground. please fish in water!");
            Utils.playSound(SoundEvents.BLOCK_GLASS_BREAK);
            client.execute(ItemUtils::useItem);
            return;
        }

        ChatUtils.modMessage("Bobber not in water, recasting");
        Utils.playSound(SoundEvents.BLOCK_GLASS_BREAK);

        client.execute(ItemUtils::useItem);

    }

    /**
     * checks the current state of the bobber.
     *
     * Technically the bobber is never in the air because on Hypixel the bobber is always attached to an armor stand
     * @return Bobber state
     */
    private static BobberState getBobberState(FishingBobberEntity bobber){
        if (client.player == null || client.player.fishHook == null) {
            return BobberState.NO_BOBBER;
        }

        if (!(bobber.getHookedEntity() instanceof ArmorStandEntity)) {
            return BobberState.HOOKED_ENTITY;
        }

        if (bobber.isTouchingWater() || bobber.isInFluid()) {
            return BobberState.IN_WATER;
        }

        if (bobber.isOnGround()) {
            return BobberState.ON_GROUND;
        }

        return BobberState.IN_AIR;
    }

    /**
     * all possible bobber states
     */
    enum BobberState {
        NO_BOBBER,
        IN_WATER,
        ON_GROUND,
        HOOKED_ENTITY,
        IN_AIR
    }



}



