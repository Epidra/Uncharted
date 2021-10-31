package mod.uncharted;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import java.util.Random;

public class GuiBiomePanel extends AbstractGui {

    /** Minecraft Instance */
    private Minecraft mc;
    /** Biome the Player is currently in */
    private Biome biomePanel;
    /** String version of the current Biome */
    private ResourceLocation biomeTexture;
    /** String version of the Static */
    private ResourceLocation biomeTextureStatic;
    /** String version of the Overlay */
    private ResourceLocation biomeTextureOverlay;
    /** Trigger if Panel moves up or down */
    private boolean transitionUp;
    /** Upward Position of Panel */
    private float timer;
    /** Absolute X Position of Panel (upper left edge) */
    private int posX;
    /** Absolute Y Position of Panel (upper left edge) */
    private int posY;
    /** Maximum Upward Position */
    private int timerMax;
    /** Dissecting String of Biome Name */
    private String[] biomeName = new String[]{"", "", ""};
    /** Localized string of ENTERING message **/
    private String entering = "ENTERING...WORLD";
    /** When active uses a Frame with Half Height **/
    private boolean smallFrame = true;
    /** Additional Scaling of the Frame **/
    private float scale = 1.0f;
    /** Speed in which the Frame moves **/
    private float speed = 1.0f;
    /** Height of the Frame **/
    private int height = 256;
    /** Should the Frame appear from the Bottom **/
    private boolean borderLower = true;
    /** Should the Frame appear from the Left **/
    private boolean borderLeft = false;
    /** Used to Animate the Static Image **/
    private boolean animated = false;
    /** Unlocalized Name of the Biome, divided by _, [0] is the full name **/
    private String[] stringlist = new String[]{"beach"};
    /** Current Position in the stringlist **/
    private int listindex = 0;





    //----------------------------------------CONSTRUCTOR----------------------------------------//

    /** Default Constructor */
    public GuiBiomePanel(Minecraft mc) {
        super();
        this.mc = mc;
        biomePanel  = null;
        transitionUp = true;
        timer = 0.00f;
        posX  = 0;
        posY  = 0;
        biomeTexture        = new ResourceLocation(Uncharted.MODID, "textures/static.png");
        biomeTextureStatic  = new ResourceLocation(Uncharted.MODID, "textures/static.png");
        biomeTextureOverlay = new ResourceLocation(Uncharted.MODID, "textures/overlay.png");
        MinecraftForge.EVENT_BUS.addListener(this::onTravel);
        MinecraftForge.EVENT_BUS.addListener(this::onRenderExperienceBar);
        smallFrame = UnchartedConfig.smallFrame;
        scale = (float)UnchartedConfig.scale / 100.00f;
        borderLower = UnchartedConfig.borderLower;
        borderLeft = UnchartedConfig.borderLeft;
        timerMax = 100 + UnchartedConfig.timer;
        speed = (float)UnchartedConfig.speed / 100.00f;
        if(smallFrame){
            height = 32;
            timerMax -=56;
        } else {
            height = 64;
        }
    }





    //----------------------------------------EVENTS----------------------------------------//

    /** Event fires on Player Movement, checks if Player moves into a new Biome */
    @SubscribeEvent
    public void onTravel(LivingEvent.LivingUpdateEvent event){
        boolean newBiome = false;
        if(event.getEntity() instanceof PlayerEntity){
            if(event.getEntity().level.dimensionType().hasSkyLight()){ // Checks for Overworld
                if(event.getEntity().level.canSeeSky(event.getEntity().blockPosition())){
                    if(biomePanel == null || biomePanel != event.getEntity().level.getBiome(event.getEntity().blockPosition())){
                        newBiome = true;
                    }
                }
            } else { // Other dimensions might have ceiling, hence cannot check for .canBlockSeeSky()
                if(biomePanel == null || biomePanel != event.getEntity().level.getBiome(event.getEntity().blockPosition())){
                    newBiome = true;
                }
            }
        }
        if(newBiome){ // trigger for when we travel into a new Biome
            biomePanel = event.getEntity().level.getBiome(event.getEntity().blockPosition());
            transitionUp = true;
            String a = I18n.get(biomePanel.getRegistryName().getPath());
            String b = I18n.get(biomePanel.getRegistryName().getNamespace());
            String translatedKey = I18n.get("biome." + b + "." + a);
            entering = I18n.get("gui.uncharted.entering");
            biomeName = translatedKey.split(" ");
            String[] templist = biomePanel.getRegistryName().getPath().split("_");
            if(templist.length > 1){
                stringlist = new String[templist.length + 1];
                stringlist[0] = biomePanel.getRegistryName().getPath();
                System.arraycopy(templist, 0, stringlist, 1, templist.length);
            } else {
                stringlist = new String[1];
                stringlist[0] = biomePanel.getRegistryName().getPath();
            }
            listindex = 0;
        }
    }

