package me.swirtzly.minecraft.angels.common.entities;

import me.swirtzly.minecraft.angels.common.WAObjects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AnomalyEntity extends MobEntity {
	
	private static final DataParameter<Float> EYE_HEIGHT = EntityDataManager.createKey(AnomalyEntity.class, DataSerializers.FLOAT);
	
	public AnomalyEntity(World worldIn) {
		super(WAObjects.EntityEntries.ANOMALY.get(), worldIn);
	}
	
	public AnomalyEntity(EntityType<?> type, World world) {
		this(world);
	}
	
	@Override
	public boolean isInvulnerable() {
		return true;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_PLAYER_ATTACK_WEAK;
	}
	
	@Override
	protected boolean isMovementBlocked() {
		return true;
	}
	
	@Override
	public void tick() {
		super.tick();
		
		setNoAI(true);
		if (ticksExisted == 1) {
			playSound(WAObjects.Sounds.TELEPORT.get(), 1.0F, 1.0F);
		}
		
		if (ticksExisted > 100) {
			remove();
		}

		if(!world.isRemote) {
			for (WeepingAngelEntity weepingAngelEntity : world.getEntitiesWithinAABB(WeepingAngelEntity.class, getBoundingBox().grow(10))) {
				BlockPos pos = getPosition().subtract(weepingAngelEntity.getPosition());
				Vec3d vec = new Vec3d(pos.getX(), pos.getY(), pos.getZ()).normalize();
				weepingAngelEntity.setNoAI(false);
				weepingAngelEntity.setMotion(vec.scale(0.25D));
			}
		}
	}

	@Override
	protected void collideWithEntity(Entity entityIn) {
		super.collideWithEntity(entityIn);

		if(entityIn instanceof WeepingAngelEntity){
			WeepingAngelEntity weepingAngelEntity = (WeepingAngelEntity) entityIn;
			weepingAngelEntity.dropAngelStuff();
			weepingAngelEntity.remove();
		}
	}

	@Override
	protected void registerData() {
		super.registerData();
		getDataManager().register(EYE_HEIGHT, getEyeHeight());
	}
	
	public float getEntityEyeHeight() {
		return getDataManager().get(EYE_HEIGHT);
	}
	
	public void setEntityEyeHeight(float eyeHeight) {
		getDataManager().set(EYE_HEIGHT, eyeHeight);
	}
}
