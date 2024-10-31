package ru.kelcuprum.alinlib;

import org.meteordev.starscript.value.ValueMap;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import ru.kelcuprum.alinlib.api.KeyMappingHelper;
import ru.kelcuprum.alinlib.api.events.alinlib.AlinLibEvents;
import ru.kelcuprum.alinlib.api.events.client.ClientLifecycleEvents;
import ru.kelcuprum.alinlib.api.events.alinlib.LocalizationEvents;
import ru.kelcuprum.alinlib.api.events.client.ClientTickEvents;
import ru.kelcuprum.alinlib.config.Config;
import ru.kelcuprum.alinlib.config.Localization;
import ru.kelcuprum.alinlib.config.parser.StarScript;
import ru.kelcuprum.alinlib.gui.GuiUtils;
import ru.kelcuprum.alinlib.gui.styles.FlatStyle;
import ru.kelcuprum.alinlib.gui.styles.ModernStyle;
import ru.kelcuprum.alinlib.gui.toast.ToastBuilder;

import java.time.LocalDate;
import java.util.Random;

import static ru.kelcuprum.alinlib.gui.Icons.CLOWNFISH;
//#if NEOFORGE
//$$ @net.neoforged.fml.common.Mod("alinlib")
//#endif
public class AlinLib
    //#if FABRIC
        implements net.fabricmc.api.ClientModInitializer
    //#endif
{
    public static final String MODID = "alinlib";
    public static String VERSION = "alinlib";
    public static final AlinLogger LOG = new AlinLogger("AlinaLib");
    public static Config bariumConfig = new Config("config/AlinLib/config.json");
    public static Localization localization = new Localization("alinlib","config/AlinLib/lang");
    public static Minecraft MINECRAFT = Minecraft.getInstance();
    public static StarScript starScript;

    // Init
    public static void init() {
        starScript = new StarScript();
        GuiUtils.registerStyle(new FlatStyle());
        GuiUtils.registerStyle(new ModernStyle());
        KeyMapping toggleStealth = KeyMappingHelper.register(new KeyMapping(
                "alinlib.key.stealth",
                GLFW.GLFW_KEY_UNKNOWN,
                "alinlib"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            assert client.player != null;
            while (toggleStealth.consumeClick())
                bariumConfig.setBoolean("STREAMER.STEALTH", !bariumConfig.getBoolean("STREAMER.STEALTH", false));
        });

        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            LocalizationEvents.DEFAULT_PARSER_INIT.invoker().onParserInit(starScript);
            LOG.log(String.format("Client started. MC Version: %s", client.getLaunchedVersion()));
        });
        ClientLifecycleEvents.CLIENT_FULL_STARTED.register((client) -> {
            LOG.log(String.format("Client full started. MC Version: %s", client.getLaunchedVersion()));
            aprilFool();
            isHBKel();
        });
        ClientLifecycleEvents.CLIENT_STOPPING.register((client) -> {
            LOG.log(Component.translatable("alinlib.log.exit.first"));
            LOG.log(Component.translatable("alinlib.log.exit.two"));
        });
        LocalizationEvents.DEFAULT_PARSER_INIT.register(parser -> parser.ss.set("alinlib", new ValueMap()
                .set("id", MODID)
                .set("version", VERSION))
        );
        AlinLibEvents.INIT.invoker().onInit();
    }
    //#if FABRIC
    @Override
    public void onInitializeClient() {
        AlinLib.VERSION = net.fabricmc.loader.api.FabricLoader.getInstance().getModContainer(AlinLib.MODID).get().getMetadata().getVersion().getFriendlyString();
        init();
    }
    //#elseif NEOFORGE
    //$$  public AlinLib(){
    //$$      init();
    //$$      for(net.neoforged.neoforgespi.language.IModInfo mod : net.neoforged.fml.ModList.get().getMods()){
    //$$          if(mod.getModId().equals(AlinLib.MODID)){
    //$$              AlinLib.VERSION = mod.getVersion().getQualifier();
    //$$          }
    //$$      }
    //$$      if (net.neoforged.fml.loading.FMLLoader.getDist() == net.neoforged.api.distmarker.Dist.CLIENT) {
    //$$          net.neoforged.fml.ModLoadingContext.get().registerExtensionPoint(
    //$$                  net.neoforged.neoforge.client.gui.IConfigScreenFactory.class,
    //$$                  () -> (minecraftClient, screen) -> ru.kelcuprum.alinlib.gui.config.DesignScreen.build(screen));
    //$$          final net.neoforged.bus.api.IEventBus bus = net.neoforged.fml.ModLoadingContext.get().getActiveContainer().getEventBus();
    //$$          bus.addListener(this::registerBindings);
    //$$          net.neoforged.neoforge.common.NeoForge.EVENT_BUS.addListener(this::onPostRenderGui);
    //$$      }
    //$$  }
    //$$  @net.neoforged.bus.api.SubscribeEvent
    //$$  public void registerBindings(net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent event) {
    //$$      for(KeyMapping mapping : KeyMappingHelper.EXAMPLE_MAPPING) {
    //$$          event.register(mapping);
    //$$      }
    //$$  }
    //#endif

    //#if NEOFORGE && MC >= 12100
    //$$  public void onPostRenderGui(net.neoforged.neoforge.client.event.RenderGuiEvent.Post event) {
    //$$      ru.kelcuprum.alinlib.api.events.client.GuiRenderEvents.RENDER.invoker().onRender(event.getGuiGraphics(), event.getPartialTick().getGameTimeDeltaTicks());
    //$$  }
    //#elseif NEOFORGE && MC < 12100
    //$$  public void onPostRenderGui(net.neoforged.neoforge.client.event.RenderGuiEvent.Post event) {
    //$$      ru.kelcuprum.alinlib.api.events.client.GuiRenderEvents.RENDER.invoker().onRender(event.getGuiGraphics(), event.getPartialTick());
    //$$  }
    //#endif

    // Funny
    public static boolean isAprilFool(){
        return LocalDate.now().getMonthValue() == 4 && LocalDate.now().getDayOfMonth() == 1;
    }
    public static void aprilFool(){
        if(isAprilFool()){
            String[] types = {
                    "white",
                    "welcome",
                    "clownfish"
            };
            String type = types[new Random().nextInt(types.length)];
            new ToastBuilder()
                    .setIcon(CLOWNFISH)
                    .setTitle(Component.literal("AlinLib"))
                    .setMessage(Component.translatable("alinlib.april_fools."+type))
                    .setType(ToastBuilder.Type.WARN)
                    .buildAndShow();
        }
    }
    public static void isHBKel(){
        if(LocalDate.now().getMonthValue() == 4 && LocalDate.now().getDayOfMonth() == 8){
            if(bariumConfig.getBoolean("KEL_HB_"+LocalDate.now().getYear(), false)) return;
            bariumConfig.setBoolean("KEL_HB_"+LocalDate.now().getYear(), true);
            new ToastBuilder()
                    .setIcon(Items.CAKE)
                    .setTitle(Component.literal("AlinLib"))
                    .setMessage(Component.translatable("alinlib.hb"))
                    .buildAndShow();
        }
    }
}
