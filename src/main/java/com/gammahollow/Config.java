package com.gammahollow;

import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // The actual boolean value we will check in our code
    public static final ModConfigSpec.BooleanValue ENABLE_RADIATION_FLASHES = BUILDER
            .comment("Whether to show bright light flashes when irradiated. Disable this for photosensitivity/epilepsy safety.")
            .define("enableRadiationFlashes", true);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
