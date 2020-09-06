package me.swirtzly.minecraft.angels.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MineableBlock extends Block {

	private final ResourceLocation lootTable;

	public MineableBlock(ResourceLocation lootLocation) {
		super(Properties.create(Material.ROCK).hardnessAndResistance(3).sound(SoundType.STONE));
		this.lootTable = lootLocation;
	}

	@Override
	public ResourceLocation getLootTable() {
		return lootTable;
	}
}
