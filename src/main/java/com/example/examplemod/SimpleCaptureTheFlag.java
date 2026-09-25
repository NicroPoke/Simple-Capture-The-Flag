package com.example.examplemod;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import com.example.examplemod.registry.ModBlockEntities;
import com.example.examplemod.registry.ModBlocks;
import com.example.examplemod.registry.ModCreativeTabs;
import com.example.examplemod.registry.ModItems;

@Mod(SimpleCaptureTheFlag.MODID)
public class SimpleCaptureTheFlag {

    public static final String MODID = "simplecapturetheflag";

    public SimpleCaptureTheFlag(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
    }
}
