package net.karen.mccourse.entity.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.item.ModItems;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ElytraOverpowerLayer<T extends LivingEntity, M extends EntityModel<T>> extends ElytraLayer<T, M> {
    private static final ResourceLocation DIAMOND_ELYTRA_TEXTURE =
            new ResourceLocation(MCCourseMod.MOD_ID, "textures/entity/diamond_elytra.png");
    private final ElytraModel<T> elytraOverpowerModel;

    public ElytraOverpowerLayer(RenderLayerParent<T, M> parent, EntityModelSet models) {
        super(parent, models);
        this.elytraOverpowerModel = new ElytraModel<>(models.bakeLayer(ModModelLayers.DIAMOND_ELYTRA_LAYER));
    }

    @Override
    public @NotNull ResourceLocation getElytraTexture(ItemStack stack, @NotNull T entity) {
        if (stack.getItem() == ModItems.DIAMOND_ELYTRA.get()) { return DIAMOND_ELYTRA_TEXTURE; }
        return super.getElytraTexture(stack, entity); // Fallback
    }

    @Override
    public void render(@NotNull PoseStack pose, @NotNull MultiBufferSource buffer,
                       int packedLight, T entity, float limbSwing, float limbSwingAmount,
                       float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack elytra = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (shouldRender(elytra, entity)) {
            ResourceLocation resourcelocation;
            if (entity instanceof AbstractClientPlayer abstractclientplayer) {
                if (abstractclientplayer.isElytraLoaded() && abstractclientplayer.getElytraTextureLocation() != null) {
                    resourcelocation = abstractclientplayer.getElytraTextureLocation();
                }
                else if (abstractclientplayer.isCapeLoaded() && abstractclientplayer.getCloakTextureLocation() != null &&
                         abstractclientplayer.isModelPartShown(PlayerModelPart.CAPE)) {
                    resourcelocation = abstractclientplayer.getCloakTextureLocation();
                }
                else { resourcelocation = getElytraTexture(elytra, entity); }
            }
            else { resourcelocation = getElytraTexture(elytra, entity); }
            pose.pushPose();
            pose.translate(0.0F, 0.0F, 0.125F);
            this.getParentModel().copyPropertiesTo(this.elytraOverpowerModel);
            this.elytraOverpowerModel.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(buffer, RenderType.armorCutoutNoCull(resourcelocation),
                                                                            false, elytra.hasFoil());
            this.elytraOverpowerModel.renderToBuffer(pose, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                                                     1F, 1F, 1F, 1F);
            pose.popPose();
        }
    }

    @Override
    public boolean shouldRender(ItemStack stack, @NotNull T entity) { return stack.getItem() == ModItems.DIAMOND_ELYTRA.get(); }
}