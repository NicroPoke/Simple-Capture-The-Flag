package net.surya.ctfmod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.surya.ctfmod.network.ModNetwork;
import net.surya.ctfmod.registry.ModBlockEntities;
import net.surya.ctfmod.registry.ModBlocks;
import net.surya.ctfmod.registry.ModCreativeTabs;
import net.surya.ctfmod.registry.ModItems;

@Mod(CtfMod.MOD_ID)
public class CtfMod {

    public static final String MOD_ID = "ctfmod";

    public CtfMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        ModNetwork.register();
    }
}
