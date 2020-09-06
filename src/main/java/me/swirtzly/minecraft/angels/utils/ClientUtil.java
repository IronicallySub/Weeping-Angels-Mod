package me.swirtzly.minecraft.angels.utils;

import me.swirtzly.minecraft.angels.client.models.entity.*;
import me.swirtzly.minecraft.angels.client.renders.entities.AngelRender;
import me.swirtzly.minecraft.angels.client.renders.entities.AnomalyRender;
import me.swirtzly.minecraft.angels.client.renders.entities.CGRender;
import me.swirtzly.minecraft.angels.client.renders.tileentities.PlinthTileRender;
import me.swirtzly.minecraft.angels.client.renders.tileentities.SnowArmTileRender;
import me.swirtzly.minecraft.angels.client.renders.tileentities.StatueRender;
import me.swirtzly.minecraft.angels.common.WAObjects;
import me.swirtzly.minecraft.angels.common.entities.WeepingAngelEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientUtil {

	private static final EntityModel<WeepingAngelEntity> modelOne = new ModelAngel<>();
	private static final EntityModel<WeepingAngelEntity> modelTwo = new ModelAngelEd();
	private static final EntityModel<WeepingAngelEntity> modelChild = new ModelAngelChild<WeepingAngelEntity>();
	private static final EntityModel<WeepingAngelEntity> modelClassic = new ModelClassicAngel();
	private static final EntityModel<WeepingAngelEntity> modelMel = new ModelAngelMel<WeepingAngelEntity>();
	private static final EntityModel<WeepingAngelEntity> modelAngela = new ModelAngela<WeepingAngelEntity>();
	private static final EntityModel<WeepingAngelEntity> modelAngela2 = new ModelAngelaAngel();


	public static EntityModel<WeepingAngelEntity> getModelForAngel(int id){
		switch (id) {
			case -1:
				return modelChild;
			case 0:
				return modelOne;
			case 1:
				return modelTwo;
			case 2:
				return modelClassic;
			case 3:
				return modelMel;
			case 4:
				return modelAngela;
			case 5:
				return modelAngela2;
		}
		return null;
	}

	@OnlyIn(Dist.CLIENT)
	public static void playSound(SoundEvent soundIn, float volumeSfx) {
		Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(soundIn, volumeSfx));
	}

	public static void doClientStuff() {
		ClientRegistry.bindTileEntityRenderer(WAObjects.Tiles.ARM.get(), SnowArmTileRender::new);
		ClientRegistry.bindTileEntityRenderer(WAObjects.Tiles.PLINTH.get(), PlinthTileRender::new);
		ClientRegistry.bindTileEntityRenderer(WAObjects.Tiles.STATUE.get(), StatueRender::new);

		RenderingRegistry.registerEntityRenderingHandler(WAObjects.EntityEntries.WEEPING_ANGEL.get(), AngelRender::new);
		RenderingRegistry.registerEntityRenderingHandler(WAObjects.EntityEntries.ANOMALY.get(), AnomalyRender::new);
		RenderingRegistry.registerEntityRenderingHandler(WAObjects.EntityEntries.CHRONODYNE_GENERATOR.get(), (EntityRendererManager entityRendererManager) -> new CGRender(entityRendererManager, Minecraft.getInstance().getItemRenderer()));

		RenderTypeLookup.setRenderLayer(WAObjects.Blocks.ARM.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(WAObjects.Blocks.PLINTH.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(WAObjects.Blocks.STATUE.get(), RenderType.getCutout());
		RenderTypeLookup.setRenderLayer(WAObjects.Blocks.KONTRON_ORE.get(), RenderType.getCutout());
	}

}
