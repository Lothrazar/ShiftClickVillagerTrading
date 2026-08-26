package com.lothrazar.shiftclickvillagertrading;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRegistry {

  public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModMain.MODID);
  public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ModMain.MODID);
  public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ModMain.MODID);
  //  public static final DeferredHolder<Item, Item> OVERWORLD_KEY = ITEMS.registerItem("whatever", props -> new Item(props));
  //  public static final DeferredHolder<Block, Block> STUFF_BLOCK = BLOCKS.registerBlock("stuff", props -> new Block(props));
  //  public static final DeferredHolder<Item, Item> STUFF_ITEM = ITEMS.registerItem("stuff", props -> new BlockItem(STUFF_BLOCK.get(), props));
}
