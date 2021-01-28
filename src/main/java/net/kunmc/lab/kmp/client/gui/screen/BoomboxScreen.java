package net.kunmc.lab.kmp.client.gui.screen;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.TranslationTextComponent;

public class BoomboxScreen extends Screen {
    protected BoomboxScreen() {
        super(new TranslationTextComponent("boombox.title"));
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float parTick) {
        this.renderBackground();
        super.render(mouseX, mouseY, parTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
