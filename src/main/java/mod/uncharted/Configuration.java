package mod.uncharted;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraft.world.level.biome.Biomes.*;

@SuppressWarnings("unused")
public class Configuration {
	
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	public static final ConfigPanel UNCHARTED = new ConfigPanel(BUILDER);
	
	public static class ConfigPanel {
		public final ForgeConfigSpec.BooleanValue smallFrame;
		public final ForgeConfigSpec.BooleanValue borderLower;
		public final ForgeConfigSpec.BooleanValue borderLeft;
		public final ForgeConfigSpec.IntValue     timer;
		public final ForgeConfigSpec.IntValue     speed;
		
		ConfigPanel(ForgeConfigSpec.Builder builder){
			builder.push("Panel Position and Movement");
			smallFrame = builder.define("smallframe", true);
			borderLower = builder.define("borderlower", true);
			borderLeft = builder.define("borderleft", false);
			timer  = builder.defineInRange("timer",  100,  0, 500);
			speed  = builder.defineInRange("speed",  100,  0, 500);
			builder.pop();
		}
	}
	
	public static final ForgeConfigSpec spec = BUILDER.build();
	
}