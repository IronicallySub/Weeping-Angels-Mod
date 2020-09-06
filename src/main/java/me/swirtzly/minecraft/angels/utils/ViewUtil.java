package me.swirtzly.minecraft.angels.utils;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import me.swirtzly.minecraft.angels.WeepingAngels;
import me.swirtzly.minecraft.angels.common.entities.QuantumLockBaseEntity;
import me.swirtzly.minecraft.angels.config.WAConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;

public class ViewUtil {
	
	public static boolean isInFrontOfEntity(LivingEntity entity, Entity target, boolean vr) {
		Vec3d vecTargetsPos = target.getPositionVec();
		Vec3d vecLook;

		if (vr) {
			if (entity instanceof PlayerEntity) {
				vecLook = WeepingAngels.reflector.getHMDRot((PlayerEntity) entity);
			} else {
				throw new RuntimeException("Attempted to use a non-player entity with VRSupport: " + entity.getPersistentData());
			}
		} else {
			vecLook = entity.getLookVec();
		}
		Vec3d vecFinal = vecTargetsPos.subtractReverse(new Vec3d(entity.getPosX(), entity.getPosY(), entity.getPosZ())).normalize();
		vecFinal = new Vec3d(vecFinal.x, 0.0D, vecFinal.z);
		return vecFinal.dotProduct(vecLook) < 0.0;
	}
	
	/**
	 * Method that detects whether a entity is the the view sight of another entity
	 *
	 * @param viewer The viewer entity
	 * @param beingViewed The entity being watched by viewer
	 */
	public static boolean canEntitySee(LivingEntity viewer, LivingEntity beingViewed) {
		double dx = beingViewed.getPosX() - viewer.getPosX();
		double dz;
		for (dz = beingViewed.getPosX() - viewer.getPosZ(); dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D) {
			dx = (Math.random() - Math.random()) * 0.01D;
		}
		while (viewer.rotationYaw > 360) {
			viewer.rotationYaw -= 360;
		}
		while (viewer.rotationYaw < -360) {
			viewer.rotationYaw += 360;
		}
		float yaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - viewer.rotationYaw;
		yaw = yaw - 90;
		while (yaw < -180) {
			yaw += 360;
		}
		while (yaw >= 180) {
			yaw -= 360;
		}
		
		return yaw < 60 && yaw > -60 && viewer.canEntityBeSeen(beingViewed);
	}
	
	public static boolean isInSightPos(LivingEntity viewer, BlockPos pos) {
		double dx = pos.getX() - viewer.getPosX();;
		double dz;
		for (dz = pos.getX() - viewer.getPosZ(); dx * dx + dz * dz < 1.0E-4D; dz = (Math.random() - Math.random()) * 0.01D) {
			dx = (Math.random() - Math.random()) * 0.01D;
		}
		while (viewer.rotationYaw > 360) {
			viewer.rotationYaw -= 360;
		}
		while (viewer.rotationYaw < -360) {
			viewer.rotationYaw += 360;
		}
		float yaw = (float) (Math.atan2(dz, dx) * 180.0D / Math.PI) - viewer.rotationYaw;
		yaw = yaw - 90;
		while (yaw < -180) {
			yaw += 360;
		}
		while (yaw >= 180) {
			yaw -= 360;
		}
		return yaw < 60 && yaw > -60;
	}
	
	/**
	 * Method that detects whether a tile is the the view sight of viewer
	 *
	 * @param livingBase The viewer entity
	 * @param angel The entity being watched by viewer
	 */
	public static boolean isInSight(LivingEntity livingBase, QuantumLockBaseEntity angel) {
		if (viewBlocked(livingBase, angel)) {
			return false;
		}
		if (livingBase instanceof PlayerEntity) {
			return isInFrontOfEntity(livingBase, angel, WeepingAngels.reflector.isVRPlayer((PlayerEntity) livingBase));
		}
		return isInFrontOfEntity(livingBase, angel, false);
	}

	private static final float headSize = 0.15f;

