package jockeyphilosopher.extended_sounds;

import net.glasslauncher.mods.gcapi3.api.*;

public class Config {

    @ConfigRoot(value = "config", visibleName = "Extended Sounds")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigCategory(name = "Environment Sounds")
        public EnvironmentSounds environment = new EnvironmentSounds();

        @ConfigCategory(name = "Entity Sounds")
        public EntitySounds entity = new EntitySounds();

        @ConfigCategory(name = "Movement Sounds")
        public MovementSounds movement = new MovementSounds();

        public static class EnvironmentSounds {
            @ConfigEntry(name = "Enable Furnace Crackle Sound", description = "Furnace Crackle Sound")
            public Boolean enableFurnaceCrackle = true;

            @ConfigEntry(name = "Enable Water Enter/Exit Sound", description = "Water Enter/Exit Sound")
            public Boolean enableWaterEnterExit = true;

            @ConfigEntry(name = "Enable Underwater Ambient Sound", description = "Underwater Ambient Sound")
            public Boolean enableUnderwaterAmbient = true;
        }

        public static class EntitySounds {
            @ConfigEntry(name = "Enable Minecart Rolling Sound", description = "Minecart Rolling Sound")
            public Boolean enableMinecartRolling = true;

            @ConfigEntry(name = "Enable Boat Water Sound", description = "Boat Water Sound")
            public Boolean enableBoatWater = true;

            @ConfigEntry(name = "Enable Squid Sounds", description = "Squid Sounds")
            public Boolean enableSquidSounds = true;
        }

        public static class MovementSounds {
            @ConfigEntry(name = "Enable Ladder Climbing Sound", description = "Ladder Climbing Sound")
            public Boolean enableLadderSound = true;

            @ConfigEntry(name = "Enable Fall Wind Sound", description = "Fall Wind Sound")
            public Boolean enableFallWindSound = true;
        }
    }
}