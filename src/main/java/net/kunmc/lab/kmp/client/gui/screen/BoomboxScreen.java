package net.kunmc.lab.kmp.client.gui.screen;

import net.kunmc.lab.kmp.KatyouMovablePlayer;
import net.kunmc.lab.kmp.client.util.RenderUtil;
import net.kunmc.lab.kmp.data.KMPWorldData;
import net.kunmc.lab.kmp.data.ResponseSender;
import net.kunmc.lab.kmp.item.KMPItems;
import net.kunmc.lab.kmp.music.BoomboxMode;
import net.kunmc.lab.kmp.proxy.CommonProxy;
import net.kunmc.lab.kmp.util.MusicUtils;
import net.kunmc.lab.kmp.util.StringUtils;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BoomboxScreen extends Screen {
    public static final ResourceLocation BOOMBOX_GUI_TEXTURES = new ResourceLocation(KatyouMovablePlayer.MODID, "textures/gui/boombox.png");
    private final PlayerEntity editingPlayer;
    private final CommonProxy.IWhetherItem stack;
    private final Hand hand;
    private TextFieldWidget musicURLTextField;
    private BoomboxButton pauseButton;
    private BoomboxButton stopButton;
    private BoomboxButton playButton;
    private BoomboxButton loopButton;
    private String url;
    private boolean musicLoading;
    private MusicURLCheckThread musicURLCheckThread;
    private boolean musicResult;

    public BoomboxScreen(PlayerEntity player, CommonProxy.IWhetherItem stack, Hand handIn) {
        super(NarratorChatListener.EMPTY);
        this.editingPlayer = player;
        this.stack = stack;
        this.hand = handIn;
    }

    @Override
    protected void init() {
        super.init();

        musicResult = !getStackMusicURL().isEmpty();

        this.pauseButton = addBoomboxButton(75, 44, 0, n -> {
            if (!getStackMusicURL().isEmpty() && getMode() == BoomboxMode.PLAY)
                insMode(BoomboxMode.PAUSE);
        }, () -> getMode() == BoomboxMode.PAUSE);

        this.stopButton = addBoomboxButton(97, 44, 1, n -> insStop());

        this.playButton = addBoomboxButton(119, 44, 2, n -> {
            if (!getStackMusicURL().isEmpty() && musicResult)
                insMode(BoomboxMode.PLAY);
        }, () -> getMode() == BoomboxMode.PLAY);

        int btsx = (width - 214) / 2;
        int btsy = (height - 90) / 2;

        String MusicURLTextField = getStackMusicURL();

        if (MusicURLTextField.isEmpty())
            MusicURLTextField = "";

        if (this.musicURLTextField != null)
            MusicURLTextField = this.musicURLTextField.getText();

        this.musicURLTextField = this.addButton(new TextFieldWidget(this.font, btsx + 40, btsy + 70, 142, 12, "url"));
        this.musicURLTextField.setEnableBackgroundDrawing(false);
        this.musicURLTextField.setMaxStringLength(Integer.MAX_VALUE);
        this.musicURLTextField.setTextColor(-1);
        this.musicURLTextField.setDisabledTextColour(-1);
        this.musicURLTextField.setText(MusicURLTextField);
        this.musicURLTextField.setResponder(n -> {
            if (this.musicURLCheckThread != null)
                this.musicURLCheckThread.setStop(true);
            insStop();
            this.musicURLCheckThread = new MusicURLCheckThread(n);
            this.musicURLCheckThread.start();
        });
    }

    private void insStop() {
        insMode(BoomboxMode.NONE, true);
    }

    protected void insMode(BoomboxMode mode) {
        insMode(mode, false);
    }

    protected void insMode(BoomboxMode mode, boolean stop) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("hand", hand == Hand.MAIN_HAND);
        ResponseSender.sendToServer(KMPWorldData.BOOMBOX_INS, 0, mode.getName(), tag);

        if (mode == BoomboxMode.PLAY)
            ResponseSender.sendToServer(KMPWorldData.BOOMBOX_INS, 2, "", tag);

        if (stop)
            ResponseSender.sendToServer(KMPWorldData.BOOMBOX_INS, 3, "", tag);
    }

    protected void insMusicURL(String url, long duration) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("hand", hand == Hand.MAIN_HAND);
        tag.putLong("duration", duration);
        ResponseSender.sendToServer(KMPWorldData.BOOMBOX_INS, 1, url, tag);
    }

    protected String getStackMusicURL() {
        if (getBoomboxStack().getTag() != null) {
            return getBoomboxStack().getTag().getString("MusicURL");
        }
        return "";
    }

    public ItemStack getBoomboxStack() {
        return stack.getStack();
    }

    private long getMusicDuration() {
        CompoundNBT tag = getBoomboxStack().getTag();
        if (tag != null) {
            return tag.getLong("Duration");
        }
        return 0;
    }

    private long getCurrentMusicPlayPosition() {
        CompoundNBT tag = getBoomboxStack().getTag();
        if (tag != null) {
            return tag.getLong("CurrentPosition");
        }
        return 0;
    }

    private BoomboxMode getMode() {
        CompoundNBT tag = getBoomboxStack().getTag();
        if (tag != null) {
            return BoomboxMode.getModeByName(tag.getString("Mode"));
        }
        return BoomboxMode.NONE;
    }


    @Override
    public void render(int mouseX, int mouseY, float parTick) {
        this.renderBackground();
        int btsx = (width - 214) / 2;
        int btsy = (height - 90) / 2;
        RenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, btsx, btsy, 0, 0, 214, 90, 256, 256);


        if (musicLoading) {
            drawGreenCenterString("urlcheack", 108, 25);
        } else if (!musicResult && musicURLTextField.getText().isEmpty()) {
            drawGreenCenterString("urlempty", 108, 25);
        } else if (!musicResult) {
            drawGreenCenterString("notavailable", 108, 25);
        } else {
            drawNlGreenCenterString(StringUtils.getTimeNotationPercentage(getCurrentMusicPlayPosition(), getMusicDuration()), 108, 25);
        }

        if (musicURLTextField.getText().isEmpty()) {
            RenderUtil.drawString(font, I18n.format("boombox.plaseurl"), btsx + 40, btsy + 70, 11119017);
        }
        this.font.drawString(new TranslationTextComponent(KMPItems.BOOMBOX.getTranslationKey()).getFormattedText(), btsx + 42f + 8.0F, btsy + 6.0F, 4210752);
        super.render(mouseX, mouseY, parTick);
    }

    private void drawNlGreenCenterString(String str, int x, int y) {
        int btsx = (width - 214) / 2;
        int btsy = (height - 90) / 2;
        RenderUtil.drawCenterString(font, str, btsx + x, btsy + y, 2722312);
    }

    private void drawGreenCenterString(String str, int x, int y) {
        int btsx = (width - 214) / 2;
        int btsy = (height - 90) / 2;
        RenderUtil.drawCenterString(font, I18n.format("boombox." + str), btsx + x, btsy + y, 2722312);
    }


    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private BoomboxButton addBoomboxButton(int x, int y, int btnnum, Button.IPressable pressedAction) {
        return addBoomboxButton(x, y, btnnum, pressedAction, () -> false);
    }

    private BoomboxButton addBoomboxButton(int x, int y, int btnnum, Button.IPressable pressedAction, BoomboxButton.IWhetherPresseble whetherpresseble) {
        int btsx = (width - 214) / 2;
        int btsy = (height - 90) / 2;
        return this.addButton(new BoomboxButton(btsx + x, btsy + y, btnnum, pressedAction::onPress, whetherpresseble));
    }

    private static class BoomboxButton extends Button {
        private final int buttunNum;
        private final IWhetherPresseble whetherpresseble;

        public BoomboxButton(int x, int y, int buttonNum, IPressable pressedAction, IWhetherPresseble whetherpresseble) {
            super(x, y, 22, 17, "narrator.button.boombox", pressedAction);
            this.buttunNum = buttonNum;
            this.whetherpresseble = whetherpresseble;
        }

        @Override
        public void renderButton(int mouseX, int mouseY, float partialTicks) {
            RenderUtil.guiBindAndBlit(BOOMBOX_GUI_TEXTURES, this.x, this.y, buttunNum * 22, (this.isHovered() ? 17 : 0) + (whetherpresseble.isPresseble() ? 34 : 0) + 90, 22, 17, 256, 256);

        }

        @OnlyIn(Dist.CLIENT)
        public interface IWhetherPresseble {
            boolean isPresseble();
        }
    }

    private class MusicURLCheckThread extends Thread {
        private final String source;
        private boolean stop;

        public MusicURLCheckThread(String source) {
            this.setName("Music URL Check");
            this.source = source;
        }

        public boolean isStop() {
            return stop;
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public void run() {
            if (this.stop || source.isEmpty())
                return;
            musicLoading = true;
            musicResult = false;
            long duration = 0;
            try {
                URL url = new URL(source);
                HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
                httpConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36");
                InputStream stream = httpConnection.getInputStream();
                duration = MusicUtils.getMP3Duration(stream, httpConnection.getContentLength());
            } catch (Exception ex) {
                if (!this.stop) {
                    musicResult = false;
                    musicLoading = false;
                    return;
                }
            }
            if (!this.stop) {
                musicResult = true;
                musicLoading = false;
                insMusicURL(source, duration);
            }
        }
    }

    private NonNullList<ItemStack> getPlayerItems() {
        PlayerInventory inventory = getMinecraft().player.inventory;
        NonNullList<ItemStack> il = NonNullList.create();
        il.addAll(inventory.mainInventory);
        il.addAll(inventory.armorInventory);
        il.addAll(inventory.offHandInventory);
        return il;
    }

    @Override
    public void tick() {
        super.tick();
        if (getPlayerItems().stream().filter(n -> n.getItem() == KMPItems.BOOMBOX).count() != 1) {
            onClose();
        }
    }

    @Override
    public boolean keyPressed(int p_keyPressed_1_, int p_keyPressed_2_, int p_keyPressed_3_) {
        InputMappings.Input mouseKey = InputMappings.getInputByCode(p_keyPressed_1_, p_keyPressed_2_);
        if (!musicURLTextField.canWrite() && (p_keyPressed_1_ == 256 || this.minecraft.gameSettings.keyBindInventory.isActiveAndMatches(mouseKey))) {
            onClose();
            return true;
        }
        return super.keyPressed(p_keyPressed_1_, p_keyPressed_2_, p_keyPressed_3_);
    }
}
