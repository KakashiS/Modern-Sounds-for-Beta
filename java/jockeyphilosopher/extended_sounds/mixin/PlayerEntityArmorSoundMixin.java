package jockeyphilosopher.extended_sounds.mixin;

import jockeyphilosopher.extended_sounds.Config;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityArmorSoundMixin {

    @Unique
    private final int[] extended_sounds$prevArmor = new int[]{-1, -1, -1, -1};

    @Unique
    private int extended_sounds$graceTicks = 40;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void extended_sounds$armorSound(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;

        if (!(self instanceof ClientPlayerEntity)) return;

        ItemStack[] armor = self.inventory.armor;

        boolean grace = extended_sounds$graceTicks > 0;
        if (grace) {
            extended_sounds$graceTicks--;
        }

        String sound = null;
        for (int i = 0; i < 4; i++) {
            ItemStack stack = armor[i];
            int id = stack == null ? -1 : stack.itemId;

            if (!grace && sound == null && id != -1 && id != extended_sounds$prevArmor[i]) {
                sound = extended_sounds$soundFor(stack);
            }
            extended_sounds$prevArmor[i] = id;
        }

        if (sound != null && Config.config.items.enableArmorEquipSounds) {
            self.world.playSound(self, sound, 0.5F, 1.0F);
        }
    }

    @Unique
    private String extended_sounds$soundFor(ItemStack stack) {
        if (stack.getItem() instanceof ArmorItem) {
            int type = ((ArmorItem) stack.getItem()).type;
            if (type == 0) return "extended_sounds:equip_generic";
            if (type == 3) return "extended_sounds:equip_diamond";
            return "extended_sounds:equip_iron";
        }
        return "extended_sounds:equip_generic";
    }
}