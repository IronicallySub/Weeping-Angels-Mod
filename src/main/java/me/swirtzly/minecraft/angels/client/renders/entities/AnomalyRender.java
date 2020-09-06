package me.swirtzly.minecraft.angels.client.renders.entities;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.minecraft.angels.WeepingAngels;
import me.swirtzly.minecraft.angels.client.models.entity.PortalModel;
import me.swirtzly.minecraft.angels.common.entities.AnomalyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;

public class AnomalyRender extends MobRenderer<AnomalyEntity, EntityModel<AnomalyEntity>> {

	private static ResourceLocation TEXTURE = new ResourceLocation(WeepingAngels.MODID, "textures/entities/anomaly.png");

	public AnomalyRender(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new PortalModel(), 0);
	}

	@Override
	public void render(AnomalyEntity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
		matrixStackIn.push();
		ActiveRenderInfo activerenderinfo = Minecraft.getInstance().gameRenderer.getActiveRenderInfo();
		matrixStackIn.rotate(activerenderinfo.getRotation());
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
		matrixStackIn.pop();
	}

	/**
	 * Returns the location of an entity's texture.
	 *
	 * @param entity
	 */
	@Override
	public ResourceLocation getEntityTexture(AnomalyEntity entity) {
		return TEXTURE;
	}
}
