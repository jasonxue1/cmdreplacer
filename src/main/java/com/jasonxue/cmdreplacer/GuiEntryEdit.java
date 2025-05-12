package com.jasonxue.cmdreplacer;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import java.io.IOException;

public class GuiEntryEdit extends GuiScreen {
    private GuiTextField origField, replField;
    private int editIndex;

    public GuiEntryEdit(int idx) {
        this.editIndex = idx;
    }

    @Override
    public void initGui() {
        origField = new GuiTextField(0, fontRendererObj, width/2-100, height/2-20, 200, 20);
        replField = new GuiTextField(1, fontRendererObj, width/2-100, height/2+5, 200, 20);
        if (editIndex >= 0) {
            ConfigManager.Entry e = ConfigManager.getAll().get(editIndex);
            origField.setText(e.original);
            replField.setText(e.replacement);
        }
        buttonList.clear();
        buttonList.add(new GuiButton(0, width/2-100, height/2+35, "保存"));
        buttonList.add(new GuiButton(1, width/2+0,   height/2+35, "取消"));
    }

    @Override
    public void drawScreen(int mx, int my, float pt) {
        drawDefaultBackground();
        drawCenteredString(fontRendererObj, editIndex<0?"新增映射":"编辑映射", width/2, height/2-40, 0xFFFFFF);
        drawString(fontRendererObj, "原 指 令 (带 /):", width/2-100, height/2-30, 0xAAAAAA);
        origField.drawTextBox();
        drawString(fontRendererObj, "替 换 指 令:", width/2-100, height/2-5, 0xAAAAAA);
        replField.drawTextBox();
        super.drawScreen(mx, my, pt);
    }

    @Override
    protected void actionPerformed(GuiButton btn) throws IOException {
        if (btn.id == 0) {
            String o = origField.getText().trim();
            String r = replField.getText().trim();
            if (!o.isEmpty() && !r.isEmpty()) {
                ConfigManager.Entry e = new ConfigManager.Entry(o, r);
                if (editIndex < 0) ConfigManager.add(e);
                else               ConfigManager.update(editIndex, e);
            }
            mc.displayGuiScreen(new GuiCmdReplacer());
        } else {
            mc.displayGuiScreen(new GuiCmdReplacer());
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    protected void keyTyped(char c, int key) throws IOException {
        origField.textboxKeyTyped(c, key);
        replField.textboxKeyTyped(c, key);
    }

    @Override
    protected void mouseClicked(int mx, int my, int btn) throws IOException {
        origField.mouseClicked(mx, my, btn);
        replField.mouseClicked(mx, my, btn);
        super.mouseClicked(mx, my, btn);
    }
}
