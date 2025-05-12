package com.jasonxue.cmdreplacer;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.lang.reflect.Field;

@Mod(
    modid = "cmdreplacer",
    name = "Command Replacer",
    clientSideOnly = true,
    version = "1.0"
)
public class CommandReplacer {
    public static KeyBinding openGuiKey;
    private boolean guiOpenRequested = false;

    private boolean returnPressedLast = false;
    private Field inputFieldRef;
    private boolean inputFieldRefInit = false;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent evt) {
        ConfigManager.init(evt.getModConfigurationDirectory());
        ConfigManager.load();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent evt) {
        openGuiKey = new KeyBinding("Open Command Replacer", Keyboard.KEY_R, "Command Replacer");
        ClientRegistry.registerKeyBinding(openGuiKey);

        // 注册事件总线
        MinecraftForge.EVENT_BUS.register(this);
        cpw.mods.fml.common.FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // 打开配置 GUI
        if (openGuiKey.isPressed()) {
            guiOpenRequested = true;
        }
        if (guiOpenRequested) {
            guiOpenRequested = false;
            mc.displayGuiScreen(new GuiCmdReplacer());
        }

        // 拦截聊天发送
        boolean returnPressed = Keyboard.isKeyDown(Keyboard.KEY_RETURN);
        if (returnPressed && !returnPressedLast && mc.currentScreen instanceof GuiChat) {
            GuiChat chat = (GuiChat) mc.currentScreen;
            try {
                if (!inputFieldRefInit) {
                    inputFieldRefInit = true;
                    inputFieldRef = GuiChat.class.getDeclaredField("inputField");
                    inputFieldRef.setAccessible(true);
                }
                GuiTextField inputField = (GuiTextField) inputFieldRef.get(chat);
                String msg = inputField.getText().trim();
                if (msg.startsWith("/")) {
                    for (ConfigManager.Entry e : ConfigManager.getAll()) {
                        String orig = e.original.trim();
                        if (msg.equalsIgnoreCase(orig)
                          || msg.toLowerCase().startsWith(orig.toLowerCase() + " ")) {
                            String suffix = msg.length() > orig.length()
                                ? msg.substring(orig.length()).trim()
                                : "";
                            String replacement = e.replacement
                                + (suffix.isEmpty() ? "" : " " + suffix);
                            mc.thePlayer.sendChatMessage(replacement);
                            // 关闭聊天界面
                            mc.displayGuiScreen(null);
                            break;
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        returnPressedLast = returnPressed;
    }
}
