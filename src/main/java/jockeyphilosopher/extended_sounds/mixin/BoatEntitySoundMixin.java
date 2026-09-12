package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(BoatEntity.class)
public abstract class BoatEntitySoundMixin {

    @Unique
    private final Random extended_sounds$random = new Random();

    @Unique
    private int extended_sounds$waterTimer = 16;

    @Inject(method = "tick", at = @At("TAIL"))
    private void extended_sounds$waterSound(CallbackInfo ci) {
        if (!jockeyphilosopher.extended_sounds.Config.config.entity.enableBoatWater) {
            extended_sounds$waterTimer = 0;
            return;
        }

        BoatEntity self = (BoatEntity) (Object) this;

        boolean audibleToLocalPlayer = self.passenger == null || self.passenger instanceof ClientPlayerEntity;

        boolean moving = audibleToLocalPlayer
                && (self.velocityX * self.velocityX + self.velocityZ * self.velocityZ) > 0.0015;

        if (moving) {
            extended_sounds$waterTimer--;
            if (extended_sounds$waterTimer <= 0) {
                extended_sounds$waterTimer = 13 + extended_sounds$random.nextInt(3);
                float pitch = 0.95F + extended_sounds$random.nextFloat() * 0.1F;
                self.world.playSound(self, "extended_sounds:boat_water", 0.35F, pitch);
            }
        } else {
            extended_sounds$waterTimer = 0;
        }
    }
}