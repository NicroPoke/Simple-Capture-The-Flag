package net.surya.ctfmod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.surya.ctfmod.CtfMod;
import net.surya.ctfmod.capture.CtfCaptureManager;

@Mod.EventBusSubscriber(modid = CtfMod.MOD_ID)
public final class CtfCommands {

    private static final String[] HELP_KEYS = {
            "ctfmod.help.header",
            "ctfmod.help.setup1",
            "ctfmod.help.setup2",
            "ctfmod.help.setup3",
            "ctfmod.help.commands",
            "ctfmod.help.cmd_time",
            "ctfmod.help.cmd_radius",
            "ctfmod.help.cmd_light",
            "ctfmod.help.cmd_help",
            "ctfmod.help.rules1",
            "ctfmod.help.rules2",
            "ctfmod.help.rules3",
    };

    private CtfCommands() {
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("ctftime")
                .requires(source -> source.hasPermission(2))
                .executes(context -> showTime(context.getSource()))
                .then(Commands.argument("time", IntegerArgumentType.integer(1))
                        .executes(context -> setTime(context.getSource(),
                                IntegerArgumentType.getInteger(context, "time"), 1))
                        .then(Commands.literal("s")
                                .executes(context -> setTime(context.getSource(),
                                        IntegerArgumentType.getInteger(context, "time"), 1)))
                        .then(Commands.literal("m")
                                .executes(context -> setTime(context.getSource(),
                                        IntegerArgumentType.getInteger(context, "time"), 60)))));

        dispatcher.register(Commands.literal("ctflight")
                .requires(source -> source.hasPermission(2))
                .executes(context -> showLightStatus(context.getSource()))
                .then(Commands.literal("on")
                        .executes(context -> setLight(context.getSource(), true)))
                .then(Commands.literal("off")
                        .executes(context -> setLight(context.getSource(), false))));

        dispatcher.register(Commands.literal("ctfradius")
                .requires(source -> source.hasPermission(2))
                .executes(context -> showRadius(context.getSource()))
                .then(Commands.argument("radius", IntegerArgumentType.integer(0, 64))
                        .executes(context -> setRadius(context.getSource(),
                                IntegerArgumentType.getInteger(context, "radius")))));

        dispatcher.register(Commands.literal("ctfhelp")
                .executes(context -> showHelp(context.getSource())));
    }

    private static int setTime(CommandSourceStack source, int value, int multiplier) {
        int seconds = value * multiplier;
        CtfCaptureManager.setCaptureSeconds(seconds);
        source.sendSuccess(() -> Component.translatable("ctfmod.cmd.ctime_set",
                seconds, formatTime(seconds)), true);
        return seconds;
    }

    private static int showTime(CommandSourceStack source) {
        int seconds = CtfCaptureManager.getCaptureSeconds();
        source.sendSuccess(() -> Component.translatable("ctfmod.cmd.ctime_current",
                seconds, formatTime(seconds)), false);
        return seconds;
    }

    private static int setLight(CommandSourceStack source, boolean enabled) {
        CtfCaptureManager.setBeamEnabled(enabled);
        source.sendSuccess(() -> Component.translatable(enabled
                ? "ctfmod.cmd.light_on" : "ctfmod.cmd.light_off"), true);
        return 1;
    }

    private static int showLightStatus(CommandSourceStack source) {
        boolean enabled = CtfCaptureManager.isBeamEnabled();
        source.sendSuccess(() -> Component.translatable("ctfmod.cmd.light_status",
                Component.translatable(enabled ? "ctfmod.cmd.state.on" : "ctfmod.cmd.state.off")), false);
        return 1;
    }

    private static int setRadius(CommandSourceStack source, int radius) {
        CtfCaptureManager.setCaptureRadius(radius);
        source.sendSuccess(() -> Component.translatable("ctfmod.cmd.radius_set",
                CtfCaptureManager.getCaptureRadius()), true);
        return CtfCaptureManager.getCaptureRadius();
    }

    private static int showRadius(CommandSourceStack source) {
        source.sendSuccess(() -> Component.translatable("ctfmod.cmd.radius_current",
                CtfCaptureManager.getCaptureRadius()), false);
        return CtfCaptureManager.getCaptureRadius();
    }

    private static int showHelp(CommandSourceStack source) {
        for (String key : HELP_KEYS) {
            source.sendSuccess(() -> Component.translatable(key), false);
        }
        return HELP_KEYS.length;
    }

    private static String formatTime(int totalSeconds) {
        return String.format("%d:%02d", totalSeconds / 60, totalSeconds % 60);
    }
}
