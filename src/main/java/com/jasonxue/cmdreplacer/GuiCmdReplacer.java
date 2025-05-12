package com.jasonxue.cmdreplacer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import java.io.IOException;
import java.util.List;

public class GuiCmdReplacer extends GuiScreen {
    private int selected = -1;
    private List<ConfigManager.Entry> list;

    @Override
    public void initGui() {
        list = ConfigManager.getAll();
        buttonList.clear();
        int y = height / 4;
        buttonList.add(new GuiButton(0, width / 2 - 100, height - 52, "增加"));
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 28, "编辑"));
        buttonList.add(new GuiButton(2, width / 2 + 0,   height - 52, "删除"));
        buttonList.add(new GuiButton(3, width / 2 + 0,   height - 28, "完成"));
    }

    @Override
    public void drawScreen(int mx, int my, float partialTicks) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, "Command Replacer 配置", width / 2, 20, 0xFFFFFF);
        int startY = 40;
        for (int i = 0; i < list.size(); i++) {
            ConfigManager.Entry e = list.get(i);
            int y = startY + i * 12;
            int color = (i == selected ? 0xFFFFA0 : 0xFFFFFF);
            drawString(fontRendererObj,
                       String.format("%s → %s", e.original, e.replacement),
                       width / 2 - 100, y, color);
        }
        super.drawScreen(mx, my, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton btn) throws IOException {
        if (btn.id == 0) {
            mc.displayGuiScreen(new GuiEntryEdit(-1));
        } else if (btn.id == 1 && selected >= 0) {
            mc.displayGuiScreen(new GuiEntryEdit(selected));
        } else if (btn.id == 2 && selected >= 0) {
            ConfigManager.remove(selected);
            selected = -1;
        } else if (btn.id == 3) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
