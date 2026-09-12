package jockeyphilosopher.extended_sounds;

import jockeyphilosopher.extended_sounds.mixin.SoundManagerAccessor;
import paulscode.sound.SoundSystem;

import java.net.URL;

public class ExtendedSoundsLoop {

    private static final float UNDERWATER_BASE_VOLUME = 0.25F;
    private static final float MINECART_BASE_VOLUME = 0.09F;
    private static final float FALL_WIND_TARGET_VOLUME = 0.2F;
    private static final float FALL_WIND_FADE_STEP = 0.02F;

    private static float masterVolume = 1.0F;
    private static boolean menuOpen = false;

    private static URL underwaterUrl;
    private static boolean underwaterPlaying = false;
    private static boolean underwaterPausedByMenu = false;

    private static URL minecartUrl;
    private static boolean minecartPlaying = false;
    private static boolean minecartPausedByMenu = false;

    private static URL fallWindUrl;
    private static boolean fallWindPlaying = false;
    private static boolean fallWindPausedByMenu = false;
    private static float fallWindFadeProgress = 0F;

    private static int preloadCounter = 0;

    public static void init() {
        try {
            ClassLoader classLoader = ExtendedSoundsLoop.class.getClassLoader();
            String base = "assets/extended_sounds/stationapi/sounds/sound/";

            underwaterUrl = classLoader.getResource(base + "underwater_loop.ogg");
            minecartUrl = classLoader.getResource(base + "minecart_rolling_loop.ogg");
            fallWindUrl = classLoader.getResource(base + "fall_wind_loop.ogg");

            // System.out.println("EXTENDED_SOUNDS loop file (underwater): " + underwaterUrl);
            // System.out.println("EXTENDED_SOUNDS loop file (minecart): " + minecartUrl);
            // System.out.println("EXTENDED_SOUNDS loop file (fallWind): " + fallWindUrl);

            preload(underwaterUrl, "underwater_loop.ogg");
            preload(minecartUrl, "minecart_rolling_loop.ogg");
            preload(fallWindUrl, "fall_wind_loop.ogg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void preload(URL url, String codecHint) {
        if (url == null) return;
        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem == null) return;
        String sourceName = "extended_sounds_preload_" + (preloadCounter++);
        soundSystem.newSource(false, sourceName, url, codecHint, false, 0f, 0f, 0f, 0, 0f);
        soundSystem.removeSource(sourceName);
    }

    public static void globalTick(boolean menuOpenNow, float masterVolumeNow) {
        menuOpen = menuOpenNow;
        masterVolume = masterVolumeNow;

        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem == null) return;

        if (underwaterPlaying) {
            if (menuOpen && !underwaterPausedByMenu) {
                soundSystem.pause("extended_sounds_underwater");
                underwaterPausedByMenu = true;
            } else if (!menuOpen && underwaterPausedByMenu) {
                soundSystem.play("extended_sounds_underwater");
                underwaterPausedByMenu = false;
            }
            if (!menuOpen) {
                soundSystem.setVolume("extended_sounds_underwater", UNDERWATER_BASE_VOLUME * masterVolume);
            }
        }

        if (minecartPlaying) {
            if (menuOpen && !minecartPausedByMenu) {
                soundSystem.pause("extended_sounds_minecart");
                minecartPausedByMenu = true;
            } else if (!menuOpen && minecartPausedByMenu) {
                soundSystem.play("extended_sounds_minecart");
                minecartPausedByMenu = false;
            }
            if (!menuOpen) {
                soundSystem.setVolume("extended_sounds_minecart", MINECART_BASE_VOLUME * masterVolume);
            }
        }

        if (fallWindPlaying) {
            if (menuOpen && !fallWindPausedByMenu) {
                soundSystem.pause("extended_sounds_fallwind");
                fallWindPausedByMenu = true;
            } else if (!menuOpen && fallWindPausedByMenu) {
                soundSystem.play("extended_sounds_fallwind");
                fallWindPausedByMenu = false;
            }
            if (!menuOpen) {
                soundSystem.setVolume("extended_sounds_fallwind", fallWindFadeProgress * masterVolume);
            }
        }
    }


    public static void startUnderwater() {
        if (underwaterPlaying || underwaterUrl == null) return;
        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem == null) return;
        soundSystem.newSource(false, "extended_sounds_underwater", underwaterUrl, "underwater_loop.ogg", true, 0f, 0f, 0f, 0, 0f);
        soundSystem.setVolume("extended_sounds_underwater", UNDERWATER_BASE_VOLUME * masterVolume);
        soundSystem.play("extended_sounds_underwater");
        underwaterPlaying = true;
        underwaterPausedByMenu = false;
    }

    public static void stopUnderwater() {
        if (!underwaterPlaying) return;
        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem != null) {
            soundSystem.stop("extended_sounds_underwater");
            soundSystem.removeSource("extended_sounds_underwater");
        }
        underwaterPlaying = false;
        underwaterPausedByMenu = false;
    }

    public static void startMinecart(float x, float y, float z) {
        if (minecartPlaying || minecartUrl == null) return;
        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem == null) return;
        soundSystem.newSource(false, "extended_sounds_minecart", minecartUrl, "minecart_rolling_loop.ogg", true, x, y, z, 2, 16f);
        soundSystem.setVolume("extended_sounds_minecart", MINECART_BASE_VOLUME * masterVolume);
        soundSystem.play("extended_sounds_minecart");
        minecartPlaying = true;
        minecartPausedByMenu = false;
    }

    public static void updateMinecartPosition(float x, float y, float z) {
        if (!minecartPlaying) return;
        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem != null) {
            soundSystem.setPosition("extended_sounds_minecart", x, y, z);
        }
    }

    public static void stopMinecart() {
        if (!minecartPlaying) return;
        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem != null) {
            soundSystem.stop("extended_sounds_minecart");
            soundSystem.removeSource("extended_sounds_minecart");
        }
        minecartPlaying = false;
        minecartPausedByMenu = false;
    }


    public static void startFallWind() {
        if (fallWindPlaying || fallWindUrl == null) return;
        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem == null) return;
        soundSystem.newSource(false, "extended_sounds_fallwind", fallWindUrl, "fall_wind_loop.ogg", true, 0f, 0f, 0f, 0, 0f);
        fallWindFadeProgress = 0F;
        soundSystem.setVolume("extended_sounds_fallwind", 0F);
        soundSystem.play("extended_sounds_fallwind");
        fallWindPlaying = true;
        fallWindPausedByMenu = false;
    }

    public static void tickFallWindFade() {
        if (!fallWindPlaying || fallWindFadeProgress >= FALL_WIND_TARGET_VOLUME) return;
        fallWindFadeProgress = Math.min(FALL_WIND_TARGET_VOLUME, fallWindFadeProgress + FALL_WIND_FADE_STEP);
    }

    public static void stopFallWind() {
        if (!fallWindPlaying) return;
        SoundSystem soundSystem = SoundManagerAccessor.extended_sounds$getSoundSystem();
        if (soundSystem != null) {
            soundSystem.stop("extended_sounds_fallwind");
            soundSystem.removeSource("extended_sounds_fallwind");
        }
        fallWindPlaying = false;
        fallWindPausedByMenu = false;
        fallWindFadeProgress = 0F;
    }
}