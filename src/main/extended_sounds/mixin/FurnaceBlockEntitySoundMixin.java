package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import jockeyphilosopher.extended_sounds.FurnaceSoundLimiter;
import net.minecraft.block.entity.FurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(FurnaceBlockEntity.class)
public abstract class FurnaceBlockEntitySoundMixin {

    @Unique
    private final Random extended_sounds$random = new Random();

    @Unique
    private int extended_sounds$crackleTimer = 0;

    @Inject(method = "tick", at = @At("TAIL"))
    private void extended_sounds$crackleSound(CallbackInfo ci) {
        if (!Config.config.environment.enableFurnaceCrackle) return;

        FurnaceBlockEntity self = (FurnaceBlockEntity) (Object) this;

        if (self.isBurning()) {
            extended_sounds$crackleTimer--;
            if (extended_sounds$crackleTimer <= 0) {
                double fx = (double) self.x + 0.5D;
                double fy = (double) self.y + 0.5D;
                double fz = (double) self.z + 0.5D;

                if (!FurnaceSoundLimiter.isAudible(fx, fy, fz)) {
                    extended_sounds$crackleTimer = 20 + extended_sounds$random.nextInt(20);
                } else if (FurnaceSoundLimiter.tryAcquire()) {
                    extended_sounds$crackleTimer = 60 + extended_sounds$random.nextInt(80);

                    float pitch = 0.8F + extended_sounds$random.nextFloat() * 0.2F;

                    self.world.playSound(fx, fy, fz, "extended_sounds:furnace_fire_crackle", 1.0F, pitch);
                } else {
                    extended_sounds$crackleTimer = 10 + extended_sounds$random.nextInt(10);
                }
            }
        } else {
            extended_sounds$crackleTimer = 0;
        }
    }
}