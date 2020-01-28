package mod.uncharted;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.opengl.GL11;

public class GuiBiomePanel extends Gui {

    /** Minecraft Instance */
    private Minecraft mc;
    /** Biome the Player is currently in */
    Biome biomePanel;
    /** String version of the current Biome */
    ResourceLocation biomeTexture;
    /** Trigger if Panel moves up or down */
    boolean transitionUp;
    /** Upward Position of Panel */
    float timer;
    /** Absolute X Position of Panel (upper left edge) */
    int posX;
    /** Absolute Y Position of Panel (upper left edge) */
    int posY;
    /** Maximum Upward Position */
    int timerMax;



    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor */
    public GuiBiomePanel(Minecraft mc) {
        super();
        this.mc = mc;
        biomePanel  = Biomes.THE_END;
        transitionUp = true;
        timer = 0.00f;
        timerMax = 200;
        posX  = 0;
        posY  = 0;
        biomeTexture = new ResourceLocation(Uncharted.MODID + ":" + "textures/uncharted.png");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onTravel);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRenderExperienceBar);
    }



    //----------------------------------------EVENTS----------------------------------------//

    /** Event fires on Player Movement, checks if Player moves into a new Biome */
    @SubscribeEvent
    public void onTravel(LivingEvent.LivingUpdateEvent event){
        boolean newBiome = false;
        if(event.getEntity() instanceof EntityPlayer){
            if(event.getEntity().dimension == DimensionType.OVERWORLD){ // Checks for Overworld
                if(event.getEntity().world.canBlockSeeSky(event.getEntity().getPosition())){
                    if(biomePanel != event.getEntity().world.getBiome(event.getEntity().getPosition())){
                        newBiome = true;
                    }
                }
            } else { // Other dimensions might have ceiling, hence cannot check for .canBlockSeeSky()
                if(biomePanel != event.getEntity().world.getBiome(event.getEntity().getPosition())){
                    newBiome = true;
                }
            }
        }
        if(newBiome){ // trigger for when we travel into a new Biome
            biomePanel = event.getEntity().world.getBiome(event.getEntity().getPosition());
            transitionUp = true;
            biomeTexture = new ResourceLocation(Uncharted.MODID , "textures/" + biomePanel.getRegistryName().getPath() + ".png");
        }
    }

    /** Hooks into Render Event for Experience Bar, draws Biome Panel */
    @SubscribeEvent
    public void onRenderExperienceBar(RenderGameOverlayEvent event){
        // We don't want to draw into the Helmet Render Event
        if(event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.HELMET){
            return;
        }

        int fps = Minecraft.getDebugFPS();
        if(fps == 0){
            fps = 1;
        } else {
            fps = 120 / fps;
        }


        if(transitionUp){
            if(timer >= timerMax){
                timer = timerMax;
                transitionUp = false;
            } else {
                timer += 1;
                //timer += fps;
                //timer += event.getPartialTicks()/2;
            }
        } else {
            if(timer <= 0){
                timer = 0;
            } else {
                timer -= 1;
                //timer -= fps;
                //timer -= event.getPartialTicks()/2;
            }
        }

        if(timer > 0){
            posX = mc.mainWindow.getScaledWidth()-10;
            posY = mc.mainWindow.getScaledHeight();
            int u = (int)(posY-timer/2);
            if(timer > 130){
                u = posY-130/2;
            }
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            ITextureObject texture = mc.getTextureManager().getTexture(biomeTexture);
            if(texture instanceof DynamicTexture){
                biomeTexture = new ResourceLocation(Uncharted.MODID, "textures/uncharted.png");
                this.mc.getTextureManager().bindTexture(biomeTexture);
                this.drawTexturedModalRect(posX-120, u, 0, 0, 120, 64);
            }else if(texture == null){
                this.mc.getTextureManager().bindTexture(biomeTexture);
                this.drawTexturedModalRect(posX-120, u, 0, 0, 120, 64);
            }else{
                this.mc.getTextureManager().bindTexture(biomeTexture);
                this.drawTexturedModalRect(posX-120, u, 0, 0, 120, 64);
            }
        }
    }

}
