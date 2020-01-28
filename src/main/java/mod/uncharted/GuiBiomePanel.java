package mod.uncharted;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiBiomePanel extends Gui {
	
	private Minecraft mc;
	  Biome biomePlayer;
	  Biome biomePanel;
	  boolean transition;
	  boolean transition_up;
	  int timer;
	  int posX;
	  int posY;
	  int timerMax;
	  ResourceLocation textureBiome;

	  public GuiBiomePanel(Minecraft mc) {
	    super();
	    this.mc = mc;
	    biomePlayer = Biomes.HELL;
	    biomePanel  = Biomes.HELL;
	    transition = false;
	    transition_up = true;
	    timer = 0;
	    timerMax = 150;
	    posX  = this.mc.displayWidth - 2;
	    posY  = 0;
	    textureBiome = new ResourceLocation(Uncharted.modid + ":" + "textures/Uncharted.png");
	  }
	  
	  @SubscribeEvent
		public void OnTravel(LivingUpdateEvent event){
			if(event.getEntity() instanceof EntityPlayer){
				if(event.getEntity().dimension == 0){
					if(event.getEntity().world.canBlockSeeSky(event.getEntity().getPosition())){
						if(Biome.getIdForBiome(biomePlayer) != Biome.getIdForBiome(event.getEntity().world.getBiome(event.getEntity().getPosition()))){
							biomePlayer = event.getEntity().world.getBiome(event.getEntity().getPosition());
						}
					}
				} else {
					if(Biome.getIdForBiome(biomePlayer) != Biome.getIdForBiome(event.getEntity().world.getBiome(event.getEntity().getPosition()))){
						biomePlayer = event.getEntity().world.getBiome(event.getEntity().getPosition());
					}
				}
			}
		}
	  
	  @SubscribeEvent
	  public void onRenderExperienceBar(RenderGameOverlayEvent event){
	    if(event.isCancelable() || event.getType() != ElementType.HELMET){      
	      return;
	    }
	    if(biomePanel != biomePlayer){
	    	biomePanel = biomePlayer;
	    	transition = true;
	    	transition_up = true;
	    	textureBiome = new ResourceLocation(Uncharted.modid + ":" + "textures/" + biomePanel.getBiomeName() + ".png");
	    }
	    
	    if(transition){
	    	if(transition_up){
	    		if(timer >= timerMax){
	    			timer = timerMax;
	    			transition_up = false;
	    		} else {
	    			timer++;
	    		}
	    	} else {
	    		if(timer <= 0){
	    			timer = 0;
	    			transition_up = true;
	    			transition = false;
	    		} else {
	    			timer--;
	    		}
	    	}
	    }
	    
	    if(transition){
	    	ScaledResolution var5 = new ScaledResolution(this.mc);
	        //posX = 10;
	    	posX = var5.getScaledWidth()-10;
	        posY = var5.getScaledHeight();
	        int u = posY-timer/2;
	        if(timer > 130){
	        	u = posY-130/2;
	        }
	        //posY = -64;
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        GL11.glDisable(GL11.GL_LIGHTING);
			ITextureObject texture = null;
	    	try {
				texture = Minecraft.getMinecraft().renderEngine.getTexture(textureBiome);
			} catch (Exception e){

			}
	    	if(texture instanceof DynamicTexture){
	    		textureBiome = new ResourceLocation(Uncharted.modid + ":" + "textures/Uncharted.png");
	    		this.mc.renderEngine.bindTexture(textureBiome);
	            this.drawTexturedModalRect(posX-120, u, 0, 0, 120, 64);
	    	}else if(texture == null){
	    		this.mc.renderEngine.bindTexture(textureBiome);
	            this.drawTexturedModalRect(posX-120, u, 0, 0, 120, 64);
	    	}else{
	    		this.mc.renderEngine.bindTexture(textureBiome);
	            this.drawTexturedModalRect(posX-120, u, 0, 0, 120, 64);
	    	}
	    }
	  }
	
}
