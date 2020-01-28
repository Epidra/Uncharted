package mod.uncharted;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class Debug {

    public static void createLog(){
        List<ResourceLocation> listKeys = new ArrayList<ResourceLocation>();
        List<Biome> listName = new ArrayList<Biome>();
        listKeys.addAll(ForgeRegistries.BIOMES.getKeys());
        listName.addAll(ForgeRegistries.BIOMES.getValues());
        System.out.println("Creating Bioe List");
        for(ResourceLocation rl : listKeys) {
            System.out.println(rl);
        }
        System.out.println();
        System.out.println("Searching for Texture Data...");
        boolean found = true;
        for(Biome b : listName){

            ResourceLocation rl = new ResourceLocation(Uncharted.modid + ":" + "textures/" + b.getBiomeName().toLowerCase() + ".png");
            ITextureObject texture = texture = Minecraft.getMinecraft().renderEngine.getTexture(rl);
            try {

            } catch (Exception e){

            }
            if(texture instanceof DynamicTexture){
                System.out.println(b.getBiomeName() + ".png" + " not found. DYNAMIC");
                found = false;
            }else if(texture == null){
                System.out.println(b.getBiomeName().toLowerCase() + ".png" + " not found. NULL");
                found = false;
            }else{

            }
        }
        if(found){
            System.out.println("Äll registered Biomes found.");
        }

    }

}