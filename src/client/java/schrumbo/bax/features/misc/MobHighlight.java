package schrumbo.bax.features.misc;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.passive.WolfEntity;
import schrumbo.bax.Bax;
import schrumbo.bax.utils.entity.CustomPlayerEntity;
import schrumbo.bax.utils.entity.EntityUtils;
import schrumbo.bax.utils.location.Location;
import schrumbo.bax.utils.location.LocationManager;

import java.util.ArrayList;
import java.util.List;

import static schrumbo.bax.BaxClient.config;
import static schrumbo.bax.BaxClient.highlightConfig;

import static schrumbo.bax.utils.entity.EntityUtils.*;
import static schrumbo.bax.utils.render.RenderUtils.renderHitbox;

/**
 * highlights all mobs specified in the mob highlight list
 */
public class MobHighlight {
    public static void register(){
        WorldRenderEvents.AFTER_ENTITIES.register((context) -> {
            List<String> names = highlightConfig.getHighlightList();

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.world == null || client.player == null) return;

            Camera camera = context.camera();
            MatrixStack matrices = context.matrixStack();

            initHighlighting(camera, matrices);
        });
    }


    /**
     * highlighting registration goes here
     * @param camera
     * @param matrices
     */
    public static void initHighlighting(Camera camera, MatrixStack matrices){
        minisHighlight(camera, matrices);
        starredHighlight(camera, matrices);
        maniacHighlight(camera, matrices);
        witherHighlight(camera, matrices);
    }


    /**
     * handles miniboss highlighting if enabled
     * @param camera
     * @param matrices
     */
    private static void minisHighlight(Camera camera, MatrixStack matrices){
        if (LocationManager.currentLocation != Location.Dungeon)return;
        if (!config.minibossHighlight)return;

        List<Entity> shadowAssassin = new ArrayList<>(getEntityByName(CustomPlayerEntity.ShadowAssassin.getDisplayName()));
        for (Entity sa : shadowAssassin){
            renderHitbox(sa, camera, matrices, config.colorWithAlpha(config.shadowAssassinColor, 1.0f));
        }

        List<Entity> lostAdventurer = new ArrayList<>(getEntityByName(CustomPlayerEntity.LostAdventurer.getDisplayName()));
        for (Entity la : lostAdventurer){
            renderHitbox(la, camera, matrices, config.colorWithAlpha(config.lostAdventurerColor, 1.0f));
        }

        List<Entity> angryArcheologist = new ArrayList<>(getEntityByName(CustomPlayerEntity.AngryArcheologist.getDisplayName()));
        for (Entity aa : angryArcheologist){
            renderHitbox(aa, camera, matrices, config.colorWithAlpha(config.angryArcheologistColor, 1.0f));
        }
    }

    /**
     * handles the starred mob highlight
     * @param camera
     * @param matrices
     */
    private static void starredHighlight(Camera camera, MatrixStack matrices){
        if (LocationManager.currentLocation != Location.Dungeon);
        if (!config.starredHighlight)return;

        List<Entity> starredMobs = new ArrayList<>(getEntityByArmorStandName("✯"));
        for (Entity starred : starredMobs){
            String name = starred.getName().getString().toLowerCase();

            //miniboss check
            if (!(name.contains(CustomPlayerEntity.ShadowAssassin.getDisplayName().toLowerCase())
                || name.contains(CustomPlayerEntity.LostAdventurer.getDisplayName().toLowerCase())
                || name. contains(CustomPlayerEntity.AngryArcheologist.getDisplayName().toLowerCase())
            )){
                renderHitbox(starred, camera, matrices, config.colorWithAlpha(config.starredColor, 1.0f));
            }
        }
    }

    /**
     * handles maniac highlight
     * @param camera
     * @param matrices
     */
    private static void maniacHighlight(Camera camera, MatrixStack matrices){
        if (LocationManager.currentLocation != Location.Mineshaft)return;
        if (!config.maniacHighlight)return;
        List<Entity> maniacs = new ArrayList<>();
        maniacs.addAll(getEntityByName(CustomPlayerEntity.GlaciteBowman.getDisplayName()));
        maniacs.addAll(getEntityByName(CustomPlayerEntity.GlaciteMage.getDisplayName()));
        maniacs.addAll(getEntityByName(CustomPlayerEntity.GlaciteCaver.getDisplayName()));
        maniacs.addAll(getEntitiesByType(WolfEntity.class));

        for (Entity maniac : maniacs){
            renderHitbox(maniac, camera, matrices, config.colorWithAlpha(config.maniacColor, 1.0f));
        }
    }

    /**
     * handles Wither Highlight, used for F7/M7
     * @param camera
     * @param matrices
     */
    private static void witherHighlight(Camera camera, MatrixStack matrices){
        if (LocationManager.currentLocation != Location.Dungeon)return;
        if (!config.witherHighlight)return;
        List<Entity> withers = new ArrayList<>(getEntitiesByType(WitherEntity.class));

        for (Entity wither : withers){
            if (wither.getHeight() >= 3.5f){
                renderHitbox(wither, camera, matrices, config.colorWithAlpha(config.witherColor, 1.0f));
            }

        }
    }




}
