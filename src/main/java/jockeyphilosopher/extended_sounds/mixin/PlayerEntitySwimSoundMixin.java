package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.ExtendedSoundsLoop;
import jockeyphilosopher.extended_sounds.FurnaceSoundLimiter;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
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

    @Unique
    private int extended_sounds$transitionCooldown = 0;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void extended_sounds$underwaterAmbient(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        boolean isLocalPlayer = self instanceof ClientPlayerEntity;

        if (isLocalPlayer) {
            FurnaceSoundLimiter.setListener(self.x, self.y, self.z);

            ExtendedSoundsLoop.stopUnderwaterIfNotOwner(self);
        }

        if (((PlayerEntity) (Object) this).health <= 0) {
            if (isLocalPlayer) {
                ExtendedSoundsLoop.stopUnderwater();
            }
            extended_sounds$wasUnderwater = false;
            return;
        }

        boolean nowUnderwater = self.isInFluid(Material.WATER);

        if (extended_sounds$transitionCooldown > 0) {
            extended_sounds$transitionCooldown--;
        }
        if (nowUnderwater != extended_sounds$wasUnderwater) {
            if (extended_sounds$transitionCooldown > 0) {
                return;
            }
            extended_sounds$transitionCooldown = 8;
        }

        if (nowUnderwater && !extended_sounds$wasUnderwater) {
            if (jockeyphilosopher.extended_sounds.Config.config.environment.enableWaterEnterExit) {
                float pitch = 0.9F + extended_sounds$random.nextFloat() * 0.2F;
                self.world.playSound(self, "extended_sounds:entering_water", 0.4F, pitch);
            }
            if (isLocalPlayer && jockeyphilosopher.extended_sounds.Config.config.environment.enableUnderwaterAmbient) {
                ExtendedSoundsLoop.startUnderwater(self);
            }
        } else if (!nowUnderwater && extended_sounds$wasUnderwater) {
            if (jockeyphilosopher.extended_sounds.Config.config.environment.enableWaterEnterExit) {
                float pitch = 0.9F + extended_sounds$random.nextFloat() * 0.2F;
                self.world.playSound(self, "extended_sounds:exiting_water", 0.4F, pitch);
            }
            if (isLocalPlayer) {
                ExtendedSoundsLoop.stopUnderwater();
            }
        }

        extended_sounds$wasUnderwater = nowUnderwater;
    }
}