package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PaintingItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PaintingItem.class)
public abstract class PaintingItemSoundMixin {

    @Unique
    private int extended_sounds$countBefore = 0;

    @Inject(method = "useOnBlock", at = @At("HEAD"))
    private void extended_sounds$rememberCount(ItemStack stack, PlayerEntity user, World world,
                                               int x, int y, int z, int side,
                                               CallbackInfoReturnable<Boolean> cir) {
        extended_sounds$countBefore = stack.count;
    }

    @Inject(method = "useOnBlock", at = @At("RETURN"))
    private void extended_sounds$placeSound(ItemStack stack, PlayerEntity user, World world,
                                            int x, int y, int z, int side,
                                            CallbackInfoReturnable<Boolean> cir) {
        if (!Config.config.items.enablePaintingSounds) return;

        if (stack.count < extended_sounds$countBefore) {
            world.playSound(
                    (double) x + 0.5D,
                    (double) y + 0.5D,
                    (double) z + 0.5D,
                    "extended_sounds:painting_place",
                    0.7F,
                    1.0F
            );
        }
    }
}