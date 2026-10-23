package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.sound.SoundEntry;
import net.minecraft.client.sound.SoundManager;
import net.modificationstation.stationapi.api.client.sound.CustomSoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.Unique;

import java.net.URL;

@Mixin(SoundManager.class)
public abstract class ExtendedSoundsLoaderMixin {

    @Shadow private SoundEntry sounds;

    @Unique
    private static final String[] SOUND_FILES = {
            "entering_water1.ogg",
            "entering_water2.ogg",
            "entering_water3.ogg",
            "exiting_water1.ogg",
            "exiting_water2.ogg",
            "exiting_water3.ogg",
            "furnace_fire_crackle1.ogg",
            "furnace_fire_crackle2.ogg",
            "furnace_fire_crackle3.ogg",
            "furnace_fire_crackle4.ogg",
            "furnace_fire_crackle5.ogg",
            "boat_water1.ogg",
            "boat_water2.ogg",
            "boat_water3.ogg",
            "boat_water4.ogg",
            "boat_water5.ogg",
            "boat_water6.ogg",
            "boat_water7.ogg",
            "boat_water8.ogg",
            "squid_idle1.ogg",
            "squid_idle2.ogg",
            "squid_idle3.ogg",
            "squid_idle4.ogg",
            "squid_idle5.ogg",
            "squid_hit1.ogg",
            "squid_hit2.ogg",
            "squid_hit3.ogg",
            "squid_hit4.ogg",
            "squid_death1.ogg",
            "squid_death2.ogg",
            "squid_death3.ogg",
            "ladder_hit1.ogg",
            "ladder_hit2.ogg",
            "ladder_hit3.ogg",
            "ladder_hit4.ogg",
            "ladder_hit5.ogg"
    };

    @Inject(method = "loadSounds", at = @At("TAIL"))
    private void extended_sounds$loadOwnAudio(GameOptions options, CallbackInfo ci) {
        try {
            ClassLoader classLoader = ExtendedSoundsLoaderMixin.class.getClassLoader();
            jockeyphilosopher.extended_sounds.ExtendedSoundsLoop.init();

            for (String fileName : SOUND_FILES) {
                URL fileUrl = classLoader.getResource("assets/extended_sounds/stationapi/sounds/sound/" + fileName);
                if (fileUrl != null) {
                    String soundId = "extended_sounds:" + fileName;
                    ((CustomSoundMap) this.sounds).putSound(soundId, fileUrl);
                    jockeyphilosopher.extended_sounds.ExtendedSoundsLoop.preload(fileUrl, fileName);
                    // System.out.println("EXTENDED_SOUNDS registered: " + soundId);
                } else {
                   // System.out.println("EXTENDED_SOUNDS NOT FOUND: " + fileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}