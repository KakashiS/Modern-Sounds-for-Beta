package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.ExtendedSoundsLoop;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityFallSoundMixin {

    @Unique
    private static final double START_THRESHOLD = -0.65D;
    @Unique
    private static final double STOP_THRESHOLD = -0.4D;

    @Unique
    private boolean extended_sounds$falling = false;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void extended_sounds$fallWindSound(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;

        if (!(self instanceof ClientPlayerEntity)) return;

        ExtendedSoundsLoop.stopFallWindIfNotOwner(self);

        if (((PlayerEntity) (Object) this).health <= 0
                || !jockeyphilosopher.extended_sounds.Config.config.movement.enableFallWindSound) {
            if (extended_sounds$falling) {
                ExtendedSoundsLoop.stopFallWind();
                extended_sounds$falling = false;
            }
            return;
        }

        double threshold = extended_sounds$falling ? STOP_THRESHOLD : START_THRESHOLD;

        boolean freeFalling = self.vehicle == null
                && !self.onGround
                && !self.isInFluid(Material.WATER)
                && self.velocityY < threshold;

        if (freeFalling && !extended_sounds$falling) {
            ExtendedSoundsLoop.startFallWind(self);
            extended_sounds$falling = true;
        } else if (!freeFalling && extended_sounds$falling) {
            ExtendedSoundsLoop.stopFallWind();
            extended_sounds$falling = false;
        }

        ExtendedSoundsLoop.tickFallWindFade();
    }
}