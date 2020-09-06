package me.swirtzly.minecraft.angels.common.items;

import java.util.function.Function;

import me.swirtzly.minecraft.angels.common.WAObjects;
import me.swirtzly.minecraft.angels.common.entities.AngelEnums;
import me.swirtzly.minecraft.angels.common.entities.WeepingAngelEntity;
import me.swirtzly.minecraft.angels.common.misc.WATabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AngelSpawnerItem<E extends WeepingAngelEntity> extends Item {
	
	@SuppressWarnings("unused")
	private Function<World, E> entityCreator;
	private AngelEnums.AngelType type;
	
	public AngelSpawnerItem(AngelEnums.AngelType type, Function<World, E> angel) {
		super(new Properties().group(WATabs.MAIN_TAB));
		entityCreator = angel;
		this.type = type;
	}
	
	@Override
	public ActionResultType onItemUse(ItemUseContext context) {
		World worldIn = context.getWorld();
		BlockPos pos = context.getPos();
		PlayerEntity player = context.getPlayer();
		Hand hand = player.getActiveHand();
		
		if (!worldIn.isRemote) {
			WeepingAngelEntity angel = WAObjects.EntityEntries.WEEPING_ANGEL.get().create(worldIn);
			angel.setType(type.getId());
			angel.setCherub(type.isChild());
			angel.setPosition(pos.getX(), pos.getY() + 1, pos.getZ());
			angel.faceEntity(player, 10.0F, 10.0F);
			player.getHeldItem(hand).shrink(1);
			worldIn.addEntity(angel);
		}
		return super.onItemUse(context);
	}
	
}
