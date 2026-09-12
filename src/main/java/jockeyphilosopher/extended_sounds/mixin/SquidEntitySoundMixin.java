package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.entity.passive.SquidEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SquidEntity.class)
public abstract class SquidEntitySoundMixin {

    @Inject(method = "getRandomSound", at = @At("HEAD"), cancellable = true)
    private void extended_sounds$idleSound(CallbackInfoReturnable<String> cir) {
        if (jockeyphilosopher.extended_sounds.Config.config.entity.enableSquidSounds) {
            cir.setReturnValue("extended_sounds:squid_idle");
        }
    }

    @Inject(method = "getHurtSound", at = @At("HEAD"), cancellable = true)
    private void extended_sounds$hurtSound(CallbackInfoReturnable<String> cir) {
        if (jockeyphilosopher.extended_sounds.Config.config.entity.enableSquidSounds) {
            cir.setReturnValue("extended_sounds:squid_hit");
        }
    }

    @Inject(method = "getDeathSound", at = @At("HEAD"), cancellable = true)
    private void extended_sounds$deathSound(CallbackInfoReturnable<String> cir) {
        if (jockeyphilosopher.extended_sounds.Config.config.entity.enableSquidSounds) {
            cir.setReturnValue("extended_sounds:squid_death");
        }
    }
}