package me.swirtzly.minecraft.angels.client.renders.tileentities;

import com.mojang.blaze3d.matrix.MatrixStack;
import me.swirtzly.minecraft.angels.WeepingAngels;
import me.swirtzly.minecraft.angels.client.models.block.SnowArmModel;
import me.swirtzly.minecraft.angels.common.tileentities.SnowArmTile;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class SnowArmTileRender extends TileEntityRenderer<SnowArmTile> {

	private ResourceLocation ARM_TEX = new ResourceLocation(WeepingAngels.MODID, "textures/entities/angel_2.png");
	private SnowArmModel arm = new SnowArmModel();

	public SnowArmTileRender(TileEntityRendererDispatcher renderer) {
		super(renderer);
	}

	@Override
	public void render(SnowArmTile snowArmTile, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {
		matrixStack.push();
		matrixStack.translate(0.5F, -0.7F, 0.5F);
		this.arm.render(matrixStack, iRenderTypeBuffer.getBuffer(RenderType.getEntityCutout(ARM_TEX)), i, i1, 1F, 1F, 1F, 1F);
		matrixStack.pop();
	}
}
