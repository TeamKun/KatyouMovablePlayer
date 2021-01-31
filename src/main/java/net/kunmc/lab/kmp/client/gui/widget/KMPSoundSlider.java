package net.kunmc.lab.kmp.client.gui.widget;

import net.kunmc.lab.kmp.client.music.ClientWorldMusicManager;
import net.minecraft.client.gui.widget.AbstractSlider;
import net.minecraft.client.resources.I18n;

public class KMPSoundSlider extends AbstractSlider {

    public KMPSoundSlider(int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn, ClientWorldMusicManager.instance().getMusicVolume() / 2);
        this.updateMessage();
    }

    @Override
    protected void updateMessage() {
        String text = this.value == 0d ? I18n.format("options.off") : this.value >= 1d ? I18n.format("options.bigsugi") : ((int) (this.value * 100.0D * 2) + "%");
        this.setMessage(I18n.format("soundCategory.musicPlayer") + ": " + text);
    }

    @Override
    protected void applyValue() {
        ClientWorldMusicManager.instance().setMusicVolume(this.value * 2);
    }

}