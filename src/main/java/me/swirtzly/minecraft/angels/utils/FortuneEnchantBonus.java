package me.swirtzly.minecraft.angels.utils;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import me.swirtzly.minecraft.angels.WeepingAngels;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootFunction;
import net.minecraft.world.storage.loot.LootParameter;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.ILootFunction;

/**
 * Created by 50ap5ud5 on 13 Feb 2020 @ 11:28:06 am
 */
public class FortuneEnchantBonus extends LootFunction {

	private final RandomValueRange count;
	private final int limit;

	private FortuneEnchantBonus(ILootCondition[] conditions, RandomValueRange countIn, int limitIn) {
		super(conditions);
		this.count = countIn;
		this.limit = limitIn;
	}

	public static FortuneEnchantBonus.Builder func_215915_a(RandomValueRange p_215915_0_) {
		return new FortuneEnchantBonus.Builder(p_215915_0_);
	}

	public Set<LootParameter<?>> getRequiredParameters() {
		return ImmutableSet.of(LootParameters.KILLER_ENTITY);
	}

	private boolean func_215917_b() {
		return this.limit > 0;
	}

	public ItemStack doApply(ItemStack stack, LootContext context) {
		Entity entity = context.get(LootParameters.KILLER_ENTITY);
		if (entity instanceof LivingEntity) {
			int i = AngelUtils.getFortuneModifier((LivingEntity) entity);
			if (i == 0) {
				return stack;
			}

			float f = (float) i * this.count.generateFloat(context.getRandom());
			stack.grow(Math.round(f));
			if (this.func_215917_b() && stack.getCount() > this.limit) {
				stack.setCount(this.limit);
			}
		}

		return stack;
	}

	public static class Builder extends LootFunction.Builder<FortuneEnchantBonus.Builder> {
		private final RandomValueRange field_216073_a;
		private int field_216074_b = 0;

		public Builder(RandomValueRange p_i50932_1_) {
			this.field_216073_a = p_i50932_1_;
		}

		protected FortuneEnchantBonus.Builder doCast() {
			return this;
		}

		public FortuneEnchantBonus.Builder func_216072_a(int p_216072_1_) {
			this.field_216074_b = p_216072_1_;
			return this;
		}

		public ILootFunction build() {
			return new FortuneEnchantBonus(this.getConditions(), this.field_216073_a, this.field_216074_b);
		}
	}

	public static class Serializer extends LootFunction.Serializer<FortuneEnchantBonus> {
		public Serializer() {
			super(new ResourceLocation(WeepingAngels.MODID, "fortune_enchant"), FortuneEnchantBonus.class);
		}

		public void serialize(JsonObject object, FortuneEnchantBonus functionClazz, JsonSerializationContext serializationContext) {
			super.serialize(object, functionClazz, serializationContext);
			object.add("count", serializationContext.serialize(functionClazz.count));
			if (functionClazz.func_215917_b()) {
				object.add("limit", serializationContext.serialize(functionClazz.limit));
			}

		}

		public FortuneEnchantBonus deserialize(JsonObject object, JsonDeserializationContext deserializationContext, ILootCondition[] conditionsIn) {
			int i = JSONUtils.getInt(object, "limit", 0);
			return new FortuneEnchantBonus(conditionsIn, JSONUtils.deserializeClass(object, "count", deserializationContext, RandomValueRange.class), i);
		}
	}

}