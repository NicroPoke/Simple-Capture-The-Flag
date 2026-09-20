package net.surya.ctfmod.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.surya.ctfmod.CtfMod;

public final class ModCreativeTabs {

    private ModCreativeTabs() {
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CtfMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> CTF_TAB =
            CREATIVE_MODE_TABS.register("ctf_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.ctfmod"))
                    .icon(() -> new ItemStack(ModItems.RED_POINT_ITEM.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.RED_POINT_ITEM.get());
                        output.accept(ModItems.BLUE_POINT_ITEM.get());
                    })
                    .build());
}
