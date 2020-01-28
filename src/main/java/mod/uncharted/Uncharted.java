package mod.uncharted;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = Uncharted.modid, version = Uncharted.version, name = Uncharted.modName, dependencies = Uncharted.dependencies)
public class Uncharted {
	
	public static final String modid        = "uncharted";
	public static final String version      = "v04";
	public static final String modName      = "Uncharted";
	public static final String dependencies = "";
	
	@Instance(modid)
	public static Uncharted instance;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent){
		
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		if(event.getSide() == Side.CLIENT)
		MinecraftForge.EVENT_BUS.register(new GuiBiomePanel(Minecraft.getMinecraft()));
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent) {
		Debug.createLog();
	}
	
}
