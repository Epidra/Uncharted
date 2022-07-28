package mod.uncharted;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class Config {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ConfigPart CONFIG = new ConfigPart(BUILDER);





    //----------------------------------------CONFIG_PART----------------------------------------//

    public static class ConfigPart {
        public final ForgeConfigSpec.BooleanValue smallFrame;
        public final ForgeConfigSpec.BooleanValue borderLower;
        public final ForgeConfigSpec.BooleanValue borderLeft;
        public final ForgeConfigSpec.IntValue     timer;
        public final ForgeConfigSpec.IntValue     speed;

        ConfigPart(ForgeConfigSpec.Builder builder){
            builder.push("Panel Position and Movement");
            smallFrame = builder.define("smallframe", true);
            borderLower = builder.define("borderlower", true);
            borderLeft = builder.define("borderleft", false);
            timer  = builder.defineInRange("timer",  100,  0, 500);
            speed  = builder.defineInRange("speed",  100,  0, 500);
            builder.pop();
        }
    }





    //----------------------------------------BUILDER----------------------------------------//

    public static final ForgeConfigSpec spec = BUILDER.build();



}