	public static boolean viewBlocked(LivingEntity viewer, LivingEntity angel) {
		AxisAlignedBB viewerBoundBox = viewer.getBoundingBox();
		AxisAlignedBB angelBoundingBox = angel.getBoundingBox();
		Vec3d[] viewerPoints = { new Vec3d(viewerBoundBox.minX, viewerBoundBox.minY, viewerBoundBox.minZ), new Vec3d(viewerBoundBox.minX, viewerBoundBox.minY, viewerBoundBox.maxZ), new Vec3d(viewerBoundBox.minX, viewerBoundBox.maxY, viewerBoundBox.minZ), new Vec3d(viewerBoundBox.minX, viewerBoundBox.maxY, viewerBoundBox.maxZ), new Vec3d(viewerBoundBox.maxX, viewerBoundBox.maxY, viewerBoundBox.minZ), new Vec3d(viewerBoundBox.maxX, viewerBoundBox.maxY, viewerBoundBox.maxZ), new Vec3d(viewerBoundBox.maxX, viewerBoundBox.minY, viewerBoundBox.maxZ), new Vec3d(viewerBoundBox.maxX, viewerBoundBox.minY, viewerBoundBox.minZ), };

		if (viewer instanceof PlayerEntity) {
			Vec3d pos;
			if (WeepingAngels.reflector.isVRPlayer((PlayerEntity) viewer))
				pos = WeepingAngels.reflector.getHMDPos((PlayerEntity) viewer);
			else
				pos = new Vec3d(viewer.getPosX(), viewer.getPosY() + 1.62f, viewer.getPosZ());
			viewerPoints[0] = pos.add(-headSize, -headSize, -headSize);
			viewerPoints[1] = pos.add(-headSize, -headSize, headSize);
			viewerPoints[2] = pos.add(-headSize, headSize, -headSize);
			viewerPoints[3] = pos.add(-headSize, headSize, headSize);
			viewerPoints[4] = pos.add(headSize, headSize, -headSize);
			viewerPoints[5] = pos.add(headSize, headSize, headSize);
			viewerPoints[6] = pos.add(headSize, -headSize, headSize);
			viewerPoints[7] = pos.add(headSize, -headSize, -headSize);
		}


		Vec3d[] angelPoints = { new Vec3d(angelBoundingBox.minX, angelBoundingBox.minY, angelBoundingBox.minZ), new Vec3d(angelBoundingBox.minX, angelBoundingBox.minY, angelBoundingBox.maxZ), new Vec3d(angelBoundingBox.minX, angelBoundingBox.maxY, angelBoundingBox.minZ), new Vec3d(angelBoundingBox.minX, angelBoundingBox.maxY, angelBoundingBox.maxZ), new Vec3d(angelBoundingBox.maxX, angelBoundingBox.maxY, angelBoundingBox.minZ), new Vec3d(angelBoundingBox.maxX, angelBoundingBox.maxY, angelBoundingBox.maxZ), new Vec3d(angelBoundingBox.maxX, angelBoundingBox.minY, angelBoundingBox.maxZ), new Vec3d(angelBoundingBox.maxX, angelBoundingBox.minY, angelBoundingBox.minZ), };

		for (int i = 0; i < viewerPoints.length; i++) {
			if (viewer.world.rayTraceBlocks(new RayTraceContext(viewerPoints[i], angelPoints[i], RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, viewer)).getType() == RayTraceResult.Type.MISS) {
				return false;
			}
			if (rayTraceBlocks(viewer, viewer.world, viewerPoints[i], angelPoints[i], pos -> {
				BlockState state = viewer.world.getBlockState(pos);
				return !canSeeThrough(state, viewer.world, pos);
			}) == null) return false;
		}

		if (angel.ticksExisted % 1200 == 0) {
			if (angel.getDistance(viewer) < 15) {
				viewer.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 15));
			}
		}

		return true;
	}

	public static boolean viewBlocked(LivingEntity viewer, BlockState blockState, BlockPos blockPos) {
		AxisAlignedBB viewerBoundBox = viewer.getBoundingBox();
		AxisAlignedBB angelBoundingBox = blockState.getShape(viewer.world, blockPos).getBoundingBox();
		Vec3d[] viewerPoints = { new Vec3d(viewerBoundBox.minX, viewerBoundBox.minY, viewerBoundBox.minZ), new Vec3d(viewerBoundBox.minX, viewerBoundBox.minY, viewerBoundBox.maxZ), new Vec3d(viewerBoundBox.minX, viewerBoundBox.maxY, viewerBoundBox.minZ), new Vec3d(viewerBoundBox.minX, viewerBoundBox.maxY, viewerBoundBox.maxZ), new Vec3d(viewerBoundBox.maxX, viewerBoundBox.maxY, viewerBoundBox.minZ), new Vec3d(viewerBoundBox.maxX, viewerBoundBox.maxY, viewerBoundBox.maxZ), new Vec3d(viewerBoundBox.maxX, viewerBoundBox.minY, viewerBoundBox.maxZ), new Vec3d(viewerBoundBox.maxX, viewerBoundBox.minY, viewerBoundBox.minZ), };

		if (viewer instanceof PlayerEntity) {
			Vec3d pos;
			if (WeepingAngels.reflector.isVRPlayer((PlayerEntity) viewer))
				pos = WeepingAngels.reflector.getHMDPos((PlayerEntity) viewer);
			else
				pos = new Vec3d(viewer.getPosX(), viewer.getPosY() + 1.62f, viewer.getPosZ());
			viewerPoints[0] = pos.add(-headSize, -headSize, -headSize);
			viewerPoints[1] = pos.add(-headSize, -headSize, headSize);
			viewerPoints[2] = pos.add(-headSize, headSize, -headSize);
			viewerPoints[3] = pos.add(-headSize, headSize, headSize);
			viewerPoints[4] = pos.add(headSize, headSize, -headSize);
			viewerPoints[5] = pos.add(headSize, headSize, headSize);
			viewerPoints[6] = pos.add(headSize, -headSize, headSize);
			viewerPoints[7] = pos.add(headSize, -headSize, -headSize);
		}


		Vec3d[] angelPoints = { new Vec3d(angelBoundingBox.minX, angelBoundingBox.minY, angelBoundingBox.minZ), new Vec3d(angelBoundingBox.minX, angelBoundingBox.minY, angelBoundingBox.maxZ), new Vec3d(angelBoundingBox.minX, angelBoundingBox.maxY, angelBoundingBox.minZ), new Vec3d(angelBoundingBox.minX, angelBoundingBox.maxY, angelBoundingBox.maxZ), new Vec3d(angelBoundingBox.maxX, angelBoundingBox.maxY, angelBoundingBox.minZ), new Vec3d(angelBoundingBox.maxX, angelBoundingBox.maxY, angelBoundingBox.maxZ), new Vec3d(angelBoundingBox.maxX, angelBoundingBox.minY, angelBoundingBox.maxZ), new Vec3d(angelBoundingBox.maxX, angelBoundingBox.minY, angelBoundingBox.minZ), };

		for (int i = 0; i < viewerPoints.length; i++) {
			if (viewer.world.rayTraceBlocks(new RayTraceContext(viewerPoints[i], angelPoints[i], RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, viewer)).getType() == RayTraceResult.Type.MISS) {
				return false;
			}
			if (rayTraceBlocks(viewer, viewer.world, viewerPoints[i], angelPoints[i], pos -> {
				BlockState state = viewer.world.getBlockState(pos);
				return !canSeeThrough(state, viewer.world, pos);
			}) == null) return false;
		}
		return true;
	}


	@Nullable
	private static RayTraceResult rayTraceBlocks(LivingEntity livingEntity, World world, Vec3d vec31, Vec3d vec32, Predicate<BlockPos> stopOn) {
		if (!Double.isNaN(vec31.x) && !Double.isNaN(vec31.y) && !Double.isNaN(vec31.z)) {
			if (!Double.isNaN(vec32.x) && !Double.isNaN(vec32.y) && !Double.isNaN(vec32.z)) {
				int i = MathHelper.floor(vec32.x);
				int j = MathHelper.floor(vec32.y);
				int k = MathHelper.floor(vec32.z);
				int l = MathHelper.floor(vec31.x);
				int i1 = MathHelper.floor(vec31.y);
				int j1 = MathHelper.floor(vec31.z);
				BlockPos blockpos = new BlockPos(l, i1, j1);
				if (stopOn.test(blockpos)) {
					RayTraceResult raytraceresult = world.rayTraceBlocks(new RayTraceContext(vec31, vec32, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, livingEntity));
					if (raytraceresult != null) {
						return raytraceresult;
					}
				}
				
				int k1 = 200;
				
				while (k1-- >= 0) {
					if (Double.isNaN(vec31.x) || Double.isNaN(vec31.y) || Double.isNaN(vec31.z)) {
						return null;
					}
					
					if (l == i && i1 == j && j1 == k) {
						return null;
					}
					
					boolean flag2 = true;
					boolean flag = true;
					boolean flag1 = true;
					double d0 = 999.0D;
					double d1 = 999.0D;
					double d2 = 999.0D;
					
					if (i > l) {
						d0 = (double) l + 1.0D;
					} else if (i < l) {
						d0 = (double) l + 0.0D;
					} else {
						flag2 = false;
					}
					
					if (j > i1) {
						d1 = (double) i1 + 1.0D;
					} else if (j < i1) {
						d1 = (double) i1 + 0.0D;
					} else {
						flag = false;
					}
					
					if (k > j1) {
						d2 = (double) j1 + 1.0D;
					} else if (k < j1) {
						d2 = (double) j1 + 0.0D;
					} else {
						flag1 = false;
					}
					
					double d3 = 999.0D;
					double d4 = 999.0D;
					double d5 = 999.0D;
					double d6 = vec32.x - vec31.x;
					double d7 = vec32.y - vec31.y;
					double d8 = vec32.z - vec31.z;
					
					if (flag2) {
						d3 = (d0 - vec31.x) / d6;
					}
					
					if (flag) {
						d4 = (d1 - vec31.y) / d7;
					}
					
					if (flag1) {
						d5 = (d2 - vec31.z) / d8;
					}
					
					if (d3 == -0.0D) {
						d3 = -1.0E-4D;
					}
					
					if (d4 == -0.0D) {
						d4 = -1.0E-4D;
					}
					
					if (d5 == -0.0D) {
						d5 = -1.0E-4D;
					}
					
					Direction enumfacing;
					
					if (d3 < d4 && d3 < d5) {
						enumfacing = i > l ? Direction.WEST : Direction.EAST;
						vec31 = new Vec3d(d0, vec31.y + d7 * d3, vec31.z + d8 * d3);
					} else if (d4 < d5) {
						enumfacing = j > i1 ? Direction.DOWN : Direction.UP;
						vec31 = new Vec3d(vec31.x + d6 * d4, d1, vec31.z + d8 * d4);
					} else {
						enumfacing = k > j1 ? Direction.NORTH : Direction.SOUTH;
						vec31 = new Vec3d(vec31.x + d6 * d5, vec31.y + d7 * d5, d2);
					}
					
					l = MathHelper.floor(vec31.x) - (enumfacing == Direction.EAST ? 1 : 0);
					i1 = MathHelper.floor(vec31.y) - (enumfacing == Direction.UP ? 1 : 0);
					j1 = MathHelper.floor(vec31.z) - (enumfacing == Direction.SOUTH ? 1 : 0);
					blockpos = new BlockPos(l, i1, j1);
					if (stopOn.test(blockpos)) {
						RayTraceResult raytraceresult1 = world.rayTraceBlocks(new RayTraceContext(vec31, vec32, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, livingEntity));
						
						if (raytraceresult1 != null) {
							return raytraceresult1;
						}
					}
				}
			}
		}
		
		return null;
	}
	
	// This is bloated, I know, but I want to make sure I cover EVERY basis :/
	public static boolean canSeeThrough(BlockState blockState, World world, BlockPos pos) {
		
		// Covers all Block, Material and Tag checks :D
		if (!blockState.isSolid() || !blockState.isOpaqueCube(world, pos)) {
			return true;
		}
		
		Block block = blockState.getBlock();
		
		// Special Snowflakes
		if (block instanceof DoorBlock) {
			return blockState.get(DoorBlock.HALF) == DoubleBlockHalf.UPPER;
		}
		
		// Config
		for (String transparent_block : WAConfig.CONFIG.transparent_blocks.get()) {
			if (blockState.getBlock().getRegistryName().toString().equals(transparent_block)) return true;
		}
		
		return blockState.getCollisionShape(world, pos) == VoxelShapes.empty();
	}
	
}
