package mod.uncharted;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.nio.file.Paths;
import java.util.stream.Collectors;

@Mod("uncharted")
public class Uncharted {

    /** The Mod ID */
    public static final String MODID = "uncharted";



    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor */
    public Uncharted() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        MinecraftForge.EVENT_BUS.register(this);
        UnchartedConfig.init();
    }



    //----------------------------------------EVENTS----------------------------------------//

    /** Client Side Setup Event */
    private void doClientStuff(final FMLClientSetupEvent event) {
        UnchartedConfig.load();
        MinecraftForge.EVENT_BUS.register(new GuiBiomePanel(Minecraft.getInstance()));
    }
}
