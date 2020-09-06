package me.swirtzly.minecraft.angels.common.tileentities;

import me.swirtzly.minecraft.angels.common.WAObjects;
import me.swirtzly.minecraft.angels.common.entities.WeepingAngelEntity;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.block.SnowBlock.LAYERS;

public class SnowArmTile extends TileEntity implements ITickableTileEntity {
	
	private AxisAlignedBB AABB = new AxisAlignedBB(0.2, 0, 0, 0.8, 2, 0.1);
	
	public SnowArmTile() {
		super(WAObjects.Tiles.ARM.get());
	}
	
	@Override
	public void tick() {
		if (!world.getEntitiesWithinAABB(PlayerEntity.class, AABB.offset(getPos())).isEmpty() && !world.isRemote) {
			WeepingAngelEntity angel = new WeepingAngelEntity(world);
			angel.setCherub(false);
			BlockPos newPos = getPos();
			angel.setPosition(newPos.getX() + 0.5D, newPos.getY(), newPos.getZ() + 0.5D);
			world.addEntity(angel);
			Integer layers = world.getBlockState(pos).get(LAYERS);
			world.setBlockState(pos, Blocks.SNOW.getDefaultState().with(LAYERS, layers));
		}
	}
}
