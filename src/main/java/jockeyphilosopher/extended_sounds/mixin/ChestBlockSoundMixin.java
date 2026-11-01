package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.ChestSoundTracker;
import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChestBlock.class)
public abstract class ChestBlockSoundMixin {

    @Inject(method = "onUse", at = @At("HEAD"))
    private void extended_sounds$openSound(World world, int x, int y, int z, PlayerEntity player,
                                           CallbackInfoReturnable<Boolean> cir) {
        if (!Config.config.environment.enableChestSounds) return;

        ChestBlock self = (ChestBlock) (Object) this;
        int id = self.id;

        if (world.shouldSuffocate(x, y + 1, z)) return;
        if (world.getBlockId(x - 1, y, z) == id && world.shouldSuffocate(x - 1, y + 1, z)) return;
        if (world.getBlockId(x + 1, y, z) == id && world.shouldSuffocate(x + 1, y + 1, z)) return;
        if (world.getBlockId(x, y, z - 1) == id && world.shouldSuffocate(x, y + 1, z - 1)) return;
        if (world.getBlockId(x, y, z + 1) == id && world.shouldSuffocate(x, y + 1, z + 1)) return;

        float pitch = world.random.nextFloat() * 0.2F + 0.9F;
        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D,
                "extended_sounds:chest_open", 0.4F, pitch);

        ChestSoundTracker.onChestOpened(x, y, z);
    }
}