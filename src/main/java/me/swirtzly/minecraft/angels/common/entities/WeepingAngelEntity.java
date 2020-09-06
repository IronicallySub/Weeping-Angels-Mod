package me.swirtzly.minecraft.angels.common.entities;

import me.swirtzly.minecraft.angels.WeepingAngels;
import me.swirtzly.minecraft.angels.client.poses.AngelPoses;
import me.swirtzly.minecraft.angels.common.WAObjects;
import me.swirtzly.minecraft.angels.common.misc.WAConstants;
import me.swirtzly.minecraft.angels.config.WAConfig;
import me.swirtzly.minecraft.angels.utils.AngelUtils;
import me.swirtzly.minecraft.angels.utils.WATeleporter;
import net.minecraft.block.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.BreakDoorGoal;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPlaySoundEffectPacket;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.*;
import net.minecraft.util.concurrent.TickDelayedTask;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.function.Predicate;

import static me.swirtzly.minecraft.angels.utils.WATeleporter.yCoordSanity;

public class WeepingAngelEntity extends QuantumLockBaseEntity {

	private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(WeepingAngelEntity.class, DataSerializers.VARINT);
	private static final DataParameter<Boolean> IS_CHERUB = EntityDataManager.createKey(WeepingAngelEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<String> CURRENT_POSE = EntityDataManager.createKey(WeepingAngelEntity.class, DataSerializers.STRING);
	private static final DataParameter<Integer> HUNGER_LEVEL = EntityDataManager.createKey(WeepingAngelEntity.class, DataSerializers.VARINT);
	public long timeSincePlayedSound = 0;

	private final SoundEvent[] CHILD_SOUNDS = new SoundEvent[]{SoundEvents.ENTITY_VEX_AMBIENT, WAObjects.Sounds.LAUGHING_CHILD.get()};

	private static final Predicate<Difficulty> DIFFICULTY = (p_213697_0_) -> p_213697_0_ == Difficulty.EASY;

	public WeepingAngelEntity(EntityType<? extends QuantumLockBaseEntity> type, World world) {
		this(world);
	}

	public WeepingAngelEntity(World world) {
		super(world, WAObjects.EntityEntries.WEEPING_ANGEL.get());
		goalSelector.addGoal(0, new BreakDoorGoal(this, DIFFICULTY));
		goalSelector.addGoal(5, new MoveTowardsRestrictionGoal(this, 1.0D));
		goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
		goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 50.0F));
		experienceValue = WAConfig.CONFIG.xpGained.get();
	}
	
	public void dropAngelStuff() {
		AngelUtils.dropEntityLoot(this, this.attackingPlayer);
		entityDropItem(getHeldItemMainhand());
		entityDropItem(getHeldItemOffhand());
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		getDataManager().register(IS_CHERUB, rand.nextInt(10) == 4);
		getDataManager().register(TYPE, AngelUtils.randomType().getId());
		getDataManager().register(CURRENT_POSE, AngelPoses.getRandomPose().getRegistryName().toString());
		getDataManager().register(HUNGER_LEVEL, 50);
	}

	@Nullable
	@Override
	public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
		playSound(WAObjects.Sounds.ANGEL_AMBIENT.get(), 0.5F, 1.0F);
		return super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
	}

	@Override
	protected void registerAttributes() {
		super.registerAttributes();
		this.getAttributes().getAttributeInstance(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(WAConfig.CONFIG.damage.get());
		this.getAttributes().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D);
		this.getAttributes().getAttributeInstance(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(9999999.0D);
		this.getAttributes().getAttributeInstance(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(WAConfig.CONFIG.moveSpeed.get());
		this.getAttributes().getAttributeInstance(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.BLOCK_STONE_HIT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return WAObjects.Sounds.ANGEL_DEATH.get();
	}

	@Override
	protected SoundEvent getAmbientSound() {
		if (isCherub() && ticksExisted % AngelUtils.secondsToTicks(2) == 0) {
			return CHILD_SOUNDS[rand.nextInt(CHILD_SOUNDS.length)];
		}
		return null;
	}

	@Override
	public float getEyeHeight(Pose p_213307_1_) {
		return isCherub() ? getHeight() : 1.3F;
	}


	@Override
	public boolean attackEntityAsMob(Entity entity) {

		if (entity instanceof ServerPlayerEntity) {

			ServerPlayerEntity playerMP = (ServerPlayerEntity) entity;
			
			// Blowing out light items from the players hand
			if (WAConfig.CONFIG.torchBlowOut.get() && isCherub()) {
				AngelUtils.removeLightFromHand(playerMP, this);
			}
			
			// Teleporting and damage
			if (WAConfig.CONFIG.justTeleport.get()) {
				if (!isCherub()) {
					teleportInteraction(playerMP);
					return false;
				} else {
					dealDamage(playerMP);
					return true;
				}
			} else {
				boolean shouldTeleport = rand.nextInt(10) < 5 && !isWeak();
				if (shouldTeleport) {
					teleportInteraction(playerMP);
					return false;
				} else {
					dealDamage(playerMP);
					return true;
				}
			}
			
		}
		return true;
	}
	
	public void dealDamage(PlayerEntity playerMP) {
		if (getHealth() > 5) {
			playerMP.attackEntityFrom(WAObjects.ANGEL, 4.0F);
			heal(4.0F);
		} else {
			playerMP.attackEntityFrom(WAObjects.ANGEL_NECK_SNAP, 4.0F);
			heal(2.0F);
		}


		// Steals keys from the player
		if (getHeldItemMainhand().isEmpty() && rand.nextBoolean()) {
			for (int i = 0; i < playerMP.inventory.getSizeInventory(); i++) {
				ItemStack stack = playerMP.inventory.getStackInSlot(i);
				if (stack.getItem().isIn(AngelUtils.KEYS)) {
					setHeldItem(Hand.MAIN_HAND, playerMP.inventory.getStackInSlot(i).copy());
					playerMP.inventory.getStackInSlot(i).setCount(0);
					playerMP.container.detectAndSendChanges();
					return;
				}
			}
		}
	}

	public long getTimeSincePlayedSound() {
		return timeSincePlayedSound;
	}

	public void setTimeSincePlayedSound(long timeSincePlayedSound) {
		this.timeSincePlayedSound = timeSincePlayedSound;
	}

	@Override
	protected boolean canDropLoot() {
		return true;
	}

	/*
	 * Drops Tardis Keys on Death + uses loot table drops Used to allow for config value defined tardis keys to be dropped Used instead of adding loot table functions N.B.There is a loot table function that does the same thing, but it requires: -Hardcoded item registry names -New entry for each tardis key (There could be many Tardis keys/items the player wants the angel to steal and drop on death
	 */
	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		entityDropItem(getHeldItemMainhand());
		entityDropItem(getHeldItemOffhand());
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putString(WAConstants.POSE, getAngelPose().toString());
		compound.putInt(WAConstants.TYPE, getAngelType());
		compound.putBoolean(WAConstants.ANGEL_CHILD, isCherub());
		compound.putInt(WAConstants.HUNGER_LEVEL, getHungerLevel());
	}
	
	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);

		if (compound.contains(WAConstants.POSE))
			setPose(new ResourceLocation(compound.getString(WAConstants.POSE).toLowerCase()));

		if (compound.contains(WAConstants.TYPE)) setType(compound.getInt(WAConstants.TYPE));

		if (compound.contains(WAConstants.ANGEL_CHILD)) setCherub(compound.getBoolean(WAConstants.ANGEL_CHILD));

		if (compound.contains(WAConstants.HUNGER_LEVEL)) setHungerLevel(compound.getInt(WAConstants.HUNGER_LEVEL));
	}
	
	@Override
	public void livingTick() {
		super.livingTick();
		if (ticksExisted % 2400 == 0 && !world.isRemote) {
			setHungerLevel(getHungerLevel() - 1);
			if (isWeak()) {
				attackEntityFrom(DamageSource.STARVE, 2);
			}
		}
	}

	@Override
	public void invokeSeen(PlayerEntity player) {
		super.invokeSeen(player);
		if (player instanceof ServerPlayerEntity && getSeenTime() == 1 && getPrevPos().toLong() != getPosition().toLong()) {
			setPrevPos(getPosition());

			boolean canPlaySound = !player.isCreative() && getTimeSincePlayedSound() == 0 || System.currentTimeMillis() - getTimeSincePlayedSound() >= 20000;
			// Play Sound
			if (canPlaySound) {
				if (WAConfig.CONFIG.playSeenSounds.get() && player.getDistance(this) < 15) {
					setTimeSincePlayedSound(System.currentTimeMillis());
					((ServerPlayerEntity) player).connection.sendPacket(new SPlaySoundEffectPacket(WAObjects.Sounds.ANGEL_SEEN.get(), SoundCategory.HOSTILE, player.getPosX(), player.getPosY(), player.getPosZ(), 0.2F, 1.0F));
				}
			}

			if (getAngelType() != AngelEnums.AngelType.ANGEL_THREE.getId()) {
				setPose(AngelPoses.getRandomPose().getRegistryName());
			} else {
				setPose(rand.nextBoolean() ? AngelPoses.POSE_ANGRY.getRegistryName() : AngelPoses.POSE_HIDING_FACE.getRegistryName());
			}
		}
	}
	
	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn) {
		if (!blockIn.getMaterial().isLiquid()) {
			BlockState blockstate = this.world.getBlockState(pos.up());
			SoundType soundtype = blockstate.getBlock() == Blocks.SNOW ? blockstate.getSoundType(world, pos, this) : blockIn.getSoundType(world, pos, this);
			
			if (WAConfig.CONFIG.playScrapeSounds.get() && !isCherub()) {
				playSound(WAObjects.Sounds.STONE_SCRAP.get(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
			}
			
			if (isCherub()) {
				if (world.rand.nextInt(5) == 5) {
					playSound(WAObjects.Sounds.CHILD_RUN.get(), soundtype.getVolume() * 0.15F, soundtype.getPitch());
				}
			}
			
		}
	}
	
	public boolean isWeak() {
		return getHungerLevel() < 15;
	}
	
	@Override
	public void tick() {
		super.tick();

		if (getSeenTime() == 0 || world.getLight(getPosition()) == 0 || world.isAirBlock(getPosition().down())) {
			setNoAI(false);
		}

		if (ticksExisted % 500 == 0 && getAttackTarget() == null && getSeenTime() == 0) {
			setPose(AngelPoses.POSE_HIDING_FACE.getRegistryName());
		}

		if (WAConfig.CONFIG.blockBreaking.get()) {
			replaceBlocks(getBoundingBox().grow(WAConfig.CONFIG.blockBreakRange.get()));
		}
	}


	/*
	 * @Override public void onKillEntity(LivingEntity entityLivingIn) {
	 * super.onKillEntity(entityLivingIn);
	 * 
	 * if (entityLivingIn instanceof PlayerEntity) {
	 * playSound(WAObjects.Sounds.ANGEL_NECK_SNAP.get(), 1, 1); } }
	 */
	
	@Override
	protected PathNavigator createNavigator(World worldIn) {
		GroundPathNavigator navigator = new GroundPathNavigator(this, worldIn);
		navigator.setCanSwim(false);
		navigator.setBreakDoors(true);
		navigator.setAvoidSun(false);
		return navigator;
	}
	
	private void replaceBlocks(AxisAlignedBB box) {
		if (world.isRemote || ticksExisted % 100 != 0) return;

		if (world.getLight(getPosition()) == 0) {
			return;
		}

		for (Iterator<BlockPos> iterator = BlockPos.getAllInBox(new BlockPos(box.maxX, box.maxY, box.maxZ), new BlockPos(box.minX, box.minY, box.minZ)).iterator(); iterator.hasNext(); ) {
			BlockPos pos = iterator.next();
			BlockState blockState = world.getBlockState(pos);
			if (world.getGameRules().getBoolean(GameRules.MOB_GRIEFING) && getHealth() > 5) {
/*

				if(blockState.getShape(world, pos) == VoxelShapes.empty()){
					continue;
				}

				if (ViewUtil.viewBlocked(this, blockState, pos)) {
					System.out.println("Cannot See: " + blockState);
					world.setBlockState(pos, Blocks.RED_WOOL.getDefaultState());
					continue;
				} else {
					world.setBlockState(pos, Blocks.GREEN_WOOL.getDefaultState());
				}
*/

				if (!canBreak(blockState) || blockState.getBlock() == Blocks.LAVA || blockState.getBlock() == Blocks.AIR) {
					continue;
				}

				if (blockState.getBlock() == Blocks.TORCH || blockState.getBlock() == Blocks.REDSTONE_TORCH || blockState.getBlock() == Blocks.GLOWSTONE) {
					AngelUtils.playBreakEvent(this, pos, Blocks.AIR);
					return;
				}

				if (blockState.getBlock() == Blocks.REDSTONE_LAMP) {
					if (blockState.get(RedstoneLampBlock.LIT)) {
						world.setBlockState(pos, blockState.with(RedstoneLampBlock.LIT, false));
						playSound(WAObjects.Sounds.LIGHT_BREAK.get(), 0.5F, 1.0F);
						return;
					}
				}

				if (blockState.getLightValue() > 0) {
					AngelUtils.playBreakEvent(this, pos, Blocks.AIR);
					return;
					}
					
					if (blockState.getBlock() instanceof NetherPortalBlock || blockState.getBlock() instanceof EndPortalBlock) {
						if (getHealth() < getMaxHealth()) {
							heal(1.5F);
							world.removeBlock(pos, true);
						}
					} else
						continue;
					
					return;
			}
		}
	}
	
	private boolean canBreak(BlockState blockState) {
		for (String regName : WAConfig.CONFIG.disAllowedBlocks.get()) {
			if (blockState.getBlock().getRegistryName().toString().equals(regName)) {
				return false;
			}
		}
		return true;
	}
	
	private void teleportInteraction(ServerPlayerEntity player) {
		if (world.isRemote) return;
		AngelUtils.EnumTeleportType type = AngelUtils.EnumTeleportType.valueOf(WAConfig.CONFIG.teleportType.get());
		switch (type) {
			case DONT:
				attackEntityAsMob(player);
				break;
			case STRUCTURES:
				world.getServer().enqueue(new TickDelayedTask(0, new Runnable() {
					@Override
					public void run() {
						if(!WATeleporter.handleStructures(player)){
							dealDamage(player);
						}
					}
				}));
				break;
			case RANDOM_PLACE:
				double x = player.getPosX() + rand.nextInt(WAConfig.CONFIG.teleportRange.get());
				double z = player.getPosZ() + rand.nextInt(WAConfig.CONFIG.teleportRange.get());

				ServerWorld teleportWorld = WAConfig.CONFIG.angelDimTeleport.get() ? (ServerWorld) player.world : WATeleporter.getRandomDimension(rand);
				ChunkPos chunkPos = new ChunkPos(new BlockPos(x, 0, z));
				teleportWorld.forceChunk(chunkPos.x, chunkPos.z, true);

				teleportWorld.getServer().enqueue(new TickDelayedTask(0, () -> {
					BlockPos blockPos = new BlockPos(x, yCoordSanity(teleportWorld, new BlockPos(x, 0, z)), z);

					if (AngelUtils.isOutsideOfBorder(world, blockPos)) {
						WorldInfo worldInfo = world.getWorldInfo();
						blockPos = new BlockPos(worldInfo.getSpawnX() + 12, worldInfo.getSpawnY(), worldInfo.getSpawnZ() + 12);
						blockPos = new BlockPos(blockPos.getX(), yCoordSanity(world, blockPos), blockPos.getZ());
						WeepingAngels.LOGGER.error("Weeping Angel Attempted to Teleport [" + player.getName().getUnformattedComponentText() + "] outside the world border! Correcting!");
					}

					if (teleportWorld != null) {
						WATeleporter.teleportPlayerTo(player, blockPos, teleportWorld);
						teleportWorld.forceChunk(chunkPos.x, chunkPos.z, false);
					}
				}));
				break;
		}
	}

	@Override
	public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
		return worldIn.getDifficulty() != Difficulty.PEACEFUL && this.isValidLightLevel() && super.canSpawn(worldIn, spawnReasonIn);
	}

	protected boolean isValidLightLevel() {
		BlockPos blockpos = new BlockPos(this.getPosX(), this.getBoundingBox().minY, this.getPosZ());
		if (this.world.getLightFor(LightType.SKY, blockpos) > this.rand.nextInt(32)) {
			return false;
		} else {
			int i = this.world.isThundering() ? this.world.getNeighborAwareLightSubtracted(blockpos, 10) : this.world.getLight(blockpos);
			return i <= this.rand.nextInt(8);
		}
	}

	/*
	 * Getters and Setters
	 */

	public ResourceLocation getAngelPose() {
		return new ResourceLocation(getDataManager().get(CURRENT_POSE));
	}

	public void setPose(ResourceLocation newPose) {
		getDataManager().set(CURRENT_POSE, newPose.toString());
	}
	
	public boolean isCherub() {
		return getDataManager().get(IS_CHERUB);
	}
	
	public void setCherub(boolean child) {
		getDataManager().set(IS_CHERUB, child);
	}
	
	public int getAngelType() {
		return getDataManager().get(TYPE);
	}
	
	public void setType(int angelType) {
		getDataManager().set(TYPE, angelType);
	}
	
	public int getHungerLevel() {
		return getDataManager().get(HUNGER_LEVEL);
	}
	
	public void setHungerLevel(int hunger) {
		getDataManager().set(HUNGER_LEVEL, hunger);
	}
}
