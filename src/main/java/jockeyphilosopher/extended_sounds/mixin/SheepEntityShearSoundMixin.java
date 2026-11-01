package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class SheepEntityShearSoundMixin {

    @Inject(method = "interact", at = @At("HEAD"))
    private void extended_sounds$shearSound(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!Config.config.items.enableShearSound) return;

        ItemStack held = player.inventory.getSelectedItem();
        SheepEntity self = (SheepEntity) (Object) this;

        if (held != null && held.itemId == Item.SHEARS.id && !self.isSheared()) {
            self.world.playSound(self, "extended_sounds:shear", 1.0F, 1.0F);
        }
    }
}