    /** Hooks into Tick Event to update panel position on constant speed */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(transitionUp){
            if(timer >= timerMax){
                timer = timerMax;
                transitionUp = false;
            } else {
                timer += speed;
            }
        } else {
            if(timer <= 0){
                timer = 0;
            } else {
                timer -= speed;
            }
        }
    }

    /** Hooks into Render Event for Experience Bar, draws Biome Panel */
    @SubscribeEvent
    public void onRenderExperienceBar(RenderGameOverlayEvent event){
        // We don't want to draw into the Helmet Render Event
        if(event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.HELMET){
            return;
        }
        if(timer > 0){
            LoadBiome();
            int v = 150;
            if(smallFrame) v-=56;
            int u = timer > v ? v/2 : (int)(timer/2);
            posX = borderLeft ? 10 : mc.getWindow().getGuiScaledWidth()-10-128;
            posY = borderLower ? (int)(mc.getWindow().getGuiScaledHeight()-u+8) : -(height+10)+u;
            this.mc.getTextureManager().bind(biomeTextureOverlay);
            this.blit(event.getMatrixStack(), posX-4, posY-4, 0,smallFrame ? 128 : 0, 128+8, 64+8);
            this.mc.getTextureManager().bind(biomeTexture);
            if(animated){
                Random r = new Random();
                this.blit(event.getMatrixStack(), posX, posY + (smallFrame ? 1 : 0), r.nextInt(128), r.nextInt(256-64), 128, height);
            } else {
                this.blit(event.getMatrixStack(), posX, posY + (smallFrame ? 1 : 0), 0, smallFrame ? 16 : 0, 128, height);
            }
            GL11.glPushMatrix();
            GL11.glScalef(scale, scale, scale); {
                DrawString(event.getMatrixStack(), entering, posX + 2, posY + 2, false);
                for(int i = 0; i < biomeName.length; i++){
                    DrawString(event.getMatrixStack(), biomeName[i], posX + 124, posY + height - 10*biomeName.length + i*10, true);
                }
            } GL11.glPopMatrix();
        }
    }





    //----------------------------------------SUPPORT----------------------------------------//

    private void LoadBiome(){
        Texture texture = mc.getTextureManager().getTexture(new ResourceLocation(Uncharted.MODID , "textures/biomes/" + stringlist[listindex] + ".png"));
        if(texture instanceof DynamicTexture) { // texture file not found
            listindex++;
            if (listindex == stringlist.length) {
                biomeTexture = biomeTextureStatic;
                animated = true;
                listindex = 0;
            }
        } else {
            biomeTexture = new ResourceLocation(Uncharted.MODID , "textures/biomes/" + stringlist[listindex] + ".png");
            animated = false;
        }
    }

    private void DrawString(MatrixStack stack, String text, int posX, int posY, boolean rightsided){
        if(rightsided){
            int z = mc.font.width(text)/2;
            drawCenteredString(stack, mc.font, text, (int)((posX     - z)/scale), (int)((posY    )/scale), 0);
            drawCenteredString(stack, mc.font, text, (int)((posX + 1 - z)/scale), (int)((posY + 1)/scale), 16777215);
        } else {
            drawString(stack, mc.font, text, (int)((posX    )/scale), (int)((posY    )/scale), 0);
            drawString(stack, mc.font, text, (int)((posX + 1)/scale), (int)((posY + 1)/scale), 16777215);
        }
    }

}
