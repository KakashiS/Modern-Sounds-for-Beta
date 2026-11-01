package jockeyphilosopher.extended_sounds;

import net.minecraft.client.gui.screen.ingame.DoubleChestScreen;
import net.minecraft.world.World;

public class ChestSoundTracker {

    private static final int SCREEN_WAIT_TICKS = 60;

    private static boolean pending = false;
    private static int chestX;
    private static int chestY;
    private static int chestZ;
    private static Object openedScreen = null;
    private static int waited = 0;

    public static void onChestOpened(int x, int y, int z) {
        pending = true;
        chestX = x;
        chestY = y;
        chestZ = z;
        openedScreen = null;
        waited = 0;
    }

    public static void reset() {
        pending = false;
        openedScreen = null;
        waited = 0;
    }

    public static void tick(Object currentScreen, World world) {
        if (!pending) return;

        if (world == null) {
            reset();
            return;
        }

        if (openedScreen == null) {
            if (currentScreen instanceof DoubleChestScreen) {
                openedScreen = currentScreen;
            } else if (++waited > SCREEN_WAIT_TICKS) {
                pending = false;
            }
            return;
        }

        if (currentScreen != openedScreen) {
            pending = false;
            openedScreen = null;

            if (Config.config.environment.enableChestSounds) {
                float pitch = world.random.nextFloat() * 0.2F + 0.9F;
                world.playSound(
                        (double) chestX + 0.5D, (double) chestY + 0.5D, (double) chestZ + 0.5D,
                        "extended_sounds:chest_close", 0.4F, pitch);
            }
        }
    }
}