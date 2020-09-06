package me.swirtzly.minecraft.angels.client.models.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import me.swirtzly.minecraft.angels.WeepingAngels;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class SnowArmModel extends EntityModel {

	private final ModelRenderer left_arm;
	private final ModelRenderer left_arm_wrist;

	public SnowArmModel() {
		textureWidth = 88;
		textureHeight = 88;

		left_arm = new ModelRenderer(this);
		left_arm.setRotationPoint(-1.2F, 22.1F, -0.1F);
		setRotationAngle(left_arm, 0.0F, 0.4554F, 2.9596F);
		left_arm.setTextureOffset(32, 27).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);

		left_arm_wrist = new ModelRenderer(this);
		left_arm_wrist.setRotationPoint(0.0F, 4.0F, 2.0F);
		left_arm.addChild(left_arm_wrist);
		setRotationAngle(left_arm_wrist, -0.5236F, 0.0F, 0.0F);
		left_arm_wrist.setTextureOffset(34, 52).addBox(-1.0F, 0.0F, -4.0F, 3.0F, 6.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		matrixStack.push();
		left_arm.render(matrixStack, buffer, packedLight, packedOverlay);
		matrixStack.pop();
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}


}
