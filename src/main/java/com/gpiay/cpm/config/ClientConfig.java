package com.gpiay.cpm.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {
    public static ForgeConfigSpec CONFIG;

    public static ForgeConfigSpec.BooleanValue HIDE_NEAR_PARTICLES;
    public static ForgeConfigSpec.BooleanValue HIDE_ARMORS;
    public static ForgeConfigSpec.BooleanValue SEND_MODELS;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("Client Config").push("client");

        HIDE_NEAR_PARTICLES = builder
                .translation("text.autoconfig.cpm.option.client.hideNearParticles")
                .comment("Hide particles that are too close to player's view point.")
                .define("hideNearParticles", true);

        HIDE_ARMORS = builder
                .translation("text.autoconfig.cpm.option.client.hideArmors")
                .comment("Hide armors of custom models.")
                .define("hideArmors", false);

        SEND_MODELS = builder
                .translation("text.autoconfig.cpm.option.client.sendModels")
                .comment("Send models that are not located at the server from client.")
                .define("sendModels", true);

        builder.pop();
        CONFIG = builder.build();
    }
}
