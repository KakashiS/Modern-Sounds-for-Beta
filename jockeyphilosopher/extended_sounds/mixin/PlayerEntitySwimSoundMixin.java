package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import jockeyphilosopher.extended_sounds.ExtendedSoundsLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntitySwimSoundMixin {

    @Unique
    private final Random extended_sounds$random = new Random();

    @Unique
    private boolean extended_sounds$wasUnderwater = false;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void extended_sounds$underwaterAmbient(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        boolean nowUnderwater = self.isInFluid(Material.WATER);

        if (nowUnderwater && !extended_sounds$wasUnderwater) {
            if (jockeyphilosopher.extended_sounds.Config.config.environment.enableWaterEnterExit) {
                float pitch = 0.9F + extended_sounds$random.nextFloat() * 0.2F;
                self.world.playSound(self, "extended_sounds:entering_water", 0.4F, pitch);
            }
            if (jockeyphilosopher.extended_sounds.Config.config.environment.enableUnderwaterAmbient) {
                ExtendedSoundsLoop.startUnderwater();
            }
        } else if (!nowUnderwater && extended_sounds$wasUnderwater) {
            if (jockeyphilosopher.extended_sounds.Config.config.environment.enableWaterEnterExit) {
                float pitch = 0.9F + extended_sounds$random.nextFloat() * 0.2F;
                self.world.playSound(self, "extended_sounds:exiting_water", 0.4F, pitch);
            }
            ExtendedSoundsLoop.stopUnderwater();
        }

        extended_sounds$wasUnderwater = nowUnderwater;
    }
}