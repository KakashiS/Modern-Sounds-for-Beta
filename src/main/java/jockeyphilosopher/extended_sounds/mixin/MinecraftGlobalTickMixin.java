package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.world.World;
import jockeyphilosopher.extended_sounds.ExtendedSoundsLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftGlobalTickMixin {

    @Shadow
    public Screen currentScreen;

    @Shadow
    public World world;

    @Shadow
    public GameOptions options;

    @Inject(method = "tick", at = @At("TAIL"))
    private void extended_sounds$globalTick(CallbackInfo ci) {
        boolean menuOpen =
                this.world == null
                        || (this.currentScreen != null && this.currentScreen.shouldPause());

        float masterVolume = this.options != null
                ? this.options.soundVolume
                : 1.0F;

        ExtendedSoundsLoop.globalTick(menuOpen, masterVolume);
    }
}
