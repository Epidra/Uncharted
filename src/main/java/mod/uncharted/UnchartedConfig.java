package mod.uncharted;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class UnchartedConfig {

    private static ConfigValue<Boolean> cv_smallFrame;
    private static ConfigValue<Boolean> cv_borderLower;
    private static ConfigValue<Boolean> cv_borderLeft;
    private static ConfigValue<Integer> cv_scale;
    private static ConfigValue<Integer> cv_timer;
    private static ConfigValue<Integer> cv_speed;

    public static boolean smallFrame = true;
    public static boolean borderLower = true;
    public static boolean borderLeft = false;
    public static int scale = 100;
    public static int timer = 100;
    public static int speed = 200;





    //----------------------------------------Initialize----------------------------------------//

    public static void init() {
        Pair<Loader, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Loader::new);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, specPair.getRight());
    }





    //----------------------------------------Load----------------------------------------//

    @SuppressWarnings("unchecked")
    public static void load() {
        //debugger = cv_debugger.get();
        smallFrame = cv_smallFrame.get();
        borderLower = cv_borderLower.get();
        borderLeft = cv_borderLeft.get();
        scale = cv_scale.get();
        timer = cv_timer.get();
        speed = cv_speed.get();
    }





    //----------------------------------------LOADER_CLASS----------------------------------------//

    static class Loader {
        public Loader(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            builder.comment("If TRUE uses a Panel with Half the Height");
            cv_smallFrame = builder.define("Small Frame", smallFrame);
            builder.comment("Should the Panel appear from the Bottom");
            cv_borderLower = builder.define("Lower Border", borderLower);
            builder.comment("Should the Panel appear from the Left");
            cv_borderLeft = builder.define("Left Border", borderLeft);
            builder.comment("Sets an additional Scaling to the Panel");
            cv_scale = builder.define("Scale", scale);
            builder.comment("Sets how long the Panel stays on the Screen");
            cv_timer = builder.define("Timer", timer);
            builder.comment("Sets how fast the Panel moves");
            cv_speed = builder.define("Speed", speed);
            builder.pop();
        }
    }



}
