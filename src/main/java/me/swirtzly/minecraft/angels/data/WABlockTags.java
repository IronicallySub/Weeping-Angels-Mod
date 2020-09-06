package me.swirtzly.minecraft.angels.data;

import me.swirtzly.minecraft.angels.common.WAObjects;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;

public class WABlockTags extends BlockTagsProvider {

    public WABlockTags(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerTags() {
        add(BlockTags.STONE_BRICKS, WAObjects.Blocks.ARM.get(), WAObjects.Blocks.STATUE.get(),WAObjects.Blocks.PLINTH.get());
    }

    public void add(Tag<Block> branch, Block block) {
        this.getBuilder(branch).add(block);
    }

    public void add(Tag<Block> branch, Block... block) {
        this.getBuilder(branch).add(block);
    }
}
