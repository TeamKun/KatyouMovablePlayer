package net.kunmc.lab.kmp;

import net.kunmc.lab.kmp.client.config.ClientConfig;
import net.kunmc.lab.kmp.client.handler.ClientHandler;
import net.kunmc.lab.kmp.client.handler.MusicRingerHandler;
import net.kunmc.lab.kmp.client.handler.RenderHandler;
import net.kunmc.lab.kmp.client.music.ClientWorldMusicManager;
import net.kunmc.lab.kmp.client.music.MusicThread;
import net.kunmc.lab.kmp.handler.ServerHandler;
import net.kunmc.lab.kmp.handler.WorldRingerHandler;
import net.kunmc.lab.kmp.music.ServerWorldMusicManager;
import net.kunmc.lab.kmp.packet.PacketHandler;
import net.kunmc.lab.kmp.proxy.ClientProxy;
import net.kunmc.lab.kmp.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(KatyouMovablePlayer.MODID)
public class KatyouMovablePlayer {
    public static final String MODID = "katyoumovableplayer";
    public static final CommonProxy proxy = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public KatyouMovablePlayer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        ClientConfig.init();
    }

    private void setup(final FMLCommonSetupEvent event) {
        KMPGamerules.register();
        PacketHandler.init();
        ServerWorldMusicManager.init();
        MinecraftForge.EVENT_BUS.register(ServerHandler.class);
        MinecraftForge.EVENT_BUS.register(WorldRingerHandler.class);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MusicThread.startThread();
        ClientWorldMusicManager.init();
        MinecraftForge.EVENT_BUS.register(MusicRingerHandler.class);
        MinecraftForge.EVENT_BUS.register(RenderHandler.class);
        MinecraftForge.EVENT_BUS.register(ClientHandler.class);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }
}
