package com.jasonxue.cmdreplacer;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

@Mod(
    modid = "cmdreplacer",
    name = "Command Replacer",
    clientSideOnly = true,
    version = "1.0"
)
public class CommandReplacer {
    public static KeyBinding openGuiKey;
    private boolean guiOpenRequested = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        ConfigManager.init(evt.getModConfigurationDirectory());
        ConfigManager.load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {
        openGuiKey = new KeyBinding("Open Command Replacer", Keyboard.KEY_R, "Command Replacer");
        ClientRegistry.registerKeyBinding(openGuiKey);
        net.minecraftforge.fml.common.FMLCommonHandler.instance().bus().register(this);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        if (openGuiKey.isPressed()) {
            guiOpenRequested = true;
        }
        if (guiOpenRequested) {
            guiOpenRequested = false;
            net.minecraft.client.Minecraft.getMinecraft()
                .displayGuiScreen(new GuiCmdReplacer());
        }
    }

    @SubscribeEvent
    public void onChatSend(ClientChatEvent event) {
        String msg = event.getMessage().trim();
        if (!msg.startsWith("/")) return;

        for (ConfigManager.Entry e : ConfigManager.getAll()) {
            String orig = e.original.trim();
            if (msg.equalsIgnoreCase(orig) ||
                msg.toLowerCase().startsWith(orig.toLowerCase() + " ")) {
                String suffix = msg.length() > orig.length()
                    ? msg.substring(orig.length()).trim()
                    : "";
                String replacement = e.replacement
                    + (suffix.isEmpty() ? "" : " " + suffix);
                event.setMessage(replacement);
                break;
            }
        }
    }
}
