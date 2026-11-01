package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FoodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodItem.class)
public abstract class FoodItemSoundMixin {

    @Unique
    private int extended_sounds$countBefore = 0;

    @Inject(method = "use", at = @At("HEAD"))
    private void extended_sounds$rememberCount(ItemStack stack, World world, PlayerEntity user,
                                               CallbackInfoReturnable<ItemStack> cir) {
        extended_sounds$countBefore = stack.count;
    }

    @Inject(method = "use", at = @At("RETURN"))
    private void extended_sounds$eatSound(ItemStack stack, World world, PlayerEntity user,
                                          CallbackInfoReturnable<ItemStack> cir) {
        if (!Config.config.items.enableEatSound) return;
        if (user == null) return;
        if (stack.count >= extended_sounds$countBefore) return;

        world.playSound(user, "extended_sounds:eat", 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
    }
}