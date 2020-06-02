package net.uncharted.mod;

import java.util.Collection;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class GuiBiomePanel extends Gui {
	
  private Minecraft mc;
  BiomeGenBase biomePlayer;
  BiomeGenBase biomePanel;
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
    biomePlayer = BiomeGenBase.hell;
    biomePanel  = BiomeGenBase.hell;
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
		if(event.entity instanceof EntityPlayer){
			if(event.entity.dimension == 0){
				if(event.entity.worldObj.canBlockSeeTheSky((int)event.entity.posX, (int)event.entity.posY, (int)event.entity.posZ)){
					if(biomePlayer.biomeID != event.entity.worldObj.getBiomeGenForCoords((int)event.entity.posX, (int)event.entity.posZ).biomeID){
						biomePlayer = event.entity.worldObj.getBiomeGenForCoords((int)event.entity.posX, (int)event.entity.posZ);
					}
				}
			} else {
				if(biomePlayer.biomeID != event.entity.worldObj.getBiomeGenForCoords((int)event.entity.posX, (int)event.entity.posZ).biomeID){
					biomePlayer = event.entity.worldObj.getBiomeGenForCoords((int)event.entity.posX, (int)event.entity.posZ);
				}
			}
		}
	}
  
  @SubscribeEvent
  public void onRenderExperienceBar(RenderGameOverlayEvent event){
    if(event.isCancelable() || event.type != ElementType.HELMET){      
      return;
    }
    
    if(biomePanel != biomePlayer){
    	biomePanel = biomePlayer;
    	transition = true;
    	transition_up = true;
    	textureBiome = new ResourceLocation(Uncharted.modid + ":" + "textures/" + biomePanel.biomeName + ".png");
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
    	ScaledResolution var5 = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
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
    	ITextureObject texture = Minecraft.getMinecraft().renderEngine.getTexture(textureBiome);
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