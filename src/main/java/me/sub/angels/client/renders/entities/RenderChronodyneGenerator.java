package me.sub.angels.client.renders.entities;

import me.sub.angels.client.models.entity.ModelCG;
import me.sub.angels.common.WAObjects;
import me.sub.angels.common.entities.EntityChronodyneGenerator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;

public class RenderChronodyneGenerator extends RenderSnowball<EntityChronodyneGenerator> {
	
	private ModelCG model = new ModelCG();
	
	public RenderChronodyneGenerator(RenderManager manager) {
		super(manager, WAObjects.Items.CHRONODYNE_GENERATOR, null);
	}
	
	@Override
	public void doRender(EntityChronodyneGenerator entity, double x, double y, double z, float par8, float par9) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y - 1.3, z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.disableFog();
		GlStateManager.disableLighting();
		model.render(entity, 0, 0, 0, 0, 0, 0.0625F);
		GlStateManager.enableFog();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
	
}
