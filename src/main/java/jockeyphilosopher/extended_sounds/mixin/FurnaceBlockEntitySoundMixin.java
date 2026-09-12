package jockeyphilosopher.extended_sounds.mixin;

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
        if (!jockeyphilosopher.extended_sounds.Config.config.environment.enableFurnaceCrackle) return;

        FurnaceBlockEntity self = (FurnaceBlockEntity) (Object) this;

        if (self.isBurning()) {
            extended_sounds$crackleTimer--;
            if (extended_sounds$crackleTimer <= 0) {
                extended_sounds$crackleTimer = 30 + extended_sounds$random.nextInt(40);

                float pitch = 0.9F + extended_sounds$random.nextFloat() * 0.2F;

                self.world.playSound(
                        (double) self.x + 0.5D,
                        (double) self.y + 0.5D,
                        (double) self.z + 0.5D,
                        "extended_sounds:furnace_fire_crackle",
                        1.0F,
                        pitch
                );
            }
        } else {
            extended_sounds$crackleTimer = 0;
        }
    }
}