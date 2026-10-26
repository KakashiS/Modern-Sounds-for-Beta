package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public abstract class FishingRodItemSoundMixin {

    @ModifyArg(
            method = "use",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V"),
            index = 1
    )
    private String extended_sounds$castSound(String original) {
        return Config.config.items.enableFishingRodSounds
                ? "extended_sounds:fishing_rod_cast"
                : original;
    }

    @Inject(method = "use", at = @At("HEAD"))
    private void extended_sounds$retrieveSound(ItemStack stack, World world, PlayerEntity user,
                                               CallbackInfoReturnable<ItemStack> cir) {
        if (!Config.config.items.enableFishingRodSounds) return;
        if (user == null || user.fishHook == null) return;

        float pitch = 1.0F / (world.random.nextFloat() * 0.4F + 0.8F);
        world.playSound(user, "extended_sounds:bobber_retrieve", 1.0F, pitch);
    }
}