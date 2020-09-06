package me.swirtzly.minecraft.angels.utils;

import com.google.common.collect.Lists;
import me.swirtzly.minecraft.angels.common.WAObjects;
import me.swirtzly.minecraft.angels.config.WAConfig;
import me.swirtzly.minecraft.angels.network.Network;
import me.swirtzly.minecraft.angels.network.messages.MessageSFX;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Random;

public class WATeleporter {
	
	public static int yCoordSanity(World world, BlockPos pos) {
		for (int y = world.getHeight(); y > 0; --y) {
			BlockPos newPos = new BlockPos(pos.getX(), y, pos.getZ());
			BlockState state = world.getBlockState(newPos);
			BlockState underState = world.getBlockState(newPos.down());

			if (!state.causesSuffocation(world, newPos) && underState.isSolid() && !isPosBelowOrAboveWorld(world, newPos.getY())) {
				return newPos.getY();
			}
		}
		return pos.getY();
	}
	
	public static ServerWorld getRandomDimension(Random rand) {
		Iterable<ServerWorld> dimensions = ServerLifecycleHooks.getCurrentServer().getWorlds();
		ArrayList<ServerWorld> allowedDimensions = Lists.newArrayList(dimensions);
		
		for (ServerWorld dimension : dimensions) {
			for (String dimName : WAConfig.CONFIG.notAllowedDimensions.get()) {
				if (dimension.getDimension().getType().getRegistryName().toString().equalsIgnoreCase(dimName) || dimension.getDimension().getType().getRegistryName().toString().contains("tardis")) {
					allowedDimensions.remove(dimension);
				}
			}
		}
		return allowedDimensions.get(rand.nextInt(allowedDimensions.size()));
	}
	
	public static boolean handleStructures(ServerPlayerEntity player) {

		String[] targetStructure = null;

		switch (player.world.getDimension().getType().getRegistryName().toString()) {
			case "minecraft:overworld":
				targetStructure = AngelUtils.OVERWORLD_STRUCTURES;
				break;

			case "minecraft:end":
				targetStructure = AngelUtils.END_STRUCTURES;
				break;

			case "minecraft:nether":
				targetStructure = AngelUtils.NETHER_STRUCTURES;
				break;
		}
		
		if (targetStructure != null) {
			ServerWorld serverWorld = (ServerWorld) player.world;
			BlockPos bPos = serverWorld.findNearestStructure(targetStructure[player.world.rand.nextInt(targetStructure.length)], player.getPosition(), Integer.MAX_VALUE, false);
			if (bPos != null) {
				teleportPlayerTo(player, bPos, player.getServerWorld());
				return true;
			}
		}
		return false;
	}

	public static void teleportPlayerTo(ServerPlayerEntity player, BlockPos destinationPos, ServerWorld targetDimension) {
		Network.sendTo(new MessageSFX(WAObjects.Sounds.TELEPORT.get().getRegistryName()), player);
		player.teleport(targetDimension, destinationPos.getX(), destinationPos.getY(), destinationPos.getZ(), player.rotationYaw, player.rotationPitch);
	}


	public static boolean isPosBelowOrAboveWorld(World dim, int y) {
		if (dim.getDimension().getType().equals(DimensionType.THE_NETHER.getRegistryName())) {
			return y <= 0 || y >= 126;
		}
		return y <= 0 || y >= 256;
	}

}
