package net.surya.ctfmod.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.surya.ctfmod.CtfMod;

public final class ModItems {

    private ModItems() {
    }

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, CtfMod.MOD_ID);

    public static final RegistryObject<Item> RED_POINT_ITEM = ITEMS.register("red_point",
            () -> new BlockItem(ModBlocks.RED_POINT.get(), new Item.Properties()));

    public static final RegistryObject<Item> BLUE_POINT_ITEM = ITEMS.register("blue_point",
            () -> new BlockItem(ModBlocks.BLUE_POINT.get(), new Item.Properties()));
}
