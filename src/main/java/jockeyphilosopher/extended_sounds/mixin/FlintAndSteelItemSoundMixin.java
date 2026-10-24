package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.item.FlintAndSteelItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(FlintAndSteelItem.class)
public abstract class FlintAndSteelItemSoundMixin {

    @ModifyArg(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLjava/lang/String;FF)V"),
            index = 3
    )
    private String extended_sounds$flintSoundName(String original) {
        return Config.config.items.enableFlintAndSteelSound
                ? "extended_sounds:flint_and_steel_click"
                : original;
    }

    @ModifyArg(
            method = "useOnBlock",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(DDDLjava/lang/String;FF)V"),
            index = 4
    )
    private float extended_sounds$flintSoundVolume(float original) {
        return Config.config.items.enableFlintAndSteelSound ? 0.5F : original;
    }
}