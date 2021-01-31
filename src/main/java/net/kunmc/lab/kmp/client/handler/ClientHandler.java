package net.kunmc.lab.kmp.client.handler;

import net.kunmc.lab.kmp.client.config.ClientConfig;
import net.kunmc.lab.kmp.client.gui.widget.KMPSoundSlider;
import net.kunmc.lab.kmp.client.music.ClientWorldMusicManager;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.OptionsSoundsScreen;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientHandler {
    @SubscribeEvent
    public static void onGUIInit(GuiScreenEvent.InitGuiEvent.Post e) {
        if (e.getGui() instanceof OptionsSoundsScreen) {
            e.addWidget(new KMPSoundSlider(e.getGui().width / 2 - 155 + 160, e.getGui().height / 6 - 12 + 24 * 5, 150, 20));
        }
    }

    @SubscribeEvent
    public static void onGUIopen(GuiOpenEvent e) {
        if (e.getGui() instanceof OptionsScreen) {
            ClientConfig.MusicVolume.set(ClientWorldMusicManager.instance().getMusicVolume());
        }
    }
}
