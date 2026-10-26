package jockeyphilosopher.extended_sounds.mixin;

import net.minecraft.client.sound.SoundManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import paulscode.sound.SoundSystem;

@Mixin(SoundManager.class)
public interface SoundManagerAccessor {
    @Accessor("soundSystem")
    static SoundSystem extended_sounds$getSoundSystem() {
        throw new UnsupportedOperationException();
    }
}