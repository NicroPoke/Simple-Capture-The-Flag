package com.example.examplemod.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.example.examplemod.SimpleCaptureTheFlag;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SimpleCaptureTheFlag.MODID);

    public static final DeferredItem<BlockItem> RED_POINT_ITEM = ITEMS.registerSimpleBlockItem("red_point", ModBlocks.RED_POINT);
    public static final DeferredItem<BlockItem> BLUE_POINT_ITEM = ITEMS.registerSimpleBlockItem("blue_point", ModBlocks.BLUE_POINT);

    private ModItems() {
    }
}
