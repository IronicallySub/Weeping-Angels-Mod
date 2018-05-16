package me.sub.angels.client.models.entity;

import me.sub.angels.client.models.poses.PoseBase;
import me.sub.angels.client.models.poses.PoseManager;
import me.sub.angels.common.entities.EntityAngel;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelAngelChild extends ModelBase {
	ModelRenderer head_2;
	ModelRenderer head;
	ModelRenderer body;
	ModelRenderer nose;
	ModelRenderer mouthtop;
	ModelRenderer right_wing_0;
	ModelRenderer right_wing_0_1;
	ModelRenderer left_arm;
	ModelRenderer right_arm;
	ModelRenderer left_arm_1;
	ModelRenderer right_arm_1;
	ModelRenderer right_wing_1;
	ModelRenderer right_wing_2;
	ModelRenderer right_wing_3;
	ModelRenderer right_wing_4;
	ModelRenderer right_wing_1_1;
	ModelRenderer right_wing_2_1;
	ModelRenderer right_wing_3_1;
	ModelRenderer right_wing_4_1;
	
	public ModelAngelChild() {
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.mouthtop = new ModelRenderer(this, 24, 0);
		this.mouthtop.setRotationPoint(0.0F, 0.0F, -4.0F);
		this.mouthtop.addBox(-3.5F, -2.6F, -0.01F, 7, 2, 1, 0.0F);
		this.left_arm = new ModelRenderer(this, 0, 48);
		this.left_arm.setRotationPoint(5.0F, 2.5F, -0.0F);
		this.left_arm.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
		this.setRotateAngle(left_arm, 0.0F, 0.0F, -0.10000736613927509F);
		this.right_wing_3_1 = new ModelRenderer(this, 16, 32);
		this.right_wing_3_1.setRotationPoint(0.0F, 7.0F, 2.0F);
		this.right_wing_3_1.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 2, 0.0F);
		this.setRotateAngle(right_wing_3_1, -1.2292353921796064F, 0.0F, 0.0F);
		this.right_wing_4_1 = new ModelRenderer(this, 16, 32);
		this.right_wing_4_1.setRotationPoint(0.0F, 5.0F, 0.0F);
		this.right_wing_4_1.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 2, 0.0F);
		this.setRotateAngle(right_wing_4_1, -1.1383037381507017F, 0.0F, 0.0F);
		this.right_wing_0_1 = new ModelRenderer(this, 16, 23);
		this.right_wing_0_1.setRotationPoint(-2.4F, 2.0F, 1.5F);
		this.right_wing_0_1.addBox(0.0F, 0.0F, -13.0F, 0, 11, 18, 0.0F);
		this.setRotateAngle(right_wing_0_1, 1.53588974175501F, -0.9424777960769379F, 0.0F);
		this.right_wing_1 = new ModelRenderer(this, 16, 32);
		this.right_wing_1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.right_wing_1.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.right_wing_1_1 = new ModelRenderer(this, 16, 32);
		this.right_wing_1_1.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.right_wing_1_1.addBox(-1.0F, 0.0F, -1.0F, 2, 4, 2, 0.0F);
		this.right_arm = new ModelRenderer(this, 40, 16);
		this.right_arm.setRotationPoint(-5.0F, 2.5F, 0.0F);
		this.right_arm.addBox(-2.0F, -2.0F, -2.0F, 3, 12, 4, 0.0F);
		this.setRotateAngle(right_arm, 0.0F, 0.0F, 0.10000736613927509F);
		this.right_wing_0 = new ModelRenderer(this, 16, 34);
		this.right_wing_0.setRotationPoint(2.4F, 2.0F, 1.5F);
		this.right_wing_0.addBox(0.0F, 0.0F, -13.0F, 0, 11, 18, 0.0F);
		this.setRotateAngle(right_wing_0, 1.53588974175501F, 0.9424777960769379F, 0.0F);
		this.left_arm_1 = new ModelRenderer(this, 0, 32);
		this.left_arm_1.setRotationPoint(2.0F, 12.0F, 0.1F);
		this.left_arm_1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.right_wing_4 = new ModelRenderer(this, 16, 32);
		this.right_wing_4.setRotationPoint(0.0F, 5.0F, 0.0F);
		this.right_wing_4.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 2, 0.0F);
		this.setRotateAngle(right_wing_4, -1.1383037381507017F, 0.0F, 0.0F);
		this.head_2 = new ModelRenderer(this, 32, 0);
		this.head_2.setRotationPoint(0.0F, 7.2F, 0.0F);
		this.head_2.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.5F);
		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0.0F, 7.2F, 0.0F);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, 0.0F);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.setRotationPoint(0.0F, 7.2F, 0.0F);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.nose = new ModelRenderer(this, 0, 0);
		this.nose.setRotationPoint(0.0F, -3.7F, -4.0F);
		this.nose.addBox(-0.5F, -0.3F, 0.0F, 1, 2, 1, 0.0F);
		this.setRotateAngle(nose, -0.40980330836826856F, 0.0F, 0.0F);
		this.right_arm_1 = new ModelRenderer(this, 0, 16);
		this.right_arm_1.setRotationPoint(-2.0F, 12.0F, 0.1F);
		this.right_arm_1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
		this.right_wing_2 = new ModelRenderer(this, 16, 32);
		this.right_wing_2.setRotationPoint(0.0F, 4.0F, -1.0F);
		this.right_wing_2.addBox(-1.0F, 0.0F, 0.0F, 2, 7, 2, 0.0F);
		this.setRotateAngle(right_wing_2, 1.2292353921796064F, 0.0F, 0.0F);
		this.right_wing_3 = new ModelRenderer(this, 16, 32);
		this.right_wing_3.setRotationPoint(0.0F, 7.0F, 2.0F);
		this.right_wing_3.addBox(-1.0F, 0.0F, -2.0F, 2, 5, 2, 0.0F);
		this.setRotateAngle(right_wing_3, -1.2292353921796064F, 0.0F, 0.0F);
		this.right_wing_2_1 = new ModelRenderer(this, 16, 32);
		this.right_wing_2_1.setRotationPoint(0.0F, 4.0F, -1.0F);
		this.right_wing_2_1.addBox(-1.0F, 0.0F, 0.0F, 2, 7, 2, 0.0F);
		this.setRotateAngle(right_wing_2_1, 1.2292353921796064F, 0.0F, 0.0F);
		this.head.addChild(this.mouthtop);
		this.body.addChild(this.left_arm);
		this.right_wing_2_1.addChild(this.right_wing_3_1);
		this.right_wing_3_1.addChild(this.right_wing_4_1);
		this.body.addChild(this.right_wing_0_1);
		this.right_wing_0.addChild(this.right_wing_1);
		this.right_wing_0_1.addChild(this.right_wing_1_1);
		this.body.addChild(this.right_arm);
		this.body.addChild(this.right_wing_0);
		this.body.addChild(this.left_arm_1);
		this.right_wing_3.addChild(this.right_wing_4);
		this.head.addChild(this.nose);
		this.body.addChild(this.right_arm_1);
		this.right_wing_1.addChild(this.right_wing_2);
		this.right_wing_2.addChild(this.right_wing_3);
		this.right_wing_1_1.addChild(this.right_wing_2_1);
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		this.head_2.render(scale);
		this.head.render(scale);
		GlStateManager.pushMatrix();
		GL11.glEnable(GL11.GL_CULL_FACE);
		GlStateManager.translate(this.body.offsetX, this.body.offsetY, this.body.offsetZ);
		GlStateManager.translate(this.body.rotationPointX * scale, this.body.rotationPointY * scale, this.body.rotationPointZ * scale);
		GlStateManager.scale(0.7D, 0.7D, 0.7D);
		GlStateManager.translate(-this.body.offsetX, -this.body.offsetY, -this.body.offsetZ);
		GlStateManager.translate(-this.body.rotationPointX * scale, -this.body.rotationPointY * scale, -this.body.rotationPointZ * scale);
		this.body.render(scale);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GlStateManager.popMatrix();
	}

    /**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netheadYaw, float headPitch, float scaleFactor, Entity entity) {
		if (entity instanceof EntityAngel) {
			System.out.println("sdfsdsdfssd");
			this.head.rotateAngleY = netheadYaw * 0.017453292F;
			this.head.rotateAngleX = headPitch * 0.017453292F;

            this.right_arm.rotationPointY = 2.5F;
			this.left_arm.rotationPointY = 2.5F;
			this.left_arm.rotateAngleX = 0;
			this.left_arm.rotateAngleY = 0;
			this.left_arm.rotateAngleZ = 0;
			this.right_arm.rotateAngleX = 0;
			this.right_arm.rotateAngleY = 0;
			this.right_arm.rotateAngleZ = 0;
			EntityAngel angel;
			PoseBase pose;

            angel = (EntityAngel) entity;

            pose = PoseManager.getPose(angel.getPose());

            if (pose != null) {
				pose.setArmAngles(this.left_arm, this.right_arm, this.left_arm_1, this.right_arm_1);
				pose.setWingAngles(this.right_wing_1, this.right_wing_0);
				pose.setHeadAngles(this.head);
			}
		}
	}
}