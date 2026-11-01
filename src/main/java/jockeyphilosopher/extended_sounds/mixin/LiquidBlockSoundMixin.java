package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.block.LiquidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LiquidBlock.class)
public abstract class LiquidBlockSoundMixin {

    @Inject(
            method = "randomDisplayTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Ljava/lang/String;DDDDDD)V")
    )
    private void extended_sounds$lavaPop(World world, int x, int y, int z, Random random, CallbackInfo ci) {
        if (!Config.config.environment.enableLavaSounds) return;

        LiquidBlock self = (LiquidBlock) (Object) this;
        if (self.material != Material.LAVA) return;

        world.playSound((double) x + random.nextFloat(), (double) y + 1.0D, (double) z + random.nextFloat(),
                "extended_sounds:lava_pop",
                0.2F + random.nextFloat() * 0.2F,
                0.9F + random.nextFloat() * 0.15F);
    }

    @Inject(method = "randomDisplayTick", at = @At("TAIL"))
    private void extended_sounds$lavaAmbient(World world, int x, int y, int z, Random random, CallbackInfo ci) {
        if (!Config.config.environment.enableLavaSounds) return;

        LiquidBlock self = (LiquidBlock) (Object) this;
        if (self.material != Material.LAVA) return;
        if (world.getMaterial(x, y + 1, z) != Material.AIR) return;
        if (random.nextInt(200) != 0) return;

        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D,
                "extended_sounds:lava",
                0.2F + random.nextFloat() * 0.2F,
                0.9F + random.nextFloat() * 0.15F);
    }
}