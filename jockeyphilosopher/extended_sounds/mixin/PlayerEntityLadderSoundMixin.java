package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityLadderSoundMixin {

    @Unique
    private final Random extended_sounds$random = new Random();

    @Unique
    private int extended_sounds$ladderTimer = 7;

    @Unique
    private double extended_sounds$lastY = Double.NaN;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void extended_sounds$ladderSound(CallbackInfo ci) {
        if (!jockeyphilosopher.extended_sounds.Config.config.movement.enableLadderSound) {
            extended_sounds$ladderTimer = 0;
            return;
        }

        Entity self = (Entity) (Object) this;

        double dy;
        if (Double.isNaN(extended_sounds$lastY)) {
            dy = 0;
        } else {
            dy = self.y - extended_sounds$lastY;
        }
        extended_sounds$lastY = self.y;

        boolean climbing = isOnLadder(self) && Math.abs(dy) > 0.015;

        if (climbing) {
            extended_sounds$ladderTimer--;
            if (extended_sounds$ladderTimer <= 0) {
                extended_sounds$ladderTimer = 8;
                float pitch = 0.95F + extended_sounds$random.nextFloat() * 0.1F;
                self.world.playSound(self, "extended_sounds:ladder_hit", 0.189F, pitch);
            }
        } else {
            extended_sounds$ladderTimer = 0;
        }
    }

    @Unique
    private boolean isOnLadder(Entity entity) {
        int x = MathHelper.floor(entity.x);
        int y = MathHelper.floor(entity.boundingBox.minY);
        int z = MathHelper.floor(entity.z);
        int blockId = entity.world.getBlockId(x, y, z);
        return blockId == Block.LADDER.id;
    }
}