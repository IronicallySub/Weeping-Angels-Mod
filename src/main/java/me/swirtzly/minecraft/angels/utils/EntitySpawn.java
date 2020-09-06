package me.swirtzly.minecraft.angels.utils;

import me.swirtzly.minecraft.angels.WeepingAngels;
import me.swirtzly.minecraft.angels.common.WAObjects;
import me.swirtzly.minecraft.angels.config.WAConfig;
import net.minecraft.entity.EntityClassification;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class EntitySpawn {

    //Iterate through all registered biomes in the WorldGenRegistries.
    public static void addSpawnEntries() {
        for (Biome biome : ForgeRegistries.BIOMES) {
            for (String resourceLocation : WAConfig.CONFIG.allowedBiomes.get()) {
                if(resourceLocation.equalsIgnoreCase(biome.getRegistryName().toString())){
                    if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NONE && biome.getCategory() != Biome.Category.OCEAN) {
                        WeepingAngels.LOGGER.info("Weeping Angels spawns added to : [" + biome.getRegistryName() + "]");
                        biome.getSpawns(EntityClassification.valueOf(WAConfig.CONFIG.spawnType.get())).add((new Biome.SpawnListEntry(WAObjects.EntityEntries.WEEPING_ANGEL.get(), WAConfig.CONFIG.spawnWeight.get(), WAConfig.CONFIG.minSpawn.get(), WAConfig.CONFIG.maxSpawn.get())));
                    }
                }
            }
        }
    }

}