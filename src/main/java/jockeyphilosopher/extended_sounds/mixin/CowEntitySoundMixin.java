package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CowEntity.class)
public abstract class CowEntitySoundMixin {

    @Inject(method = "interact", at = @At("RETURN"))
    private void extended_sounds$milkSound(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.config.items.enableCowMilkSound) return;

        if (Boolean.TRUE.equals(cir.getReturnValue())) {
            CowEntity self = (CowEntity) (Object) this;
            self.world.playSound(self, "extended_sounds:cow_milk", 0.7F, 1.0F);
        }
    }
}