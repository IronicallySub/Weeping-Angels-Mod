package me.swirtzly.minecraft.angels.common.entities;

import me.swirtzly.minecraft.angels.common.misc.WAConstants;
import me.swirtzly.minecraft.angels.config.WAConfig;
import me.swirtzly.minecraft.angels.utils.AngelUtils;
import me.swirtzly.minecraft.angels.utils.ViewUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class QuantumLockBaseEntity extends MonsterEntity implements IMob {

	private static final DataParameter<Boolean> IS_SEEN = EntityDataManager.createKey(QuantumLockBaseEntity.class, DataSerializers.BOOLEAN);
	private static final DataParameter<Integer> TIME_VIEWED = EntityDataManager.createKey(QuantumLockBaseEntity.class, DataSerializers.VARINT);
	private static final DataParameter<BlockPos> PREVBLOCKPOS = EntityDataManager.createKey(QuantumLockBaseEntity.class, DataSerializers.BLOCK_POS);

	public QuantumLockBaseEntity(World worldIn, EntityType<? extends MonsterEntity> entityType) {
		super(entityType, worldIn);
	}
	
	@Override
	public void livingTick() {
		super.livingTick();

		rotationYawHead = rotationYaw;
		if (!world.isRemote && ticksExisted % 5 == 0) {
			List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, getBoundingBox().grow(WAConfig.CONFIG.stalkRange.get()));
			players.removeIf(player -> player.isSpectator() || player.isInvisible() || player.isPlayerFullyAsleep() || player.world != world);

			if (WAConfig.CONFIG.freezeOnAngel.get()) {
				List<WeepingAngelEntity> angels = world.getEntitiesWithinAABB(WeepingAngelEntity.class, getBoundingBox().grow(WAConfig.CONFIG.stalkRange.get()));
				for (WeepingAngelEntity angel : angels) {
					if (angel.getUniqueID() != getUniqueID() && ViewUtil.isInSight(angel, this)) {
						setSeenTime(getSeenTime() + 1);
						setNoAI(true);
						return;
					}
				}
			}

			if (players.isEmpty()) {
				setSeenTime(0);
			} else {
				PlayerEntity targetPlayer = null;
				for (PlayerEntity player : players) {
					if (ViewUtil.isInSight(player, this) && !AngelUtils.isDarkForPlayer(this, player)) {
						setSeenTime(getSeenTime() + 1);
						invokeSeen(player);
						return;
					} else if (targetPlayer == null) {
						targetPlayer = player;
						setSeenTime(0);
					}
				}

				Vec3d vecPos = getPositionVec();
				Vec3d vecPlayerPos = targetPlayer.getPositionVec();
				float angle = (float) Math.toDegrees((float) Math.atan2(vecPos.z - vecPlayerPos.z, vecPos.x - vecPlayerPos.x));
				rotationYawHead = rotationYaw = angle > 180 ? angle : angle + 90;
				if (isSeen()) return;
				if (getDistance(targetPlayer) < 2)
					attackEntityAsMob(targetPlayer);
				else
					moveTowards(targetPlayer);
			}
		}
	}
	
	public void moveTowards(LivingEntity targetPlayer) {
		getNavigator().tryMoveToEntityLiving(targetPlayer, WAConfig.CONFIG.moveSpeed.get());
	}
	
	@Override
	protected void registerData() {
		super.registerData();
		getDataManager().register(IS_SEEN, false);
		getDataManager().register(TIME_VIEWED, 0);
		getDataManager().register(PREVBLOCKPOS, BlockPos.ZERO);
	}
	
	@Override
	public void deserializeNBT(CompoundNBT compound) {
		super.deserializeNBT(compound);
		if (compound.contains(WAConstants.TIME_SEEN)) setSeenTime(compound.getInt(WAConstants.TIME_SEEN));
		if (compound.contains(WAConstants.PREVPOS)) setPrevPos(getPrevPos());
	}
	
	@Override
	public void writeAdditional(CompoundNBT compound) {
		super.writeAdditional(compound);
		compound.putBoolean(WAConstants.IS_SEEN, isSeen());
		compound.putInt(WAConstants.TIME_SEEN, getSeenTime());
		compound.putLong(WAConstants.PREVPOS, getPrevPos().toLong());
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public boolean canRenderOnFire() {
		return false;
	}
	
	@Override
	public boolean isInvulnerableTo(DamageSource source) {
		return super.isInvulnerableTo(source);
	}
	
	public boolean isSeen() {
		return getSeenTime() > 0;
	}
	
	public int getSeenTime() {
		return getDataManager().get(TIME_VIEWED);
	}
	
	public void setSeenTime(int time) {
		getDataManager().set(TIME_VIEWED, time);
	}
	
	public BlockPos getPrevPos() {
		return getDataManager().get(PREVBLOCKPOS);
	}
	
	public void setPrevPos(BlockPos pos) {
		getDataManager().set(PREVBLOCKPOS, pos);
	}
	
	public void invokeSeen(PlayerEntity player) {
		setNoAI(true);
		getLookController().setLookPositionWithEntity(player, 30, 30);
		getNavigator().setPath(null, 0);
		setMotion(new Vec3d(0, 0, 0));
	}

	public BlockPos getPosition(){
		return new BlockPos(getPosX(), getPosY(), getPosZ());
	}


}
