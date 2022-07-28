package mod.uncharted;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("uncharted")
public class Uncharted {

    /** The Mod ID */
    public static final String MODID = "uncharted";





    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor */
    public Uncharted() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.spec);
        MinecraftForge.EVENT_BUS.register(this);
    }





    //----------------------------------------EVENTS----------------------------------------//

    /** Client Side Setup Event */
    private void setupClient(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiBiomePanel(Minecraft.getInstance()));
    }



}
