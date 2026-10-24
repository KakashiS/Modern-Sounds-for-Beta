package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PaintingEntity.class)
public abstract class PaintingEntitySoundMixin {

    @Unique
    private boolean extended_sounds$wasDead = false;

    @Inject(method = {"tick", "move", "addVelocity"}, at = @At("HEAD"))
    private void extended_sounds$rememberDead(CallbackInfo ci) {
        extended_sounds$wasDead = ((PaintingEntity) (Object) this).dead;
    }

    @Inject(method = {"tick", "move", "addVelocity"}, at = @At("TAIL"))
    private void extended_sounds$breakSound(CallbackInfo ci) {
        PaintingEntity self = (PaintingEntity) (Object) this;
        if (!extended_sounds$wasDead && self.dead) {
            extended_sounds$playBreak(self);
        }
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void extended_sounds$rememberDeadDamage(CallbackInfoReturnable<Boolean> cir) {
        extended_sounds$wasDead = ((PaintingEntity) (Object) this).dead;
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void extended_sounds$damageSound(CallbackInfoReturnable<Boolean> cir) {
        PaintingEntity self = (PaintingEntity) (Object) this;
        if (extended_sounds$wasDead) return;

        if (self.dead || self.world.isRemote) {
            extended_sounds$playBreak(self);
        }
    }

    @Unique
    private void extended_sounds$playBreak(PaintingEntity self) {
        if (!Config.config.items.enablePaintingSounds) return;
        self.world.playSound(self, "extended_sounds:painting_break", 0.7F, 1.0F);
    }
}