package me.swirtzly.minecraft.angels.common;

import static me.swirtzly.minecraft.angels.WeepingAngels.MODID;

import java.util.Collection;
import java.util.function.Supplier;

import me.swirtzly.minecraft.angels.WeepingAngels;
import me.swirtzly.minecraft.angels.common.blocks.ChronodyneGeneratorBlock;
import me.swirtzly.minecraft.angels.common.blocks.MineableBlock;
import me.swirtzly.minecraft.angels.common.blocks.PlinthBlock;
import me.swirtzly.minecraft.angels.common.blocks.SnowArmBlock;
import me.swirtzly.minecraft.angels.common.blocks.StatueBlock;
import me.swirtzly.minecraft.angels.common.entities.AngelEnums;
import me.swirtzly.minecraft.angels.common.entities.AnomalyEntity;
import me.swirtzly.minecraft.angels.common.entities.ChronodyneGeneratorEntity;
import me.swirtzly.minecraft.angels.common.entities.WeepingAngelEntity;
import me.swirtzly.minecraft.angels.common.items.AngelSpawnerItem;
import me.swirtzly.minecraft.angels.common.items.ChronodyneGeneratorItem;
import me.swirtzly.minecraft.angels.common.items.DetectorItem;
import me.swirtzly.minecraft.angels.common.misc.WATabs;
import me.swirtzly.minecraft.angels.common.tileentities.PlinthTile;
import me.swirtzly.minecraft.angels.common.tileentities.SnowArmTile;
import me.swirtzly.minecraft.angels.common.tileentities.StatueTile;
import me.swirtzly.minecraft.angels.common.world.ArmGeneration;
import me.swirtzly.minecraft.angels.utils.EntitySpawn;
import me.swirtzly.minecraft.angels.utils.WADamageSource;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WAObjects {
	
	public static DamageSource ANGEL = new WADamageSource("backintime"), STONE = new WADamageSource("punch_stone"), ANGEL_NECK_SNAP = new WADamageSource("neck_snap");
	
	@SubscribeEvent
	public static void addSpawns(FMLLoadCompleteEvent e) {
		EntitySpawn.addSpawnEntries();
	}
	
	private static Item setUpItem(Item item) {
		return item;
	}
	
	private static Block setUpBlock(Block block) {
		return block;
	}
    /**
     * Register block items for specific blocks
     * @param blocks
     */
	private static void genBlockItems(Block... blocks) {
		for (Block block : blocks) {
			Blocks.BLOCK_ITEMS.register(block.getRegistryName().getPath(), () -> setUpItem(new BlockItem(block, new Item.Properties().group(WATabs.MAIN_TAB))));
		}
	}
    /**
     * Register Block Items for all Block entries
     * @param collection
     */
	@SuppressWarnings("unused")
	private static void genBlockItems(Collection<RegistryObject<Block>> collection) {
		for (RegistryObject<Block> block : collection) {
				ItemGroup itemGroup = WATabs.MAIN_TAB;
				Blocks.BLOCK_ITEMS.register(block.get().getRegistryName().getPath(), () -> setUpItem(new BlockItem(block.get(), new Item.Properties().group(itemGroup))));
		}
	}

	private static SoundEvent setUpSound(String soundName) {
		return new SoundEvent(new ResourceLocation(MODID, soundName));
	}

	@SubscribeEvent
	public static void regBlockItems(RegistryEvent.Register<Item> e) {
		genBlockItems(Blocks.ARM.get(), Blocks.KONTRON_ORE.get(), Blocks.PLINTH.get(), Blocks.STATUE.get());
	}
	
	public static class Tiles {
		public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, WeepingAngels.MODID);
		
		public static RegistryObject<TileEntityType<SnowArmTile>> ARM = TILES.register("snow_arm", () -> registerTiles(SnowArmTile::new, Blocks.ARM.get()));
		public static RegistryObject<TileEntityType<PlinthTile>> PLINTH = TILES.register("plinth", () -> registerTiles(PlinthTile::new, Blocks.PLINTH.get()));
		public static RegistryObject<TileEntityType<StatueTile>> STATUE = TILES.register("statue", () -> registerTiles(StatueTile::new, Blocks.STATUE.get()));
	}
	
	public static class Blocks {
		public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WeepingAngels.MODID);
		public static final DeferredRegister<Item> BLOCK_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WeepingAngels.MODID);
		
		public static final RegistryObject<Block> ARM = BLOCKS.register("snow_arm", () -> setUpBlock(new SnowArmBlock()));
		public static final RegistryObject<Block> CG = BLOCKS.register("cg", () -> setUpBlock(new ChronodyneGeneratorBlock()));
		public static final RegistryObject<Block> PLINTH = BLOCKS.register("plinth", () -> setUpBlock(new PlinthBlock()));
		public static final RegistryObject<Block> KONTRON_ORE = BLOCKS.register("kontron_ore", () -> setUpBlock(new MineableBlock(null)));
		public static final RegistryObject<Block> STATUE = BLOCKS.register("statue", () -> setUpBlock(new StatueBlock()));
	}
	
	public static class Items {
		public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, WeepingAngels.MODID);
		
		public static final RegistryObject<Item> TIMEY_WIMEY_DETECTOR = ITEMS.register("timey_wimey_detector", DetectorItem::new);
		public static final RegistryObject<Item> CHRONODYNE_GENERATOR = ITEMS.register("chronodyne_generator", ChronodyneGeneratorItem::new);
		public static final RegistryObject<Item> ANGEL_0 = ITEMS.register("angel_0", () -> setUpItem(new AngelSpawnerItem<>(AngelEnums.AngelType.ANGEL_ONE, WeepingAngelEntity::new)));
		public static final RegistryObject<Item> ANGEL_1 = ITEMS.register("angel_1", () -> setUpItem(new AngelSpawnerItem<>(AngelEnums.AngelType.ANGEL_TWO, WeepingAngelEntity::new)));
		public static final RegistryObject<Item> ANGEL_2 = ITEMS.register("angel_2", () -> setUpItem(new AngelSpawnerItem<>(AngelEnums.AngelType.ANGEL_THREE, WeepingAngelEntity::new)));
		public static final RegistryObject<Item> ANGEL_3 = ITEMS.register("angel_3", () -> setUpItem(new AngelSpawnerItem<>(AngelEnums.AngelType.ANGEL_FOUR, WeepingAngelEntity::new)));
		public static final RegistryObject<Item> ANGEL_4 = ITEMS.register("angel_4", () -> setUpItem(new AngelSpawnerItem<>(AngelEnums.AngelType.ANGEL_FIVE, WeepingAngelEntity::new)));
		public static final RegistryObject<Item> ANGEL_5 = ITEMS.register("angel_5", () -> setUpItem(new AngelSpawnerItem<>(AngelEnums.AngelType.ANGEL_SIX, WeepingAngelEntity::new)));
		public static final RegistryObject<Item> ANGEL_CHILD = ITEMS.register("angel_child", () -> setUpItem(new AngelSpawnerItem<>(AngelEnums.AngelType.ANGEL_CHILD, WeepingAngelEntity::new)));
		public static final RegistryObject<Item> KONTRON_INGOT = ITEMS.register("kontron_ingot", () -> setUpItem(new Item(new Item.Properties().group(WATabs.MAIN_TAB))));
	}
	
	// Sounds
	public static class Sounds {
		public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, WeepingAngels.MODID);
		
		public static final RegistryObject<SoundEvent> ANGEL_SEEN = SOUNDS.register("angel_seen", () -> setUpSound("angel_seen"));
		public static final RegistryObject<SoundEvent> STONE_SCRAP = SOUNDS.register("stone_scrap", () -> setUpSound("stone_scrap"));
		public static final RegistryObject<SoundEvent> CHILD_RUN = SOUNDS.register("child_run", () -> setUpSound("child_run"));
		public static final RegistryObject<SoundEvent> LAUGHING_CHILD = SOUNDS.register("laughing_child", () -> setUpSound("laughing_child"));
		public static final RegistryObject<SoundEvent> LIGHT_BREAK = SOUNDS.register("light_break", () -> setUpSound("light_break"));
		public static final RegistryObject<SoundEvent> ANGEL_AMBIENT = SOUNDS.register("angel_ambient", () -> setUpSound("angel_ambient"));
		public static final RegistryObject<SoundEvent> DING = SOUNDS.register("ding", () -> setUpSound("ding"));
		public static final RegistryObject<SoundEvent> BLOW = SOUNDS.register("blow", () -> setUpSound("blow"));
		public static final RegistryObject<SoundEvent> ANGEL_DEATH = SOUNDS.register("angel_death", () -> setUpSound("angel_death"));
		public static final RegistryObject<SoundEvent> ANGEL_NECK_SNAP = SOUNDS.register("angel_neck_snap", () -> setUpSound("angel_neck_snap"));
		public static final RegistryObject<SoundEvent> PROJECTOR = SOUNDS.register("projector", () -> setUpSound("projector"));
		public static final RegistryObject<SoundEvent> TELEPORT = SOUNDS.register("teleport", () -> setUpSound("teleport"));
	}
	
	// World Gen-Features Creation
	private static <C extends IFeatureConfig, F extends Feature<C>> F registerFeatures(F value) {
		return value;
	}
	
	public static class WorldGenEntries {
		public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, WeepingAngels.MODID);
		public static final RegistryObject<Feature<NoFeatureConfig>> ARM_GEN = FEATURES.register("snow_arm", () -> registerFeatures(new ArmGeneration(NoFeatureConfig::deserialize)));
	}
	
	// Tile Creation
	private static <T extends TileEntity> TileEntityType<T> registerTiles(Supplier<T> tile, Block... validBlock) {
		return TileEntityType.Builder.create(tile, validBlock).build(null);
	}
	
	// Entity Creation
	private static <T extends Entity> EntityType<T> registerBase(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
		ResourceLocation loc = new ResourceLocation(WeepingAngels.MODID, name);
		EntityType.Builder<T> builder = EntityType.Builder.create(factory, classification);
		builder.setShouldReceiveVelocityUpdates(sendUpdate);
		builder.setTrackingRange(trackingRange);
		builder.setUpdateInterval(updateFreq);
		builder.size(width, height);
		builder.setCustomClientFactory((spawnEntity, world) -> client.spawn(world));
		EntityType<T> type = builder.build(loc.toString());
		return type;
	}
	
	// Fire Resistant Entity Creation
	private static <T extends Entity> EntityType<T> registerFireImmuneBase(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate, String name) {
		ResourceLocation loc = new ResourceLocation(WeepingAngels.MODID, name);
		EntityType.Builder<T> builder = EntityType.Builder.create(factory, classification);
		builder.setShouldReceiveVelocityUpdates(sendUpdate);
		builder.setTrackingRange(trackingRange);
		builder.setUpdateInterval(updateFreq);
		builder.immuneToFire();
		builder.size(width, height);
		builder.setCustomClientFactory((spawnEntity, world) -> client.spawn(world));
		EntityType<T> type = builder.build(loc.toString());
		return type;
	}
	
	private static <T extends Entity> EntityType<T> registerFireResistMob(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, String name, boolean velocity) {
		return registerFireImmuneBase(factory, client, classification, width, height, 80, 3, velocity, name);
	}
	
	public static <T extends Entity> EntityType<T> registerStatic(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, String name) {
		return registerBase(factory, client, classification, width, height, 64, 40, false, name);
	}
	
	public static <T extends Entity> EntityType<T> registerMob(EntityType.IFactory<T> factory, IClientSpawner<T> client, EntityClassification classification, float width, float height, String name, boolean velocity) {
		return registerBase(factory, client, classification, width, height, 80, 3, velocity, name);
	}
	
	// Entities
	public static class EntityEntries {
		public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, WeepingAngels.MODID);
		
		public static final RegistryObject<EntityType<WeepingAngelEntity>> WEEPING_ANGEL = ENTITIES.register("weeping_angel", () -> registerFireResistMob(WeepingAngelEntity::new, WeepingAngelEntity::new, EntityClassification.MONSTER, 1F, 1.75F, "weeping_angel", false));
		public static final RegistryObject<EntityType<AnomalyEntity>> ANOMALY = ENTITIES.register("anomaly", () -> registerMob(AnomalyEntity::new, AnomalyEntity::new, EntityClassification.MONSTER, 1F, 1.75F, "anomaly", false));
		public static final RegistryObject<EntityType<ChronodyneGeneratorEntity>> CHRONODYNE_GENERATOR = ENTITIES.register("chronodyne_generator", () -> registerMob(ChronodyneGeneratorEntity::new, ChronodyneGeneratorEntity::new, EntityClassification.MISC, 0.5F, 0.5F, "chronodyne_generator", true));
	}
	
	public interface IClientSpawner<T> {
		T spawn(World world);
	}
	
}
