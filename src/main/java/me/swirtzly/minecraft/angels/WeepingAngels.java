package me.swirtzly.minecraft.angels;

import me.swirtzly.minecraft.angels.compat.vr.ServerReflector;
import net.minecraft.world.storage.loot.functions.LootFunctionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.swirtzly.minecraft.angels.common.WAObjects;
import me.swirtzly.minecraft.angels.config.WAConfig;
import me.swirtzly.minecraft.angels.data.LangEnglish;
import me.swirtzly.minecraft.angels.data.WABlockTags;
import me.swirtzly.minecraft.angels.data.WAItemTags;
import me.swirtzly.minecraft.angels.network.Network;
import me.swirtzly.minecraft.angels.utils.AngelUtils;
import me.swirtzly.minecraft.angels.utils.ClientUtil;
import me.swirtzly.minecraft.angels.utils.FortuneEnchantBonus;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("weeping_angels")
public class WeepingAngels {
	
	public static final String MODID = "weeping_angels";
	public static final String NAME = "Weeping Angels";
	
	public static Logger LOGGER = LogManager.getLogger(NAME);

	public static final ServerReflector reflector = new ServerReflector();


	public WeepingAngels() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WAConfig.CONFIG_SPEC);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onNewRegistries(RegistryEvent.NewRegistry e) {
		WAObjects.Sounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
		WAObjects.Items.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		WAObjects.Blocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		WAObjects.Blocks.BLOCK_ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		WAObjects.EntityEntries.ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
		WAObjects.Tiles.TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
		WAObjects.WorldGenEntries.FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}
	
	private void setup(final FMLCommonSetupEvent event) {
		Network.init();
		LootFunctionManager.registerFunction(new FortuneEnchantBonus.Serializer());
		reflector.init();

	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientUtil::doClientStuff);
	}

	@SubscribeEvent
	public void gatherData(GatherDataEvent e) {
		e.getGenerator().addProvider(new WAItemTags(e.getGenerator()));
		e.getGenerator().addProvider(new WABlockTags(e.getGenerator()));
		e.getGenerator().addProvider(new LangEnglish(e.getGenerator()));
	}
}
