package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.block.Block;
import net.minecraft.block.WoolBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DyeItem.class)
public abstract class DyeItemSoundMixin {

    @Inject(method = "useOnBlock", at = @At("HEAD"))
    private void extended_sounds$boneMealSound(ItemStack stack, PlayerEntity user, World world,
                                               int x, int y, int z, int side,
                                               CallbackInfoReturnable<Boolean> cir) {
        if (!Config.config.items.enableBoneMealSound) return;
        if (stack.getDamage() != 15) return;

        int blockId = world.getBlockId(x, y, z);
        if (blockId != Block.SAPLING.id
                && blockId != Block.WHEAT.id
                && blockId != Block.GRASS_BLOCK.id) {
            return;
        }

        world.playSound((double) x + 0.5D, (double) y + 0.5D, (double) z + 0.5D,
                "extended_sounds:bonemeal", 1.0F, 1.0F);
    }

    @Inject(method = "useOnEntity", at = @At("HEAD"))
    private void extended_sounds$sheepDyeSound(ItemStack stack, LivingEntity entity, CallbackInfo ci) {
        if (!Config.config.items.enableSheepDyeSound) return;
        if (!(entity instanceof SheepEntity)) return;

        SheepEntity sheep = (SheepEntity) entity;
        int newColor = WoolBlock.getBlockMeta(stack.getDamage());

        if (!sheep.isSheared() && sheep.getColor() != newColor) {
            entity.world.playSound(entity, "extended_sounds:dye", 0.7F, 1.0F);
        }
    }
}