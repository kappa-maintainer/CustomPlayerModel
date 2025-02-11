package com.gpiay.cpm.mixin;

import com.gpiay.cpm.entity.AttachmentProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.util.HandSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingRenderer<AbstractClientPlayerEntity, PlayerModel<AbstractClientPlayerEntity>> {
    public MixinPlayerRenderer(EntityRendererManager p_i50965_1_, PlayerModel<AbstractClientPlayerEntity> p_i50965_2_, float p_i50965_3_) {
        super(p_i50965_1_, p_i50965_2_, p_i50965_3_);
    }

    @Inject(
            at = @At("HEAD"),
            method = "renderRightHand(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V",
            cancellable = true
    )
    public void renderRightHand(MatrixStack matrixStack, IRenderTypeBuffer renderType, int light, AbstractClientPlayerEntity player, CallbackInfo info) {
        AttachmentProvider.getEntityAttachment(player).ifPresent(attachment -> {
            if (attachment.renderFirstPerson(matrixStack, renderType, light, HandSide.RIGHT))
                info.cancel();
        });
    }

    @Inject(
            at = @At("HEAD"),
            method = "renderLeftHand(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/client/renderer/IRenderTypeBuffer;ILnet/minecraft/client/entity/player/AbstractClientPlayerEntity;)V",
            cancellable = true
    )
    public void renderLeftHand(MatrixStack matrixStack, IRenderTypeBuffer renderType, int light, AbstractClientPlayerEntity player, CallbackInfo info) {
        AttachmentProvider.getEntityAttachment(player).ifPresent(attachment -> {
            if (attachment.renderFirstPerson(matrixStack, renderType, light, HandSide.LEFT))
                info.cancel();
        });
    }

    // Fix physics bones disappearing while elytra flying. Parameter a is sometimes larger than 1 resulting a NaN matrix.
    @Redirect(
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;acos(D)D"
            ),
            method = "setupRotations(Lnet/minecraft/client/entity/player/AbstractClientPlayerEntity;Lcom/mojang/blaze3d/matrix/MatrixStack;FFF)V",
            expect = 0
    )
    private double safeAcos(double a) {
        if (a > 1) return 0;
        return Math.acos(a);
    }
}
