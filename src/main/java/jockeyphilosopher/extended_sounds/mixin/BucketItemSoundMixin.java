package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResultType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public abstract class BucketItemSoundMixin {

    @Shadow
    private int fluidBlockId;

    @Unique
    private int extended_sounds$action = 0;
    @Unique
    private int extended_sounds$x;
    @Unique
    private int extended_sounds$y;
    @Unique
    private int extended_sounds$z;
    @Unique
    private int extended_sounds$priorId;

    // Перед использованием: определяем, какой блок будет затронут
    @Inject(method = "use", at = @At("HEAD"))
    private void extended_sounds$beforeUse(ItemStack stack, World world, PlayerEntity user,
                                           CallbackInfoReturnable<ItemStack> cir) {
        extended_sounds$action = 0;
        if (!Config.config.items.enableBucketSounds) return;
        if (user == null || this.fluidBlockId < 0) return; // ведро молока пропускаем

        float yaw = user.yaw;
        float pitch = user.pitch;
        double px = user.x;
        double py = user.y + 1.62D - (double) user.standingEyeHeight;
        double pz = user.z;
        Vec3d from = Vec3d.createCached(px, py, pz);
        float cosYaw = MathHelper.cos(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float sinYaw = MathHelper.sin(-yaw * ((float) Math.PI / 180F) - (float) Math.PI);
        float cosPitch = -MathHelper.cos(-pitch * ((float) Math.PI / 180F));
        float sinPitch = MathHelper.sin(-pitch * ((float) Math.PI / 180F));
        float dirX = sinYaw * cosPitch;
        float dirZ = cosYaw * cosPitch;
        Vec3d to = from.add((double) dirX * 5.0D, (double) sinPitch * 5.0D, (double) dirZ * 5.0D);

        HitResult hit = world.raycast(from, to, this.fluidBlockId == 0);
        if (hit == null || hit.type != HitResultType.BLOCK) return;

        int x = hit.blockX;
        int y = hit.blockY;
        int z = hit.blockZ;
        if (!world.canInteract(user, x, y, z)) return;

        if (this.fluidBlockId == 0) {

            if (world.getBlockMeta(x, y, z) != 0) return;
            Material material = world.getMaterial(x, y, z);
            if (material == Material.WATER) {
                extended_sounds$action = 1;
            } else if (material == Material.LAVA) {
                extended_sounds$action = 2;
            } else {
                return;
            }
        } else {
            int side = hit.side;
            if (side == 0) --y;
            if (side == 1) ++y;
            if (side == 2) --z;
            if (side == 3) ++z;
            if (side == 4) --x;
            if (side == 5) ++x;

            if (!(world.isAir(x, y, z) || !world.getMaterial(x, y, z).isSolid())) return;

            boolean water = this.fluidBlockId == 8 || this.fluidBlockId == 9;
            boolean lava = this.fluidBlockId == 10 || this.fluidBlockId == 11;

            if (water && world.dimension.evaporatesWater) return; // в Незере вода испаряется
            if (water) {
                extended_sounds$action = 3;
            } else if (lava) {
                extended_sounds$action = 4;
            } else {
                return;
            }
            extended_sounds$priorId = world.getBlockId(x, y, z);
        }

        extended_sounds$x = x;
        extended_sounds$y = y;
        extended_sounds$z = z;
    }

    @Inject(method = "use", at = @At("RETURN"))
    private void extended_sounds$afterUse(ItemStack stack, World world, PlayerEntity user,
                                          CallbackInfoReturnable<ItemStack> cir) {
        int action = extended_sounds$action;
        extended_sounds$action = 0;
        if (action == 0 || user == null) return;

        int now = world.getBlockId(extended_sounds$x, extended_sounds$y, extended_sounds$z);
        String sound = null;

        switch (action) {
            case 1:
                if (now == 0) sound = "extended_sounds:fill_water_bucket";
                break;
            case 2:
                if (now == 0) sound = "extended_sounds:fill_lava_bucket";
                break;
            case 3:
                if (now != extended_sounds$priorId && (now == 8 || now == 9)) {
                    sound = "extended_sounds:empty_water_bucket";
                }
                break;
            case 4:
                if (now != extended_sounds$priorId && (now == 10 || now == 11)) {
                    sound = "extended_sounds:empty_lava_bucket";
                }
                break;
            default:
                break;
        }

        if (sound != null) {
            world.playSound(user, sound, 1.0F, 1.0F);
        }
    }
}