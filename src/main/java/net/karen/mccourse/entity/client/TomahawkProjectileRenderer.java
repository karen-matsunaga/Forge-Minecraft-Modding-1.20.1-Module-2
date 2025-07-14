package net.karen.mccourse.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.entity.custom.TomahawkProjectileEntity;
import net.karen.mccourse.entity.layers.ModModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class TomahawkProjectileRenderer extends EntityRenderer<TomahawkProjectileEntity> {
    private final TomahawkProjectileModel model;

    public TomahawkProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TomahawkProjectileModel(context.bakeLayer(ModModelLayers.TOMAHAWK_LAYER));
    }

    @Override
    public void render(TomahawkProjectileEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        if (!entity.onGround()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.yRotO, entity.getYRot())));
            poseStack.mulPose(Axis.XP.rotationDegrees(entity.getRotation() * 5f));
            poseStack.translate(0, -1.0f, 0);
        }
        else {
            poseStack.mulPose(Axis.YP.rotationDegrees(entity.groundedOffset.y));
            poseStack.mulPose(Axis.XP.rotationDegrees(entity.groundedOffset.x));
            poseStack.translate(0, -1.0f, 0);
        }
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(buffer,
                                        this.model.renderType(this.getTextureLocation(entity)),false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY,
                                  1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull TomahawkProjectileEntity entity) {
        return new ResourceLocation(MCCourseMod.MOD_ID, "textures/entity/tomahawk.png");
    }
}