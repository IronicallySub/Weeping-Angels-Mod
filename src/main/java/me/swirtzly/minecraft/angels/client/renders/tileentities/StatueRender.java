package me.swirtzly.minecraft.angels.client.renders.tileentities;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.minecraft.angels.client.models.entity.IAngelModel;
import me.swirtzly.minecraft.angels.client.models.entity.ModelAngelaAngel;
import me.swirtzly.minecraft.angels.client.poses.AngelPoses;
import me.swirtzly.minecraft.angels.common.entities.WeepingAngelEntity;
import me.swirtzly.minecraft.angels.common.tileentities.StatueTile;
import me.swirtzly.minecraft.angels.utils.ClientUtil;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Swirtzly on 17/02/2020 @ 12:18
 */
public class StatueRender extends TileEntityRenderer<StatueTile> {
	
	private EntityModel<WeepingAngelEntity> angel = new ModelAngelaAngel();

	public StatueRender(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
		super(tileEntityRendererDispatcher);
	}

	@Override
	public void render(StatueTile statueTile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		matrixStack.push();
		matrixStack.translate(0.5F, 1.5F, 0.5F);
		matrixStack.rotate(Vector3f.ZP.rotationDegrees(180F));
		matrixStack.rotate(Vector3f.YP.rotation(statueTile.getRotation()));

		angel = ClientUtil.getModelForAngel(statueTile.getAngelType());
		ResourceLocation texture = DefaultPlayerSkin.getDefaultSkinLegacy();

		AngelPoses pose = AngelPoses.getPoseFromString(statueTile.getPose());
		if(angel instanceof IAngelModel) {
			IAngelModel angelModel = (IAngelModel) angel;
			angelModel.setAngelPose(pose);
			texture = angelModel.getTextureForPose(pose);
		}
		angel.setRotationAngles(null, 0,0,0,0,0);
		angel.render(matrixStack, bufferIn.getBuffer(RenderType.getEntityCutout(texture)), combinedLightIn, combinedOverlayIn, 1, 1, 1,1);
		matrixStack.pop();
	}
}
