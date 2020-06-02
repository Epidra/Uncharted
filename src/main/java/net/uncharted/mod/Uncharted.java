package net.uncharted.mod;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Uncharted.modid, version = Uncharted.version, name = Uncharted.modName, dependencies = Uncharted.dependencies)
public class Uncharted {
	
	public static final String modid = "uncharted";
	public static final String version = "v02";
	public static final String modName = "Uncharted";
	public static final String dependencies = "";
	
	@Instance(modid)
	public static Uncharted instance;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent){
		
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event){
		
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){
		MinecraftForge.EVENT_BUS.register(new GuiBiomePanel(Minecraft.getMinecraft()));
	}

}