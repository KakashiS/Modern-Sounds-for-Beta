package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundManager;
import net.modificationstation.stationapi.api.client.sound.CustomSoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.URL;

@Mixin(SoundManager.class)
public abstract class ExtendedSoundsLoaderMixin {

    @Shadow private SoundEntry sounds;

    @Unique
    private static final String BASE = "assets/extended_sounds/stationapi/sounds/sound/";

    @Unique
    private static final String[] SOUND_FILES = {
            // player
            "player/entering_water1.ogg",
            "player/entering_water2.ogg",
            "player/entering_water3.ogg",
            "player/exiting_water1.ogg",
            "player/exiting_water2.ogg",
            "player/exiting_water3.ogg",
            "player/ladder_hit1.ogg",
            "player/ladder_hit2.ogg",
            "player/ladder_hit3.ogg",
            "player/ladder_hit4.ogg",
            "player/ladder_hit5.ogg",
            // block
            "block/furnace/furnace_fire_crackle1.ogg",
            "block/furnace/furnace_fire_crackle2.ogg",
            "block/furnace/furnace_fire_crackle3.ogg",
            "block/furnace/furnace_fire_crackle4.ogg",
            "block/furnace/furnace_fire_crackle5.ogg",
            // entity
            "entity/boat/boat_water1.ogg",
            "entity/boat/boat_water2.ogg",
            "entity/boat/boat_water3.ogg",
            "entity/boat/boat_water4.ogg",
            "entity/boat/boat_water5.ogg",
            "entity/boat/boat_water6.ogg",
            "entity/boat/boat_water7.ogg",
            "entity/boat/boat_water8.ogg",
            "entity/cow/cow_milk1.ogg",
            "entity/cow/cow_milk2.ogg",
            "entity/cow/cow_milk3.ogg",
            "entity/squid/squid_idle1.ogg",
            "entity/squid/squid_idle2.ogg",
            "entity/squid/squid_idle3.ogg",
            "entity/squid/squid_idle4.ogg",
            "entity/squid/squid_idle5.ogg",
            "entity/squid/squid_hit1.ogg",
            "entity/squid/squid_hit2.ogg",
            "entity/squid/squid_hit3.ogg",
            "entity/squid/squid_hit4.ogg",
            "entity/squid/squid_death1.ogg",
            "entity/squid/squid_death2.ogg",
            "entity/squid/squid_death3.ogg",
            "entity/painting/painting_place1.ogg",
            "entity/painting/painting_place2.ogg",
            "entity/painting/painting_place3.ogg",
            "entity/painting/painting_place4.ogg",
            "entity/painting/painting_break1.ogg",
            "entity/painting/painting_break2.ogg",
            "entity/painting/painting_break3.ogg",
            // item
            "item/armor/equip_generic1.ogg",
            "item/armor/equip_generic2.ogg",
            "item/armor/equip_generic3.ogg",
            "item/armor/equip_generic4.ogg",
            "item/armor/equip_iron1.ogg",
            "item/armor/equip_iron2.ogg",
            "item/armor/equip_iron3.ogg",
            "item/armor/equip_iron4.ogg",
            "item/armor/equip_diamond1.ogg",
            "item/armor/equip_diamond2.ogg",
            "item/armor/equip_diamond3.ogg",
            "item/armor/equip_diamond4.ogg",
            "item/bucket/fill_water_bucket1.ogg",
            "item/bucket/fill_water_bucket2.ogg",
            "item/bucket/fill_water_bucket3.ogg",
            "item/bucket/fill_lava_bucket1.ogg",
            "item/bucket/fill_lava_bucket2.ogg",
            "item/bucket/fill_lava_bucket3.ogg",
            "item/bucket/empty_water_bucket1.ogg",
            "item/bucket/empty_water_bucket2.ogg",
            "item/bucket/empty_water_bucket3.ogg",
            "item/bucket/empty_lava_bucket1.ogg",
            "item/bucket/empty_lava_bucket2.ogg",
            "item/bucket/empty_lava_bucket3.ogg",
            "item/fishing_rod/fishing_rod_cast.ogg",
            "item/fishing_rod/bobber_retrieve1.ogg",
            "item/fishing_rod/bobber_retrieve2.ogg",
            "item/fishing_rod/bobber_retrieve3.ogg",
            "item/flint_and_steel/flint_and_steel_click.ogg",
            "item/bone_meal/bonemeal1.ogg",
            "item/bone_meal/bonemeal2.ogg",
            "item/bone_meal/bonemeal3.ogg",
            "item/bone_meal/bonemeal4.ogg"
    };

    @Inject(method = "loadSounds", at = @At("TAIL"))
    private void extended_sounds$loadOwnAudio(GameOptions options, CallbackInfo ci) {
        try {
            jockeyphilosopher.extended_sounds.ExtendedSoundsLoop.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ClassLoader classLoader = ExtendedSoundsLoaderMixin.class.getClassLoader();

        for (String path : SOUND_FILES) {
            try {
                URL fileUrl = classLoader.getResource(BASE + path);
                if (fileUrl != null) {
                    String fileName = path.substring(path.lastIndexOf('/') + 1);
                    ((CustomSoundMap) this.sounds).putSound("extended_sounds:" + fileName, fileUrl);
                    jockeyphilosopher.extended_sounds.ExtendedSoundsLoop.preload(fileUrl, "extended_sounds:" + fileName);
                } else {
                    System.out.println("EXTENDED_SOUNDS NOT FOUND: " + path);
                }
            } catch (Exception e) {
                System.out.println("EXTENDED_SOUNDS FAILED: " + path);
                e.printStackTrace();
            }
        }
    }
}