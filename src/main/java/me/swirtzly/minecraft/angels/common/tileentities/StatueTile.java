package me.swirtzly.minecraft.angels.common.tileentities;

import me.swirtzly.minecraft.angels.client.poses.AngelPoses;
import me.swirtzly.minecraft.angels.common.WAObjects;
import me.swirtzly.minecraft.angels.common.entities.AngelEnums;
import me.swirtzly.minecraft.angels.common.entities.WeepingAngelEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Swirtzly on 17/02/2020 @ 12:18
 */
public class StatueTile extends TileEntity implements ITickableTileEntity {
	private int rotation =0, type = 0;
	private ResourceLocation pose = AngelPoses.getRandomPose().getRegistryName();
	
	public StatueTile() {
		super(WAObjects.Tiles.STATUE.get());
	}

	public int getAngelType() {
		return type;
	}

	public void setAngelType(int type) {
		this.type = type;
	}

	@Override
	public void read(CompoundNBT compound) {
		super.read(compound);
		setPose(new ResourceLocation(compound.getString("pose")));
		setRotation(compound.getInt("rotation"));
		type = compound.getInt("type");
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		compound.putString("pose", pose.toString());
		compound.putInt("rotation", rotation);
		compound.putInt("type", type);
		return compound;
	}
	
	public ResourceLocation getPose() {
		return pose;
	}
	
	public void setPose(ResourceLocation pose) {
		this.pose = pose;
		sendUpdates();
	}

	public void sendUpdates() {
		world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
		world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		markDirty();
	}


	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		return new SUpdateTileEntityPacket(pos, 3, getUpdateTag());
	}


	@Override
	public CompoundNBT getUpdateTag() {
		return write(new CompoundNBT());
	}

	public int getRotation() {
		return rotation;
	}
	
	public void setRotation(int rotation) {
		this.rotation = rotation;
		sendUpdates();
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		super.onDataPacket(net, pkt);
		handleUpdateTag(pkt.getNbtCompound());
	}


	@Override
	public void tick() {
		if (world.isRemote) return;
		
		if (world.getRedstonePowerFromNeighbors(pos) > 0 && world.getTileEntity(pos) instanceof StatueTile) {
			WeepingAngelEntity angel = new WeepingAngelEntity(world);
			angel.setType(type);
			angel.setCherub(false);
			angel.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0, 0);
			angel.setPose(getPose());
			world.addEntity(angel);
			world.removeBlock(getPos(), false);
			sendUpdates();
		}
	}
}
