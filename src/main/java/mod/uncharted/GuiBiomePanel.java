package mod.uncharted;

import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import java.io.FileNotFoundException;
import java.util.Random;

public class GuiBiomePanel extends AbstractGui {

    /** Minecraft Instance */
    private Minecraft mc;
    /** Biome the Player is currently in */
    Biome biomePanel;
    /** String version of the current Biome */
    ResourceLocation biomeTexture;
    /** String version of the Static */
    ResourceLocation biomeTextureStatic;
    /** String version of the Overlay */
    ResourceLocation biomeTextureOverlay;
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
    /** Maximum Upward Position */
    String[] biomeName = new String[]{"", "", ""};
    String entering = "ENTERING...WORLD";

    //boolean debugger = true;
    boolean smallFrame = true;
    float scale = 1.0f;
    int height = 256;
    boolean borderLower = true;
    boolean borderLeft = false;

    boolean animated = false;
    boolean newBiome = false;
    String[] stringlist = new String[]{"beach"};
    int listindex = 0;
    //boolean testing = true;



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
        biomeTexture        = new ResourceLocation(Uncharted.MODID, "textures/static.png");
        biomeTextureStatic  = new ResourceLocation(Uncharted.MODID, "textures/static.png");
        biomeTextureOverlay = new ResourceLocation(Uncharted.MODID, "textures/overlay.png");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onTravel);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRenderExperienceBar);
        //debugger = UnchartedConfig.debugger;
        smallFrame = UnchartedConfig.smallFrame;
        scale = (float)UnchartedConfig.scale/100.00f;
        borderLower = UnchartedConfig.borderLower;
        borderLeft = UnchartedConfig.borderLeft;
        if(smallFrame){
            height = 32;
            timerMax -=56;
        } else {
            height = 64;
        }
        //if(debugger){
            //DebugBiome();
        //}
    }

    private void DebugBiome(){
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            LoadBiome();
        }
        //debugger = false;
    }

    private void LoadBiome(){
        ITextureObject texture = mc.getTextureManager().getTexture(new ResourceLocation(Uncharted.MODID , "textures/biomes/" + stringlist[listindex] + ".png"));
        //if(testing){
       //     listindex++;
       //     if(listindex == stringlist.length){
       //         listindex = 0;
       //         testing = false;
       //     }
       // } else {
            if(texture instanceof DynamicTexture) { // texture file not found
                listindex++;
                if (listindex == stringlist.length) {
                    //if (debugger) Uncharted.LOGGER.info("MISSING:  " + stringlist[0]);
                    biomeTexture = biomeTextureStatic;
                    animated = true;
                    //newBiome = false;
                    listindex = 0;
                }
            } else {
                //if(debugger) {
                //    if(listindex == 0) {
                //        Uncharted.LOGGER.info("LOADING:  " + stringlist[listindex]);
                //    } else {
                //        Uncharted.LOGGER.info("SETTING:  " + stringlist[0] + "  <---  " + stringlist[listindex]);
                //    }
                //}
                biomeTexture = new ResourceLocation(Uncharted.MODID , "textures/biomes/" + stringlist[listindex] + ".png");
                animated = false;
                //newBiome = false;
            }
       // }
    }



    //----------------------------------------EVENTS----------------------------------------//

    /** Event fires on Player Movement, checks if Player moves into a new Biome */
    @SubscribeEvent
    public void onTravel(LivingEvent.LivingUpdateEvent event){
        newBiome = false;
        //if(!newBiome){
            if(event.getEntity() instanceof PlayerEntity){
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
                //LoadBiome(biomePanel);
                String translatedKey = net.minecraft.client.resources.I18n.format(biomePanel.getTranslationKey());
                entering = net.minecraft.client.resources.I18n.format("gui.uncharted.entering");
                biomeName = translatedKey.split(" ");
                String[] templist = biomePanel.getRegistryName().getPath().split("_");
                if(templist.length > 1){
                    stringlist = new String[templist.length + 1];
                    stringlist[0] = biomePanel.getRegistryName().getPath();
                    for(int i = 0; i < templist.length; i++){
                        stringlist[i+1] = templist[i];
                    }
                } else {
                    stringlist = new String[1];
                    stringlist[0] = biomePanel.getRegistryName().getPath();
                }
                listindex = 0;
                //testing = true;
            }
        //}
    }

    /** Hooks into Render Event for Experience Bar, draws Biome Panel */
    @SubscribeEvent
    public void onRenderExperienceBar(RenderGameOverlayEvent event){
        // We don't want to draw into the Helmet Render Event
        if(event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.HELMET){
            return;
        }


        if(transitionUp){
            if(timer >= timerMax){
                timer = timerMax;
                transitionUp = false;
            } else {
                timer += 1;
            }
        } else {
            if(timer <= 0){
                timer = 0;
            } else {
                timer -= 1;
            }
        }

        //if(newBiome){
        //    LoadBiome();
        //}

        if(timer > 0){
            LoadBiome();
            int v = 150;
            if(smallFrame) v-=56;
            int u = timer > v ? v/2 : (int)(timer/2);
            posX = borderLeft ? 10 : mc.mainWindow.getScaledWidth()-10-128;
            posY = borderLower ? (int)(mc.mainWindow.getScaledHeight()-u+8) : -(height+10)+u;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            this.mc.getTextureManager().bindTexture(biomeTextureOverlay);
            this.blit(posX-4, posY-4, 0,smallFrame ? 128 : 0, 128+8, 64+8);
            this.mc.getTextureManager().bindTexture(biomeTexture);
            if(animated){
                Random r = new Random();
                this.blit(posX, posY, r.nextInt(128), r.nextInt(256-64), 128, height);
            } else {
                this.blit(posX, posY + (smallFrame ? 1 : 0), 0, smallFrame ? 16 : 0, 128, height);
            }
            GL11.glPushMatrix();
            GL11.glScalef(scale, scale, scale); {
                DrawString(entering, posX + 2, posY + 2, false);
                for(int i = 0; i < biomeName.length; i++){
                    DrawString(biomeName[i], posX + 124, posY + height - 10*biomeName.length + i*10, true);
                }
            } GL11.glPopMatrix();
        }
    }

    private void DrawString(String text, int posX, int posY, boolean rightsided){
        if(rightsided){
            drawRightAlignedString(mc.fontRenderer, text, (int)((posX + 0)/scale), (int)((posY + 0)/scale), 0);
            drawRightAlignedString(mc.fontRenderer, text, (int)((posX + 1)/scale), (int)((posY + 1)/scale), 16777215);
        } else {
            drawString(mc.fontRenderer, text, (int)((posX + 0)/scale), (int)((posY + 0)/scale), 0);
            drawString(mc.fontRenderer, text, (int)((posX + 1)/scale), (int)((posY + 1)/scale), 16777215);
        }
    }

}