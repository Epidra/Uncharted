package net.uncharted.mod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = Uncharted.modid, version = Uncharted.version, name = Uncharted.modName, dependencies = Uncharted.dependencies)
public class Uncharted {
	
	public static final String modid = "Uncharted";
	public static final String version = "v03";
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