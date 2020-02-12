package mod.uncharted;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class UnchartedConfig {

    //private static ConfigValue<Boolean> cv_debugger;
    private static ConfigValue<Boolean> cv_smallFrame;
    private static ConfigValue<Boolean> cv_borderLower;
    private static ConfigValue<Boolean> cv_borderLeft;
    private static ConfigValue<Integer> cv_scale;

    //public static boolean debugger = true;
    public static boolean smallFrame = true;
    public static boolean borderLower = true;
    public static boolean borderLeft = false;
    public static int scale = 100;

    public static void init() {
        Pair<Loader, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Loader::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        //debugger = cv_debugger.get();
        smallFrame = cv_smallFrame.get();
        borderLower = cv_borderLower.get();
        borderLeft = cv_borderLeft.get();
        scale = cv_scale.get();
    }

    static class Loader {
        public Loader(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            //cv_debugger = builder.define("Debugger", debugger);
            cv_smallFrame = builder.define("Small Frame", smallFrame);
            cv_borderLower = builder.define("Lower Border", borderLower);
            cv_borderLeft = builder.define("Left Border", borderLeft);
            cv_scale = builder.define("Scale", scale);
            builder.pop();
        }
    }
}
