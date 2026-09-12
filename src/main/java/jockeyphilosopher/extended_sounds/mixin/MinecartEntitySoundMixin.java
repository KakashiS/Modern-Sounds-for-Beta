package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.block.RailBlock;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.util.math.MathHelper;
import jockeyphilosopher.extended_sounds.ExtendedSoundsLoop;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartEntity.class)
public abstract class MinecartEntitySoundMixin {

    @Unique
    private boolean extended_sounds$wasMoving = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void extended_sounds$rollSound(CallbackInfo ci) {
        MinecartEntity self = (MinecartEntity) (Object) this;

        boolean audibleToLocalPlayer = self.passenger == null || self.passenger instanceof ClientPlayerEntity;

        int ix = MathHelper.floor(self.x);
        int iy = MathHelper.floor(self.y);
        int iz = MathHelper.floor(self.z);
        if (RailBlock.isRail(self.world, ix, iy - 1, iz)) {
            iy--;
        }
        boolean onRail = RailBlock.isRail(self.world, ix, iy, iz);

        boolean moving = jockeyphilosopher.extended_sounds.Config.config.entity.enableMinecartRolling
                && audibleToLocalPlayer
                && onRail
                && (self.velocityX * self.velocityX + self.velocityZ * self.velocityZ) > 0.0015;

        if (moving) {
            if (!extended_sounds$wasMoving) {
                ExtendedSoundsLoop.startMinecart((float) self.x, (float) self.y, (float) self.z);
            } else {
                ExtendedSoundsLoop.updateMinecartPosition((float) self.x, (float) self.y, (float) self.z);
            }
        } else {
            if (extended_sounds$wasMoving) {
                ExtendedSoundsLoop.stopMinecart();
            }
        }

        extended_sounds$wasMoving = moving;
    }

    @Inject(method = "markDead", at = @At("HEAD"))
    private void extended_sounds$stopSoundOnDestroy(CallbackInfo ci) {
        if (extended_sounds$wasMoving) {
            ExtendedSoundsLoop.stopMinecart();
            extended_sounds$wasMoving = false;
        }
    }
}